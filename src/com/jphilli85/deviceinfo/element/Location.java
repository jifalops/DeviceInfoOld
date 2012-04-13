package com.jphilli85.deviceinfo.element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;

import com.jphilli85.deviceinfo.R;

public class Location implements ContentsMapper {
//	private static final int API = Build.VERSION.SDK_INT;
	
	public interface CoarseCallback {
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
	}
	
	public interface FineCallback extends CoarseCallback {
		/** Corresponds to GpsStatus.Listener.onGpsStatusChanged() */
		void onGpsStatusChanged(FineProviderWrapper fineProviderWrapper);
		/** Corresponds to GpsStatus.NmeaListener.onNmeaReceived() */		
		void onNmeaReceived(FineProviderWrapper fineProviderWrapper);
	}
	
	private final LocationManager mLocationManager;
	private final Geocoder mGeocoder;	
	
	public final String AVAILABLE;
	public final String OUT_OF_SERVICE;
	public final String TEMPORARILY_UNAVAILABLE;
	
	public Location(Context context) {
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mGeocoder = new Geocoder(context, Locale.getDefault());
		
		AVAILABLE = context.getString(R.string.location_available);
		OUT_OF_SERVICE = context.getString(R.string.location_out_of_service);
		TEMPORARILY_UNAVAILABLE = context.getString(R.string.location_temporarily_unavailable);
	}
	
	public LocationManager getLocationManager() {
		return mLocationManager;
	}
	
	public List<String> getAllProviders() {
		return mLocationManager.getAllProviders();
	}
	
	/** May block thread if Geocoder is in use */
	public Geocoder getGeocoder() {
		synchronized (mGeocoder) {
			return mGeocoder;
		}
	}
	
	public class ProviderWrapper implements LocationListener {
		private final String mProviderString;
		private LocationProvider mProvider;
		private CoarseCallback mCoarseCallback;
		/*
		 * Odd way of doing things. Keeping a FineCallback reference here
		 * allows pass on the LocationListener implementation to the 
		 * better callback. The other option is to use a single wrapper class
		 * and single callback interface that implements the coarse and fine
		 * grained methods and have the fine grained methods be unusable
		 * to coarse grained providers. I chose this way because it keeps
		 * things behind the scenes and allows implementations to be cleaner
		 * and less wasteful. 
		 * 
		 * A side effect is that a FineProviderWrapper can set a callback
		 * using a CoarseCallback and/or a FineCallback, but can only
		 * retrieve the FineCallback. There's a fairly ugly hack below to
		 * try and thwart this.
		 */
		protected FineCallback mFineCallback;
		
		private long mMinTime;
		private float mMinDistance;
		
		protected boolean mIsListening;
		private boolean mIsEnabled;
		
		private android.location.Location mLocation;
		private int mStatus;
		private Bundle mExtras;
		
		private Address mAddress;
		
		private ProviderWrapper(Context context, String provider) {
			mProviderString = provider;
			mProvider = mLocationManager.getProvider(provider);
		}

		public String getProviderString() {
			return mProviderString;
		}
		
		public LocationProvider getProvider() {
			return mProvider;
		}
		
		public CoarseCallback getCallback() {
			return mCoarseCallback;
		}
		
		public void setCallback(CoarseCallback callback) {
			mCoarseCallback = callback;
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
			if (mIsListening || (onlyIfCallbackSet 
					&& mCoarseCallback == null && mFineCallback == null)) return;
			mLocationManager.requestLocationUpdates(mProviderString, mMinTime, mMinDistance, this);
			mIsListening = true;
		}
		
		public void stopListening() {
			if (!mIsListening) return;
			mLocationManager.removeUpdates(this);
			mIsListening = false;
		}
		
		public boolean isEnabled() {
			return mIsEnabled;
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
		}

		@Override
		public void onLocationChanged(android.location.Location location) {
			mLocation = location;
			if (mFineCallback != null) mFineCallback.onLocationChanged(this);
			else if (mCoarseCallback != null) mCoarseCallback.onLocationChanged(this);
		}

		@Override
		public void onProviderDisabled(String provider) {
			mIsEnabled = false;
			if (mFineCallback != null) mFineCallback.onProviderDisabled(this);
			else if (mCoarseCallback != null) mCoarseCallback.onProviderDisabled(this);
		}

		@Override
		public void onProviderEnabled(String provider) {
			mIsEnabled = true;
			if (mFineCallback != null) mFineCallback.onProviderEnabled(this);
			else if (mCoarseCallback != null) mCoarseCallback.onProviderEnabled(this);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			mStatus = status;
			mExtras = extras;
			if (mFineCallback != null) mFineCallback.onStatusChanged(this);
			else if (mCoarseCallback != null) mCoarseCallback.onStatusChanged(this);
		}
	}
	
	
	public class FineProviderWrapper extends ProviderWrapper 
			implements GpsStatus.Listener, GpsStatus.NmeaListener {
		
		private GpsStatus mGpsStatus;
		
		private int mEvent;
		private long mTimestamp;
		private String mNmea;
		
		public FineProviderWrapper(Context context, String provider) {
			super(context, provider);
			updateGpsStatus();
		}
		
		@Override
		public FineCallback getCallback() {
			return mFineCallback;
		}
		
		/**
		 * <b>DO NOT USE!!!</b><br>
		 * This is overridden to remove functionality.
		 * Use {@code setCallback(FineCallback callback)} instead.
		 */
		@Override 
		public void setCallback(CoarseCallback callback) {
			// Empty
		}

		public void setCallback(FineCallback callback) {
			mFineCallback = callback;
		}
		
		@Override
		public void startListening(boolean onlyIfCallbackSet) {
			if (mIsListening || (onlyIfCallbackSet && mFineCallback == null)) return;			
			mLocationManager.addGpsStatusListener(this);
			mLocationManager.addNmeaListener(this);
			super.startListening(onlyIfCallbackSet);
		}
		
		@Override
		public void stopListening() {
			if (!mIsListening) return;			
			mLocationManager.removeGpsStatusListener(this);
			mLocationManager.removeNmeaListener(this);
			super.stopListening();
		}
		
		public GpsStatus getGpsStatus() {
			return mGpsStatus;
		}
		
		public int getLastEvent() {
			return mEvent;
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
		public void onGpsStatusChanged(int event) {
			mEvent = event;
			updateGpsStatus();
			if (mFineCallback != null) mFineCallback.onGpsStatusChanged(this);			
		}

		@Override
		public void onNmeaReceived(long timestamp, String nmea) {
			mTimestamp = timestamp;
			mNmea = nmea;
			updateGpsStatus();
			if (mFineCallback != null) mFineCallback.onNmeaReceived(this);
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
		
		List<String> providers = getAllProviders();
		if (providers != null) {
			int i = 0;
			for (String s : providers) {
				contents.put("Provider " + i, s);
				++i;
			}
		}
		return contents;
	}

}
