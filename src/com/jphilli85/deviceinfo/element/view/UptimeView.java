package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.widget.TextView;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Uptime;


public class UptimeView extends ElementView implements Uptime.Callback {
	private Uptime mUptime;
	
	private final TextView mTotal, mSleep, mAwake;
	
	public UptimeView() {
		this(DeviceInfo.getContext());
	}
	
	protected UptimeView(Context context) {
		super(context);
		
		TableSection table = new TableSection();
		mTotal = table.getValueTextView();
		mSleep = table.getValueTextView();
		mAwake = table.getValueTextView();
		
		table.add("Total", mTotal);
		table.add("Asleep", mSleep);
		table.add("Awake", mAwake);
		add(table);
		
		mUptime = new Uptime();
		mUptime.setCallback(this);
		mUptime.startListening();
	}

	@Override
	public Element getElement() {
		return mUptime;
	}

	@Override
	protected void initialize(Context context) {		
		
	}

	@Override
	public void onUptimeUpdated(float uptimeTotal, float uptimeAsleep) {
		mTotal.setText(DeviceInfo.getDuration((long) uptimeTotal));
		mSleep.setText(DeviceInfo.getDuration((long) uptimeAsleep));
		mAwake.setText(DeviceInfo.getDuration((long) (uptimeTotal - uptimeAsleep)));
	}
}
