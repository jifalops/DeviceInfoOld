package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.os.Build;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Cellular;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Graphics;
import com.jphilli85.deviceinfo.element.Platform;


public class PlatformView extends ElementView {
	private Platform mPlatform;
	
	public PlatformView() {
		this(DeviceInfo.getContext());
	}
	
	protected PlatformView(Context context) {
		super(context);
	}

	@Override
	public Element getElement() {		
		return mPlatform;
	}

	@Override
	protected void initialize(Context context) {
		mPlatform = new Platform(context);
		
		TableSection table = new TableSection();
		table.add("Manufacturer", Build.MANUFACTURER);
		table.add("Model", Build.MODEL);
		table.add("Device", Build.DEVICE);
		table.add("Brand", Build.BRAND);
		table.add("Android Name", mPlatform.getVersionName(API));
		table.add("Android Version", Build.VERSION.RELEASE);
		table.add("Android API Level", String.valueOf(API));
		table.add("Build Display", Build.DISPLAY);
		table.add("Build ID", Build.ID);
		table.add("Build Incremental Version", Build.VERSION.INCREMENTAL);
		table.add("Build Fingerprint", Build.FINGERPRINT);
		table.add("OpenGL Version", Graphics.openGlesVersion(DeviceInfo.getContext()));
		table.add("Kernel", Platform.getKernelVersion());
		table.add("Radio Version", Cellular.getRadioVersion());
		table.add("Radio Interface Version", Cellular.getRilVersion());
		table.add("Baseband", Cellular.getBaseband());
		if (API >= 8) {
			table.add("Bootloader", Build.BOOTLOADER);
			table.add("Hardware", Build.HARDWARE);
		}
		
		table.add("Board", Build.BOARD);
		add(table);
	}
}
