package com.jphilli85.deviceinfo.element;

import android.content.Context;

import com.jphilli85.deviceinfo.app.DeviceInfo;


public abstract class Element implements ContentsMapper {
//	private static final String LOG_TAG = Element.class.getSimpleName();

	private final Context mContext;
	
	public Element(Context context) {
		mContext = context;
	}
	
	public final Context getContext() {
		return mContext;
	}
}
