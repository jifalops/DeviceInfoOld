package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

public class Bluetooth implements ContentsMapper {

	private final BluetoothAdapter mBluetoothAdapter;
	
	public Bluetooth(Context context) throws UnavailableFeatureException {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) throw new UnavailableFeatureException();
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		
		
		return contents;
	}

}
