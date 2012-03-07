package com.jphilli85.deviceinfo2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class DeviceInfoActivity extends FragmentActivity {
	public static Context sAppContext;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if (sAppContext == null) sAppContext = getApplicationContext(); 
	}
}
