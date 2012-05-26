package com.jphilli85.deviceinfo.element.view;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Keys;
import com.jphilli85.deviceinfo.element.UnavailableFeatureException;

import android.content.Context;


public class KeysView extends ElementView {
	private Keys mKeys;
	
	public KeysView() {
		this(DeviceInfo.getContext());
	}
	
	protected KeysView(Context context) {
		super(context);
		
		ListSection list = new ListSection();
		
		try { mKeys = new Keys(context); } 
		catch (UnavailableFeatureException e) {}		
		
		if (mKeys != null) {
			Section section = new Section("Available");
			for (int i : mKeys.getAvailableKeys()) {
				list.add(mKeys.getKeyName(i));
			}
			section.add(list);
			add(section);
			
			list = new ListSection();
			section = new Section("Unavailable");		
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

	@Override
	public Element getElement() {
		return mKeys; //TODO can be null (disable save)
	}

}
