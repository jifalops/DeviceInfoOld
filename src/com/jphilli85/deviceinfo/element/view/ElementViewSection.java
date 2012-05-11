package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class ElementViewSection {
	private Context mContext;
	
	public ElementViewSection(Context context) {
		mContext = context;
	}
	
	public final View inflate(int resource) {
		return View.inflate(mContext, resource, null);
	}
	
	public abstract void addToLayout(ViewGroup layout);
}
