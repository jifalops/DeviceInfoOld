package com.jphilli85.deviceinfo.element;

import android.content.Context;

import com.jphilli85.deviceinfo.app.DeviceInfo;


public abstract class Element implements ContentsMapper {
	protected static final String LOG_TAG = Element.class.getSimpleName();
	// TODO Fix UI facing strings in getContents();
	
	public Element() {
		this(DeviceInfo.getContext());
	}
	
	public Element(Context context) {
		
	}
}
