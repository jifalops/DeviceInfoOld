package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jphilli85.deviceinfo.app.DeviceInfo;

public abstract class AbstractSection {
	private final Context mContext;
	
	public AbstractSection(Context context) {
		mContext = context;
	}
	
	public final Context getContext() {
		return mContext;
	}
	
	public final View inflate(int resource) {
		return View.inflate(mContext, resource, null);
	}
	
	public abstract void addToLayout(ViewGroup layout);
}
