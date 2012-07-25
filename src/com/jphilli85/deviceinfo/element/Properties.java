package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

import android.content.Context;

import com.jphilli85.deviceinfo.ShellHelper;

public class Properties extends Element {

	public Properties(Context context) {
		super(context);
	}

	@Override
	public LinkedHashMap<String, String> getContents() {
//		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		return ShellHelper.getProp();
//		return contents;
	}
 
}
