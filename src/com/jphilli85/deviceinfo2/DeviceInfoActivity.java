package com.jphilli85.deviceinfo2;

import com.jphilli85.deviceinfo.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DeviceInfoActivity extends FragmentActivity {
	public static Context sAppContext;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if (sAppContext == null) sAppContext = getApplicationContext(); 
		setContentView(R.layout.main);
		
	}
}
