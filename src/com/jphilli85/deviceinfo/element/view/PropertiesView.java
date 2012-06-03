package com.jphilli85.deviceinfo.element.view;

import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.jphilli85.deviceinfo.ShellHelper;
import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Properties;


public class PropertiesView extends ElementView {
	private Properties mProperties;
	
	public PropertiesView() {
		this(DeviceInfo.getContext());
	}
	protected PropertiesView(Context context) {
		super(context);
	}

	@Override
	public Element getElement() {
		return mProperties;
	}
	
	@Override
	protected void initialize(Context context) {
		mProperties = new Properties();
		TableSection table = new TableSection();
		Map<String, String> properties = ShellHelper.getProp();
		for (Entry<String, String> e : properties.entrySet()) {
			table.add(e.getKey(), e.getValue());
		}
		add(table);
	}
}
