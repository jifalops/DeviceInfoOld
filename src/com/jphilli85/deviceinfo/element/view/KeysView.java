package com.jphilli85.deviceinfo.element.view;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Keys;
import com.jphilli85.deviceinfo.element.UnavailableFeatureException;

import android.content.Context;


public class KeysView extends ElementView {
	private Keys mKeys;
	
	public KeysView(Context context) {
		super(context);
	}

	@Override
	public Element getElement() {
		return mKeys; //TODO can be null (disable save)
	}

	@Override
	protected void initialize(Context context) {
		try { mKeys = new Keys(context); } 
		catch (UnavailableFeatureException e) {}	
		
		ListSection list = new ListSection(getContext());
		
		
		if (mKeys != null) {
			Section section = new Section(getContext(), "Available");
			for (int i : mKeys.getAvailableKeys()) {
				list.add(mKeys.getKeyName(i));
			}
			section.add(list);
			add(section);
			
			list = new ListSection(getContext());
			section = new Section(getContext(), "Unavailable");		
			for (int i : mKeys.getUnavailableKeys()) {
				list.add(mKeys.getKeyName(i));
			}
			section.add(list);
			add(section);			
		}
		else { 
			list.add("You device does not support this");
			add(list);
		}	
	}
}
