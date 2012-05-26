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
//TODO keep a best-guess location
public class Location extends ThrottledListeningElement implements GpsStatus.Listener, GpsStatus.NmeaListener {
	
	public interface ProviderCallback extends ListeningElement.Callback {
		/** Corresponds to LocationListener.onLocationChanged() */
		void onLocationChanged(ProviderWrapper providerWrapper);
		/** Corresponds to LocationListener.onProviderDisabled() */
		void onProviderDisabled(ProviderWrapper providerWrapper);
		/** Corresponds to LocationListener.onProviderEnabled() */
		void onProviderEnabled(ProviderWrapper providerWrapper);
		/** Corresponds to LocationListener.onStatusChanged() */
		void onStatusChanged(ProviderWrapper providerWrapper);		
		/** Custom callback that is called after the Geocoder updates the closest address */
		void onAddressChanged(ProviderWrapper providerWrapper);		
	}
	
	public interface GpsCallback extends ListeningElement.Callback {
		/** Corresponds to GpsStatus.Listener.onGpsStatusChanged() */
		void onGpsStatusChanged(Location location);
		/** Corresponds to GpsStatus.NmeaListener.onNmeaReceived() */		
		void onNmeaReceived(Location location);
	}
	
	//TODO implement this
	public static final int THROTTLE_COUNT = 3;
	public static final int THROTTLE_INDEX_GPSSTATUS = 0;
	public static final int THROTTLE_INDEX_LOCATION = 1;
	public static final int THROTTLE_INDEX_ADDRESS = 2;
	
	// GPS Status update throttle (ms)
	public static final int GPSSTATUS_FREQUENCY_HIGH = 1000;
	public static final int GPSSTATUS_FREQUENCY_MEDIUM = 2000;
	public static final int GPSSTATUS_FREQUENCY_LOW = 3000;
	
	// Network/GPS location update throttle (ms)
	public static final int LOCATION_FREQUENCY_HIGH = 1000;
	public static final int LOCATION_FREQUENCY_MEDIUM = 2000;
	public static final int LOCATION_FREQUENCY_LOW = 5000;
	
	// Reverse geocoding of location to an address throttle (ms)
	public static final int ADDRESS_FREQUENCY_HIGH = 2000;
	public static final int ADDRESS_FREQUENCY_MEDIUM = 5000;
	public static final int ADDRESS_FREQUENCY_LOW = 10000;
	
	private final LocationManager mLocationManager;
	private final Geocoder mGeocoder;	
	private final List<ProviderWrapper> mProviders;
	
	private GpsCallback mGpsCallback;	
	
	private GpsStatus mGpsStatus;		
	private int mLastGpsStatusEvent;
	private long mLastNmeaTimestamp;
	private String mLastNmea;
	
	private long mLastGpsStatusTimestamp;
	private int mGpsStatusUpdateFrequency;
	
	public final String STATUS_AVAILABLE;
	public final String STATUS_OUT_OF_SERVICE;
	public final String STATUS_TEMPORARILY_UNAVAILABLE;	
	public final String ACCURACY_COARSE;
	public final String ACCURACY_FINE;	
	public final String POWER_REQUIREMENT_HIGH;
	public final String POWER_REQUIREMENT_MEDIUM;
	public final String POWER_REQUIREMENT_LOW;
	public final String POWER_REQUIREMENT_NONE;	
	public final String GPS_EVENT_FIRST_FIX;
	public final String GPS_EVENT_SATELLITE_STATUS;
	public final String GPS_EVENT_STARTED;
	public final String GPS_EVENT_STOPPED;
	
	public Location(Context context) {
		super(THROTTLE_COUNT);
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mGeocoder = new Geocoder(context, Locale.getDefault());
		mProviders = new ArrayList<ProviderWrapper>();
		
		STATUS_AVAILABLE = context.getString(R.string.location_available);
		STATUS_OUT_OF_SERVICE = context.getString(R.string.location_out_of_service);
		STATUS_TEMPORARILY_UNAVAILABLE = context.getString(R.string.location_temporarily_unavailable);		
		ACCURACY_COARSE = context.getString(R.string.location_accuracy_coarse);
		ACCURACY_FINE = context.getString(R.string.location_accuracy_fine);		
		POWER_REQUIREMENT_HIGH = context.getString(R.string.location_power_requirement_high);
		POWER_REQUIREMENT_MEDIUM = context.getString(R.string.location_power_requirement_medium);
		POWER_REQUIREMENT_LOW = context.getString(R.string.location_power_requirement_low);
		POWER_REQUIREMENT_NONE = context.getString(R.string.location_power_requirement_none);		
		GPS_EVENT_FIRST_FIX = context.getString(R.string.gps_first_fix);
		GPS_EVENT_SATELLITE_STATUS = context.getString(R.string.gps_satellite_status);
		GPS_EVENT_STARTED = context.getString(R.string.gps_started);
		GPS_EVENT_STOPPED = context.getString(R.string.gps_stopped);
		
		for (String s : mLocationManager.getAllProviders()) {
			mProviders.add(new ProviderWrapper(s));
		}
		
		// These two are actually unrelated
		mGpsStatusUpdateFrequency = GPSSTATUS_FREQUENCY_MEDIUM;
		updateGpsStatus();
	}
	
	public LocationManager getLocationManager() {
		return mLocationManager;
	}
	
	public List<ProviderWrapper> getProviders() {
		return mProviders;
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
	
	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		if (!super.startListening(onlyIfCallbackSet)) return false;
		mLocationManager.addGpsStatusListener(this);
		mLocationManager.addNmeaListener(this);
		for (ProviderWrapper pw : mProviders) {
			pw.startListening(onlyIfCallbackSet);
		}
		return setListening(true);
	}
	
	@Override
	public boolean stopListening() {
		if (!super.stopListening()) return false;
		mLocationManager.removeGpsStatusListener(this);
		mLocationManager.removeNmeaListener(this);
		for (ProviderWrapper pw : mProviders) {
			pw.stopListening();
		}
		return !setListening(false);
	}
	
	public boolean isAnyListening() {
		if (isListening()) return true;
		for (ProviderWrapper pw : mProviders) {
			if (pw.isListening()) return true;
		}
		return false;
	}
	
	public boolean isAllListening() {
		if (!isListening()) return false;
		for (ProviderWrapper pw : mProviders) {
			if (!pw.isListening()) return false;
		}
		return true;
	}
	
	public GpsStatus getGpsStatus() {
		return mGpsStatus;
	}
	
	public int getLastGpsStatusEvent() {
		return mLastGpsStatusEvent;
	}
	
	public String getLastGpsStatusEventString() {
		switch(mLastGpsStatusEvent) {
		case GpsStatus.GPS_EVENT_FIRST_FIX: return GPS_EVENT_FIRST_FIX;
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS: return GPS_EVENT_SATELLITE_STATUS;
		case GpsStatus.GPS_EVENT_STARTED: return GPS_EVENT_STARTED;
		case GpsStatus.GPS_EVENT_STOPPED: return GPS_EVENT_STOPPED;
		}
		return null;
	}
	
	public long getLastNmeaTimestamp() {
		return mLastNmeaTimestamp;
	}
	
	public String getLastNmea() {
		return mLastNmea;
	}
	
	private void updateGpsStatus() {
		mGpsStatus = mLocationManager.getGpsStatus(null);
	}
	
	/** 
	 * Gets the minimum time between GPS status updates in milliseconds.
	 */
	public int getGpsStatusUpdateFrequency() {
		return mGpsStatusUpdateFrequency;
	}
	
	/** 
	 * Sets the minimum time between GPS status updates in milliseconds.
	 */
	public void setAddressUpdateFrequency(int frequency) {
		mGpsStatusUpdateFrequency = frequency;
	}
	
	public long getLastGpsStatusTimestamp() {
		return mLastGpsStatusTimestamp;
	}
	
	@Override
	public void onGpsStatusChanged(int event) {
		long time = System.currentTimeMillis();
		if (time - mLastGpsStatusTimestamp < mGpsStatusUpdateFrequency) return;
		mLastGpsStatusTimestamp = time;
		mLastGpsStatusEvent = event;
		updateGpsStatus();
		if (getCallback() != null) ((GpsCallback) getCallback()).onGpsStatusChanged(this);			
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		mLastNmeaTimestamp = timestamp;
		mLastNmea = nmea;
		updateGpsStatus();
		if (getCallback() != null) ((GpsCallback) getCallback()).onNmeaReceived(this);
	}
	
	
	public class ProviderWrapper implements ContentsMapper, LocationListener {
		private final String mProviderString;
		private LocationProvider mProvider;
		private ProviderCallback mCallback;
		
		private long mMinTime;
		private float mMinDistance;
		protected boolean mIsListening;		
		private android.location.Location mLocation;
		private android.location.Location mLastGeocodedLocation;
		private int mStatus;
		private Bundle mExtras;		
		private Address mAddress;
		
		private long mLastLocationTimestamp;
		private long mLastAddressTimestamp;
		private int mAddressUpdateFrequency;
		
		private ProviderWrapper(String provider) {
			mProviderString = provider;
			mMinTime = LOCATION_FREQUENCY_MEDIUM;
			mAddressUpdateFrequency = ADDRESS_FREQUENCY_MEDIUM;
			updateProvider();
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
		
		public ProviderCallback getCallback() {
			return mCallback;
		}
		
		public void setCallback(ProviderCallback callback) {
			mCallback = callback;
		}

		/** 
		 * Gets the minimum time between location updates in milliseconds.
		 */
		public long getMinTime() {
			return mMinTime;
		}
		
		/** 
		 * Sets the minimum time between location updates in milliseconds.
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
		
		/** 
		 * Gets the minimum time between address updates from the Geocoder in 
		 * milliseconds.
		 */
		public int getAddressUpdateFrequency() {
			return mAddressUpdateFrequency;
		}
		
		/** 
		 * Sets the minimum time between address updates from the Geocoder in 
		 * milliseconds. See
		 * {@link https://developers.google.com/maps/articles/geocodestrat#quota-limits}
		 * for limits.
		 */
		public void setAddressUpdateFrequency(int frequency) {
			mAddressUpdateFrequency = frequency;
		}
		
		/** 
		 * Gets the last location update timestamp in milliseconds.
		 */
		public long getLastLocationTimestamp() {
			return mLastLocationTimestamp;
		}
		
		/** 
		 * Gets the last address update timestamp in milliseconds.
		 */
		public long getLastAddressTimestamp() {
			return mLastAddressTimestamp;
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
			mIsListening = true;
		}
		
		public void stopListening() {
			if (!mIsListening) return;
			mLocationManager.removeUpdates(this);			
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
			case LocationProvider.AVAILABLE: return STATUS_AVAILABLE;
			case LocationProvider.OUT_OF_SERVICE: return STATUS_OUT_OF_SERVICE;
			case LocationProvider.TEMPORARILY_UNAVAILABLE: return STATUS_TEMPORARILY_UNAVAILABLE;
			}
			return null;
		}
		
		public String getAccuracyString() {
			switch (mProvider.getAccuracy()) {
			case Criteria.ACCURACY_COARSE: return ACCURACY_COARSE;
			case Criteria.ACCURACY_FINE: return ACCURACY_FINE;
			}
			return null;
		}
		
		public String getPowerRequirementString() {
			switch (mProvider.getPowerRequirement()) {
			case Criteria.POWER_HIGH: return POWER_REQUIREMENT_HIGH;
			case Criteria.POWER_LOW: return POWER_REQUIREMENT_MEDIUM;
			case Criteria.POWER_MEDIUM: return POWER_REQUIREMENT_LOW;
			case Criteria.NO_REQUIREMENT: return POWER_REQUIREMENT_NONE;
			}
			return null;
		}
		
		public Address getLastAddress() {
			return mAddress;
		}
		
		private void setLastAddresses(Address address) {
			mAddress = address;			
			if (mCallback != null) mCallback.onAddressChanged(this);
		}
		
		private boolean shouldGeocode(android.location.Location location) {			
			if (mLastGeocodedLocation == null) return true;
			return mLastGeocodedLocation.getLatitude() != location.getLatitude() 
				|| mLastGeocodedLocation.getLongitude() != location.getLongitude();
		}

		@Override
		public void onLocationChanged(android.location.Location location) {
			long time = System.currentTimeMillis();
			if (time - mLastLocationTimestamp < mMinTime) return;
			mLastLocationTimestamp = time;
			if (time - mLastAddressTimestamp >= mAddressUpdateFrequency
					&& shouldGeocode(location)) {
				mLastAddressTimestamp = time;
				mLastGeocodedLocation = location;
				new GeocoderTask(this).execute(location);
			}
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
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			
			// ProviderWrapper values
			contents.put("Type", getProviderString());
			contents.put("Accuracy", getAccuracyString());
			contents.put("Power", getPowerRequirementString());
			contents.put("Min Time (ms)", String.valueOf(getMinTime()));
			contents.put("Min Distance (m)", String.valueOf(getMinDistance()));
			contents.put("Last Location Timestamp (ms)", String.valueOf(getLastLocationTimestamp()));
			contents.put("Last Address Timestamp (ms)", String.valueOf(getLastAddressTimestamp()));
			contents.put("Address Update Frequency (ms)", String.valueOf(getAddressUpdateFrequency()));
			contents.put("Is Enabled", String.valueOf(isEnabled()));
			contents.put("Is Listening", String.valueOf(isListening()));
			contents.put("Last Status", getLastStatusString());
			if (getLastExtras() != null) {
				for (String s : getLastExtras().keySet()) {
					contents.put("Last Extras " + s, getLastExtras().getString(s));
				} 
			}
			else contents.put("Last Extras", null);
			
			// LocationProvider values			
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
		
		contents.put("Is Listening (GpsStatus)", String.valueOf(isListening()));
		contents.put("Best Provider index", String.valueOf(mProviders.indexOf(getBestProvider())));
		contents.put("Last GPS Status Timestamp", String.valueOf(getLastGpsStatusTimestamp()));
		contents.put("Last GPS Status Event", getLastGpsStatusEventString());
		contents.put("Last NMEA Timestamp", String.valueOf(getLastNmeaTimestamp()));
		contents.put("Last NMEA", getLastNmea());
		contents.put("GPS Status update frequency (ms)", String.valueOf(getGpsStatusUpdateFrequency()));
		
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
		
		for (int i = 0; i < mProviders.size(); ++i) {
			subcontents = mProviders.get(i).getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {
				contents.put("Provider " + i + " " + e.getKey(), e.getValue());
			}
		}
		
		return contents;
	}

}
