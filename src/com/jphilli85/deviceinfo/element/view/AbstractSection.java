package com.jphilli85.deviceinfo.element.view;

import android.view.View;
import android.view.ViewGroup;

import com.jphilli85.deviceinfo.app.DeviceInfo;

public abstract class AbstractSection {	
	public final View inflate(int resource) {
		return View.inflate(DeviceInfo.getContext(), resource, null);
	}
	
	public abstract void addToLayout(ViewGroup layout);
}
