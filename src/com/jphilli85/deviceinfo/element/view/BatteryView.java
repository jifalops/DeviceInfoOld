package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Battery;
import com.jphilli85.deviceinfo.element.Element;


public class BatteryView extends ListeningElementView implements Battery.Callback {
	private Battery mBattery;
	
	private final TextView mTimestamp;	
	private final TextView mLevel;
	private final TextView mVoltage; 	// mV
	private final TextView mTemp; 		// C
	private final TextView mTechnology;
	private final TextView mStatus;
	private final TextView mHealth;
	private final TextView mPluggedStatus;	
	private final TextView mPresent; // battery exists
	private final ImageView mIcon;
	
	private final TimeView mTimeView;
	
	public BatteryView() {
		this(DeviceInfo.getContext());
	}
	
	public BatteryView(Context context) {
		super(context);
		
		TableSection table = new TableSection();
		
		mTimestamp = table.getValueTextView();
		mLevel = table.getValueTextView();
		mVoltage = table.getValueTextView();
		mTemp = table.getValueTextView();
		mTechnology = table.getValueTextView();
		mStatus = table.getValueTextView();
		mHealth = table.getValueTextView();
		mPluggedStatus = table.getValueTextView();
		mPresent = table.getValueTextView();
		mIcon = new ImageView(context);
		
		mTimeView = new TimeView(mTimestamp);			
	}

	@Override
	public Element getElement() {
		return mBattery;
	}

	@Override
	public void onReceive(Context context, Intent intent) {		
		mTimeView.setSuffix(" (previous " 
			+ DeviceInfo.getDuration((int) (mTimeView.getValue() / 1000 + 0.5f)) + ")");
		mTimeView.setOffset(mBattery.getTimestamp());
		mLevel.setText((int) DeviceInfo.getPercent(mBattery.getLevel(), mBattery.getLevelMax()) + "%");
		mTemp.setText(mBattery.getTemperature() + "°C");
		mStatus.setText(mBattery.getStatusString());
		mPluggedStatus.setText(mBattery.getPluggedInStatusString());
		mVoltage.setText(mBattery.getVoltage() + "mV");
		mTechnology.setText(mBattery.getTechnology());
		mHealth.setText(mBattery.getHealthString());
		mPresent.setText(String.valueOf(mBattery.isBatteryPresent()));
		mIcon.setImageResource(mBattery.getIconResourceId());
	}

	@Override
	public void onPlay(PlayableSection section) {
		super.onPlay(section);
		mTimeView.start();		
	}

	@Override
	public void onPause(PlayableSection section) {
		super.onPause(section);
		mTimeView.stop();
	}

	@Override
	protected void initialize(Context context) {
		mBattery = new Battery(context);
		mBattery.setCallback(this);	
	}

	@Override
	protected void onInitialized() {
		TableSection table = new TableSection();
		
		table.add("Level", mLevel);
		// TODO celcius and farenheit
		table.add("Temperature", mTemp);
		table.add("Status",  mStatus);
		table.add("Plugged Status", mPluggedStatus);
		table.add("Voltage", mVoltage);
		table.add("Technology", mTechnology);
		table.add("Health", mHealth);
		table.add("Present", mPresent);
		table.add("System icon", mIcon);
		table.add("Since Last Change", mTimestamp);
		
	
		add(table);
		
		// initialize
		onReceive(null, null);
		// simulate a play click
		mHeader.play();
	}
}
