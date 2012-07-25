package com.jphilli85.deviceinfo.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class Features extends Element {
	private static final int API = Build.VERSION.SDK_INT;
	
	private final PackageManager mPackageManager;
	private final List<String> mAvailable;
	private final List<String> mUnavailable;
	
	public Features(Context context) {
		super(context);
		mPackageManager = context.getPackageManager();
		mAvailable = new ArrayList<String>();
		mUnavailable = new ArrayList<String>();
		checkFeatures();
	}
	
	private void checkFeatures() {
		check(PackageManager.FEATURE_CAMERA);
		check(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
		check(PackageManager.FEATURE_CAMERA_FLASH);
		check(PackageManager.FEATURE_LIVE_WALLPAPER);		
		check(PackageManager.FEATURE_SENSOR_LIGHT);
		check(PackageManager.FEATURE_SENSOR_PROXIMITY);		
		check(PackageManager.FEATURE_TELEPHONY);
		check(PackageManager.FEATURE_TELEPHONY_CDMA);
		check(PackageManager.FEATURE_TELEPHONY_GSM);	
		check(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);

		if (API >= 8) {
			check(PackageManager.FEATURE_BLUETOOTH);
			check(PackageManager.FEATURE_LOCATION);
			check(PackageManager.FEATURE_LOCATION_GPS);
			check(PackageManager.FEATURE_LOCATION_NETWORK);
			check(PackageManager.FEATURE_MICROPHONE);
			check(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
			check(PackageManager.FEATURE_SENSOR_COMPASS);
			check(PackageManager.FEATURE_TOUCHSCREEN);
			check(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT);
			check(PackageManager.FEATURE_WIFI);
		}
		if (API >= 9) {
			check(PackageManager.FEATURE_AUDIO_LOW_LATENCY);
			check(PackageManager.FEATURE_CAMERA_FRONT);
			check(PackageManager.FEATURE_NFC);
			check(PackageManager.FEATURE_SENSOR_BAROMETER);			
			check(PackageManager.FEATURE_SENSOR_GYROSCOPE);
			check(PackageManager.FEATURE_SIP);
			check(PackageManager.FEATURE_SIP_VOIP);
			check(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND);
		}
		if (API >= 11) {
			check(PackageManager.FEATURE_FAKETOUCH);
		}
		if (API >= 12) {
			check(PackageManager.FEATURE_USB_ACCESSORY);
			check(PackageManager.FEATURE_USB_HOST);
		}
		if (API >= 13) {
			check(PackageManager.FEATURE_FAKETOUCH_MULTITOUCH_DISTINCT);
			check(PackageManager.FEATURE_FAKETOUCH_MULTITOUCH_JAZZHAND);
			check(PackageManager.FEATURE_SCREEN_LANDSCAPE);
			check(PackageManager.FEATURE_SCREEN_PORTRAIT);
		}
		if (API >= 14) {
			check(PackageManager.FEATURE_WIFI_DIRECT);
		}
	}
	
	public List<String> getAvailableFeatures() {
		return mAvailable;
	}
	
	public List<String> getUnavailableFeatures() {
		return mUnavailable;
	}
	
	private void check(String feature) {
		if (mPackageManager.hasSystemFeature(feature)) mAvailable.add(feature);
		else mUnavailable.add(feature);
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		for (int i = 0; i < mAvailable.size(); ++i) {
			contents.put("Available Feature " + i, mAvailable.get(i));
		}
		
		for (int i = 0; i < mUnavailable.size(); ++i) {
			contents.put("Unavailable Feature " + i, mUnavailable.get(i));
		}
		
		return contents;
	}

}
