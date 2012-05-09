package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.ShellHelper;

public class Platform extends Element {
	private static final int API = Build.VERSION.SDK_INT;
	public final String ECLAIR;
	public final String FROYO;
	public final String GINGERBREAD;
	public final String HONEYCOMB;
	public final String ICE_CREAM_SANDWICH;
	
	private final TelephonyManager mTelephonyManager;
	
	public Platform(Context context) {
		
		ECLAIR = context.getString(R.string.platform_eclair);
		FROYO = context.getString(R.string.platform_froyo);
		GINGERBREAD = context.getString(R.string.platform_gingerbread);
		HONEYCOMB = context.getString(R.string.platform_honeycomb);
		ICE_CREAM_SANDWICH = context.getString(R.string.platform_ice_cream_sandwich);
		
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public String getVersionName(int version) {
		switch (version) {
		case 7: return ECLAIR;
		case 8: return FROYO;
		case 9: // g
		case 10: return GINGERBREAD;
		case 11: // h
		case 12: // h
		case 13: return HONEYCOMB;
		case 14: // i
		case 15: return ICE_CREAM_SANDWICH;
		default: return null;
		}
	}
	
	public static String getKernelVersion() {
		List<String> list = ShellHelper.getProc("version");
		if (list == null || list.isEmpty()) return null;
		String[] parts = list.get(0).split("\\s+");		
		if (parts.length >= 4) 	return parts[2] + " " + parts[3];
		else if (parts.length == 3) return parts[2];
		return null;	
	}
	
	
	
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();

		contents.put("Manufacturer", Build.MANUFACTURER);
		contents.put("Model", Build.MODEL);
		contents.put("Device", Build.DEVICE);
		contents.put("Brand", Build.BRAND);
		contents.put("Android Name", getVersionName(API));
		contents.put("Android Version", Build.VERSION.RELEASE);
		contents.put("Android API Level", String.valueOf(API));
		contents.put("Build Display", Build.DISPLAY);
		contents.put("Build ID", Build.ID);
		contents.put("Build Incremental Version", Build.VERSION.INCREMENTAL);
		contents.put("Build Fingerprint", Build.FINGERPRINT);
		contents.put("OpenGL Version", Graphics.openGlesVersion(DeviceInfo.getAppContext()));
		contents.put("Kernel", getKernelVersion());
		contents.put("Radio Version", Cellular.getRadioVersion());
		contents.put("Radio Interface Version", Cellular.getRilVersion());
		contents.put("Baseband", Cellular.getBaseband());
		contents.put("Bootloader", Build.BOOTLOADER);
		contents.put("Hardware", Build.HARDWARE);
		contents.put("Board", Build.BOARD);
		
		return contents;
	}

}
