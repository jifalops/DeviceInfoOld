package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

import android.content.Context;
import android.net.wifi.WifiManager;

public class Wifi implements ContentsMapper, SmartListener {
	
	private WifiManager mWifiManager;
	
	public Wifi(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	@Override
	public void startListening() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startListening(boolean onlyIfCallbackSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopListening() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isListening() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinkedHashMap<String, String> getContents() {
		// TODO Auto-generated method stub
		return null;
	}

}
