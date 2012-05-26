package com.jphilli85.deviceinfo.element.view;

import android.content.Context;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Network;


public class NetworkView extends ElementView {
	private final Network mNetwork;
	
	public NetworkView() {
		this(DeviceInfo.getContext());
	}
	
	protected NetworkView(Context context) {
		super(context);
		mNetwork = new Network(context);
		showElementContents();
	}

	@Override
	public Element getElement() {
		return mNetwork;
	}

}
