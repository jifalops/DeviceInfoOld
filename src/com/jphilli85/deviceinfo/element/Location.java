package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;
import java.util.List;


import android.content.Context;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Location implements ContentsMapper, 
		LocationListener, GpsStatus.Listener, GpsStatus.NmeaListener {
	
	public interface Callback {
		/** Corresponds to LocationListener.onLocationChanged() */
		void onLocationChanged(android.location.Location location);
		/** Corresponds to LocationListener.onProviderDisabled() */
		void onProviderDisabled(String provider);
		/** Corresponds to LocationListener.onProviderEnabled() */
		void onProviderEnabled(String provider);
		/** Corresponds to LocationListener.onStatusChanged() */
		void onStatusChanged(String provider, int status, Bundle extras);
		/** Corresponds to GpsStatus.Listener.onGpsStatusChanged() */
		void onGpsStatusChanged(int event);
		/** Corresponds to GpsStatus.NmeaListener.onNmeaReceived() */		
		void onNmeaReceived(long timestamp, String nmea);			
	}
	
	private final LocationManager mLocationManager;
	private final Callback mCallback;
	
	public Location(Context context, Callback callback) {
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mCallback = callback;
	}
	
	
	
	public LocationManager getLocationManager() {
		return mLocationManager;
	}
	
	public void startListening() {
		mLocationManager.addGpsStatusListener(this);
		mLocationManager.addNmeaListener(this);
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
	}
	
	public void stopListening() {
		mLocationManager.removeGpsStatusListener(this);
		mLocationManager.removeNmeaListener(this);
		mLocationManager.removeUpdates(this);
	}
	
	public List<String> getAllProviders() {
		return mLocationManager.getAllProviders();
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		
		if (mCallback != null) mCallback.onLocationChanged(location);
	}

	@Override
	public void onProviderDisabled(String provider) {

		if (mCallback != null) mCallback.onProviderDisabled(provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		
		if (mCallback != null) mCallback.onProviderEnabled(provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
		if (mCallback != null) mCallback.onStatusChanged(provider, status, extras);
	}

	@Override
	public void onGpsStatusChanged(int event) {
		
		if (mCallback != null) mCallback.onGpsStatusChanged(event);
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		
		if (mCallback != null) mCallback.onNmeaReceived(timestamp, nmea);
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
