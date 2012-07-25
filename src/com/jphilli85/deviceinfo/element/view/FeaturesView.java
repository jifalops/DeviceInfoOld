package com.jphilli85.deviceinfo.element.view;

import android.content.Context;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Features;


public class FeaturesView extends ElementView {
	private Features mFeatures;
	
	
	public FeaturesView(Context context) {
		super(context);
	}

	@Override
	public Element getElement() {
		return mFeatures;
	}

	@Override
	protected void initialize(Context context) {
		mFeatures = new Features(context);
		
		ListSection list = new ListSection(getContext());
		
		Section section = new Section(getContext(), "Available");		
		for (String s : mFeatures.getAvailableFeatures()) {
			list.add(s);
		}
		section.add(list);
		add(section);
		
		list = new ListSection(getContext());
		section = new Section(getContext(), "Unavailable");
		for (String s : mFeatures.getUnavailableFeatures()) {
			list.add(s);
		}
		section.add(list);
		add(section);
	}
}
