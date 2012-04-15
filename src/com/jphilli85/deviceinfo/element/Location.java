package com.jphilli85.deviceinfo.element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;

import com.jphilli85.deviceinfo.R;

public class Location implements ContentsMapper {
//	private static final int API = Build.VERSION.SDK_INT;
	
	public interface Callback {
		/** Corresponds to LocationListener.onLocationChanged() */
		void onLocationChanged(ProviderWrapper providerWrapper);
		/** Corresponds to LocationListener.onProviderDisabled() */
		void onProviderDisabled(ProviderWrapper providerWrapper);
		/** Corresponds to LocationListener.onProviderEnabled() */
		void onProviderEnabled(ProviderWrapper providerWrapper);
		/** Corresponds to LocationListener.onStatusChanged() */
		void onStatusChanged(ProviderWrapper providerWrapper);		
		/** Custom callback for use with the Geocoder. Use with geocodeLocation(). */
		void onGeocoderFinished(ProviderWrapper providerWrapper);
		/** Corresponds to GpsStatus.Listener.onGpsStatusChanged() */
		void onGpsStatusChanged(ProviderWrapper providerWrapper);
		/** Corresponds to GpsStatus.NmeaListener.onNmeaReceived() */		
		void onNmeaReceived(ProviderWrapper providerWrapper);
	}
	
	private final LocationManager mLocationManager;
	private final Geocoder mGeocoder;	
	private final List<ProviderWrapper> mProviders;
	
	public final String AVAILABLE;
	public final String OUT_OF_SERVICE;
	public final String TEMPORARILY_UNAVAILABLE;
	
	public final String COARSE;
	public final String FINE;
	
	public final String HIGH;
	public final String MEDIUM;
	public final String LOW;
	public final String NONE;
	
	public final String FIRST_FIX;
	public final String SATELLITE_STATUS;
	public final String STARTED;
	public final String STOPPED;
	
	public Location(Context context) {
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mGeocoder = new Geocoder(context, Locale.getDefault());
		mProviders = new ArrayList<ProviderWrapper>();
		
		AVAILABLE = context.getString(R.string.location_available);
		OUT_OF_SERVICE = context.getString(R.string.location_out_of_service);
		TEMPORARILY_UNAVAILABLE = context.getString(R.string.location_temporarily_unavailable);
		
		COARSE = context.getString(R.string.location_accuracy_coarse);
		FINE = context.getString(R.string.location_accuracy_fine);
		
		HIGH = context.getString(R.string.location_power_requirement_high);
		MEDIUM = context.getString(R.string.location_power_requirement_medium);
		LOW = context.getString(R.string.location_power_requirement_low);
		NONE = context.getString(R.string.location_power_requirement_none);
		
		FIRST_FIX = context.getString(R.string.gps_first_fix);
		SATELLITE_STATUS = context.getString(R.string.gps_satellite_status);
		STARTED = context.getString(R.string.gps_started);
		STOPPED = context.getString(R.string.gps_stopped);
		
		for (String s : mLocationManager.getAllProviders()) {
			mProviders.add(new ProviderWrapper(context, s));
		}
	}
	
	public LocationManager getLocationManager() {
		return mLocationManager;
	}
	
	public List<String> getAllProviders() {
		return mLocationManager.getAllProviders();
	}
	
	public ProviderWrapper getBestProvider() {
		return getBestProvider(new Criteria(), false);
	}
	
	public ProviderWrapper getBestProvider(Criteria criteria, boolean enabledOnly) {
		String provider = mLocationManager.getBestProvider(criteria, enabledOnly);
		for (ProviderWrapper pw : mProviders) {
			if (pw.getProviderString().equals(provider)) return pw;
		}
		return null;
	}
	
	/** Blocks thread if Geocoder is in use */
	public Geocoder getGeocoder() {
		synchronized (mGeocoder) {
			return mGeocoder;
		}
	}
	
	public void startListening() {
		startListening(true);
	}
	
	public void startListening(boolean onlyIfCallbackSet) {
		for (ProviderWrapper pw : mProviders) pw.startListening(onlyIfCallbackSet);
	}
	
	public void stopListening() {
		for (ProviderWrapper pw : mProviders) pw.stopListening();
	}
	
	public class ProviderWrapper implements ContentsMapper, LocationListener,
			GpsStatus.Listener, GpsStatus.NmeaListener {
		private final String mProviderString;
		private LocationProvider mProvider;
		private Callback mCallback;
//		/*
//		 * Odd way of doing things. Keeping a FineCallback reference here
//		 * allows pass on the LocationListener implementation to the 
//		 * better callback. The other option is to use a single wrapper class
//		 * and single callback interface that implements the coarse and fine
//		 * grained methods and have the fine grained methods be unusable
//		 * to coarse grained providers. I chose this way because it keeps
//		 * things behind the scenes and allows implementations to be cleaner
//		 * and less wasteful. 
//		 * 
//		 * A side effect is that a FineProviderWrapper can set a callback
//		 * using a CoarseCallback and/or a FineCallback, but can only
//		 * retrieve the FineCallback. There's a fairly ugly hack below to
//		 * try and thwart this.
//		 */
//		protected FineCallback mFineCallback;
		
		private long mMinTime;
		private float mMinDistance;
		
		protected boolean mIsListening;
		
		private android.location.Location mLocation;
		private int mStatus;
		private Bundle mExtras;
		
		private Address mAddress;		
		
		private GpsStatus mGpsStatus;
		
		private int mEvent;
		private long mTimestamp;
		private String mNmea;
		
		private ProviderWrapper(Context context, String provider) {
			mProviderString = provider;
			updateProvider();
			updateGpsStatus();
		}
		
		public android.location.Location getLastKnownLocation() {
			return mLocationManager.getLastKnownLocation(mProviderString);
		}
		
		private void updateProvider() {
			mProvider = mLocationManager.getProvider(mProviderString);
		}

		public String getProviderString() {
			return mProviderString;
		}
		
		public LocationProvider getProvider() {
			return mProvider;
		}
		
		public Callback getCallback() {
			return mCallback;
		}
		
		public void setCallback(Callback callback) {
			mCallback = callback;
		}

		/** 
		 * Gets the minimum time between location updates in milliseconds.
		 * Only used as a hint to the system, actual times may vary.
		 */
		public long getMinTime() {
			return mMinTime;
		}
		
		/** 
		 * Sets the minimum time between location updates in milliseconds.
		 * Only used as a hint to the system, actual times may vary.
		 */
		public void setMinTime(long minTime) {
			mMinTime = minTime;
		}
		
		/** 
		 * Gets the minimum distance change that triggers a location update 
		 * in meters.
		 */
		public float getMinDistance() {
			return mMinDistance;
		}
		
		/** 
		 * Sets the minimum distance change that triggers a location update 
		 * in meters.
		 */
		public void setMinDistance(float minDistance) {
			mMinDistance = minDistance;
		}
		
		public boolean isListening() {
			return mIsListening;
		}
		
		public void startListening() {
			startListening(true);
		}
		
		public void startListening(boolean onlyIfCallbackSet) {
			if (mIsListening || (onlyIfCallbackSet && mCallback == null)) return;
			mLocationManager.requestLocationUpdates(mProviderString, mMinTime, mMinDistance, this);
			mLocationManager.addGpsStatusListener(this);
			mLocationManager.addNmeaListener(this);
			mIsListening = true;
		}
		
		public void stopListening() {
			if (!mIsListening) return;
			mLocationManager.removeUpdates(this);
			mLocationManager.removeGpsStatusListener(this);
			mLocationManager.removeNmeaListener(this);
			mIsListening = false;
		}
		
		public boolean isEnabled() {
			return mLocationManager.isProviderEnabled(mProviderString);
		}
		
		public android.location.Location getLastLocation() {
			return mLocation;
		}
		
		public int getLastStatus() {
			return mStatus;
		}
		
		public Bundle getLastExtras() {
			return mExtras;
		}
		
		public String getLastStatusString() {
			switch (mStatus) {
			case LocationProvider.AVAILABLE: return AVAILABLE;
			case LocationProvider.OUT_OF_SERVICE: return OUT_OF_SERVICE;
			case LocationProvider.TEMPORARILY_UNAVAILABLE: return TEMPORARILY_UNAVAILABLE;
			}
			return null;
		}
		
		public String getAccuracyString() {
			switch (mProvider.getAccuracy()) {
			case Criteria.ACCURACY_COARSE: return COARSE;
			case Criteria.ACCURACY_FINE: return FINE;
			}
			return null;
		}
		
		public String getPowerRequirementString() {
			switch (mProvider.getPowerRequirement()) {
			case Criteria.POWER_HIGH: return HIGH;
			case Criteria.POWER_LOW: return MEDIUM;
			case Criteria.POWER_MEDIUM: return LOW;
			case Criteria.NO_REQUIREMENT: return NONE;
			}
			return null;
		}
		
		/** Calls onGeocoderFinished() when completed */
		public void geocodeLocation() {
			geocodeLocation(mLocation);
		}
		
		/** Calls onGeocoderFinished() when completed */
		public void geocodeLocation(android.location.Location location) {
			new GeocoderTask(this).execute(location);
		}
		
		public Address getLastAddress() {
			return mAddress;
		}
		
		private void setLastAddresses(Address address) {
			mAddress = address;			
			if (mCallback != null) mCallback.onGeocoderFinished(this);
		}
		
		public GpsStatus getGpsStatus() {
			return mGpsStatus;
		}
		
		public int getLastEvent() {
			return mEvent;
		}
		
		public String getLastEventString() {
			switch(mEvent) {
			case GpsStatus.GPS_EVENT_FIRST_FIX: return FIRST_FIX;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS: return SATELLITE_STATUS;
			case GpsStatus.GPS_EVENT_STARTED: return STARTED;
			case GpsStatus.GPS_EVENT_STOPPED: return STOPPED;
			}
			return null;
		}
		
		public long getLastTimestamp() {
			return mTimestamp;
		}
		
		public String getLastNmea() {
			return mNmea;
		}
		
		private void updateGpsStatus() {
			mGpsStatus = mLocationManager.getGpsStatus(null);
		}

		@Override
		public void onLocationChanged(android.location.Location location) {
			mLocation = location;
			updateProvider();
			if (mCallback != null) mCallback.onLocationChanged(this);
		}

		@Override
		public void onProviderDisabled(String provider) {
			updateProvider();
			if (mCallback != null) mCallback.onProviderDisabled(this);
		}

		@Override
		public void onProviderEnabled(String provider) {
			updateProvider();
			if (mCallback != null) mCallback.onProviderEnabled(this);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			mStatus = status;
			mExtras = extras;
			updateProvider();
			if (mCallback != null) mCallback.onStatusChanged(this);
		}

		@Override
		public void onGpsStatusChanged(int event) {
			mEvent = event;
			updateGpsStatus();
			if (mCallback != null) mCallback.onGpsStatusChanged(this);			
		}

		@Override
		public void onNmeaReceived(long timestamp, String nmea) {
			mTimestamp = timestamp;
			mNmea = nmea;
			updateGpsStatus();
			if (mCallback != null) mCallback.onNmeaReceived(this);
		}
		
		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			
			// ProviderWrapper values
			contents.put("Type", getProviderString());
			contents.put("Accuracy", getAccuracyString());
			contents.put("Power", getPowerRequirementString());
			contents.put("Min Time", String.valueOf(getMinTime()));
			contents.put("Min Distance", String.valueOf(getMinDistance()));
			contents.put("Is Enabled", String.valueOf(isEnabled()));
			contents.put("Is Listening", String.valueOf(isListening()));
			contents.put("Last Status", getLastStatusString());
			if (getLastExtras() != null) {
				for (String s : getLastExtras().keySet()) {
					contents.put("Last Extras " + s, getLastExtras().getString(s));
				} 
			}
			else contents.put("Last Extras", null);
			contents.put("Last Event", getLastEventString());
			contents.put("Last Timestamp", String.valueOf(getLastTimestamp()));
			contents.put("Last NMEA", getLastNmea());
			
			// LocationProvider values			
			contents.put("Name", mProvider.getName());			
			contents.put("HasMonetaryCost", String.valueOf(mProvider.hasMonetaryCost()));
			contents.put("Requires Cellular", String.valueOf(mProvider.requiresCell()));
			contents.put("Requires Network", String.valueOf(mProvider.requiresNetwork()));
			contents.put("Requires Satellite", String.valueOf(mProvider.requiresSatellite()));
			contents.put("Supports Altitude", String.valueOf(mProvider.supportsAltitude()));
			contents.put("Supports Bearing", String.valueOf(mProvider.supportsBearing()));
			contents.put("Supports Speed", String.valueOf(mProvider.supportsSpeed()));
			
			
			// Location values
			if (mLocation != null) {
				contents.put("Last Location Accuracy (m)", String.valueOf(mLocation.getAccuracy()));
				contents.put("Last Location Altitude (m?)", String.valueOf(mLocation.getAltitude()));
				contents.put("Last Location Bearing (°)", String.valueOf(mLocation.getBearing()));
				if (mLocation.getExtras() != null) {
					for (String s : mLocation.getExtras().keySet()) {
						contents.put("Last Location Extras " + s, mLocation.getExtras().getString(s));
					}
				}
				else contents.put("Last Location Extras", null);
				contents.put("Last Location Latitude", String.valueOf(mLocation.getLatitude()));
				contents.put("Last Location Longitude", String.valueOf(mLocation.getLongitude()));
				contents.put("Last Location Provider", mLocation.getProvider());
				contents.put("Last Location Speed", String.valueOf(mLocation.getSpeed()));
				contents.put("Last Location Timestamp", String.valueOf(mLocation.getTime()));
				contents.put("Last Location hasAccuracy", String.valueOf(mLocation.hasAccuracy()));
				contents.put("Last Location hasAltitude", String.valueOf(mLocation.hasAltitude()));
				contents.put("Last Location hasBearing", String.valueOf(mLocation.hasBearing()));
				contents.put("Last Location hasSpeed", String.valueOf(mLocation.hasSpeed()));
			}
			else contents.put("Last Location", null);
			
			if (mGpsStatus != null) {
				// GpsStatus values
				contents.put("Max Satellites", String.valueOf(mGpsStatus.getMaxSatellites()));			
				contents.put("Time to First Fix", String.valueOf(mGpsStatus.getTimeToFirstFix()));
				
				// GpsSatellite values
				int i = 0;
				Iterable<GpsSatellite> sats = mGpsStatus.getSatellites();
				if (sats != null) {
					for (GpsSatellite s : sats) {
						contents.put("Satellite " + i + " Azimuth (°)", String.valueOf(s.getAzimuth()));
						contents.put("Satellite " + i + " Elevation (°)", String.valueOf(s.getElevation()));
						contents.put("Satellite " + i + " PRN", String.valueOf(s.getPrn()));
						contents.put("Satellite " + i + " SNR", String.valueOf(s.getSnr()));
						contents.put("Satellite " + i + " hasAlmanac", String.valueOf(s.hasAlmanac()));
						contents.put("Satellite " + i + " hasEphemeris", String.valueOf(s.hasEphemeris()));
						contents.put("Satellite " + i + " usedInFix", String.valueOf(s.usedInFix()));
						++i;
					}
				}
				else contents.put("Satellites", null);
			}
			else contents.put("GpsStatus", null);
			
			if (mAddress != null) {
				// Address values
				int len = mAddress.getMaxAddressLineIndex() + 1;
				for (int i = 0; i < len; ++i) {
					contents.put("Last Address Line " + i, mAddress.getAddressLine(i));
				}
				contents.put("Last Address AdminArea", mAddress.getAdminArea());
				contents.put("Last Address CountryCode", mAddress.getCountryCode());
				contents.put("Last Address CountryName", mAddress.getCountryName());
				if (mAddress.getExtras() != null) {
					for (String s : mAddress.getExtras().keySet()) {
						contents.put("Last Address Extras " + s, mAddress.getExtras().getString(s));
					}
				}
				else contents.put("Last Address Extras", null);
				contents.put("Last Address FeatureName", mAddress.getFeatureName());
				contents.put("Last Address Latitude", String.valueOf(mAddress.getLatitude()));
				contents.put("Last Address Longitude", String.valueOf(mAddress.getLongitude()));
				contents.put("Last Address Phone", mAddress.getPhone());
				contents.put("Last Address PostalCode", mAddress.getPostalCode());
				contents.put("Last Address Premises", mAddress.getPremises());
				contents.put("Last Address SubAdminArea", mAddress.getSubAdminArea());
				contents.put("Last Address SubLocality", mAddress.getSubLocality());
				contents.put("Last Address SubThoroughfare", mAddress.getSubThoroughfare());
				contents.put("Last Address Thoroughfare", mAddress.getThoroughfare());
			}
			else contents.put("Last Address", null);

			return contents;
		}
	}

	private class GeocoderTask extends AsyncTask<android.location.Location, Void, Address> {
		private ProviderWrapper mProviderWrapper;
		private GeocoderTask(ProviderWrapper pw) {
			mProviderWrapper = pw;
		}
		@Override
		protected Address doInBackground(android.location.Location... params) {
			List<Address> addresses = null;
			synchronized (mGeocoder) {
				try {
					addresses = mGeocoder.getFromLocation(params[0].getLatitude(), 
							params[0].getLongitude(), 1);
				} 
				catch (IOException ignored) {}
				catch (IllegalArgumentException ignored) {}				
			}
			
			if (addresses != null && !addresses.isEmpty()) {
				return addresses.get(0);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Address result) {			
			mProviderWrapper.setLastAddresses(result);
		}
	}


	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> subcontents;
		
		contents.put("Best Provider index", String.valueOf(mProviders.indexOf(getBestProvider())));
		for (int i = 0; i < mProviders.size(); ++i) {
			subcontents = mProviders.get(i).getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {
				contents.put("Provider " + i + " " + e.getKey(), e.getValue());
			}
		}
		
		return contents;
	}

}
