package com.jphilli85.deviceinfo.element.view;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Identifiers;

import android.content.Context;


public class IdentifiersView extends ElementView {
	private Identifiers mIdentifiers;
	
	public IdentifiersView() {
		this(DeviceInfo.getContext());
	}
	
	protected IdentifiersView(Context context) {
		super(context);
		
		
		
	}

	@Override
	public Element getElement() {
		return mIdentifiers;
	}

	@Override
	protected void initialize(Context context) {
		mIdentifiers = new Identifiers(context);
	}

	@Override
	protected void onInitialized() {
		TableSection table = new TableSection();

		table.add("Device ID", mIdentifiers.DEVICE_ID);
		table.add("Device Serial", mIdentifiers.DEVICE_SERIAL);
		table.add("Android ID", mIdentifiers.ANDOID_ID);	
		table.add("Phone ID", mIdentifiers.PHONE_ID);
		table.add("SIM Serial", mIdentifiers.SIM_SERIAL);
		table.add("Line 1 Number", mIdentifiers.LINE_1_NUMBER);
		table.add("Subscriber ID", mIdentifiers.SUBSCRIBER_ID);
		
		add(table);
		
		//TODO more
		//wifi, bt, others?
	}

}
