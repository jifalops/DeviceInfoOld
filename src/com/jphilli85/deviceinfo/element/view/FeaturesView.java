package com.jphilli85.deviceinfo.element.view;

import android.content.Context;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Features;


public class FeaturesView extends ElementView {
	private Features mFeatures;
	
	public FeaturesView() {
		this(DeviceInfo.getContext());
	}
	
	protected FeaturesView(Context context) {
		super(context);
		
		
		
	}

	@Override
	public Element getElement() {
		return mFeatures;
	}

	@Override
	protected void initialize(Context context) {
		mFeatures = new Features(context);
	}

	@Override
	protected void onInitialized() {
		ListSection list = new ListSection();
		
		Section section = new Section("Available");		
		for (String s : mFeatures.getAvailableFeatures()) {
			list.add(s);
		}
		section.add(list);
		add(section);
		
		list = new ListSection();
		section = new Section("Unavailable");
		for (String s : mFeatures.getUnavailableFeatures()) {
			list.add(s);
		}
		section.add(list);
		add(section);
	}

}
