package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

import com.jphilli85.deviceinfo.DeviceInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.ImageView;

public class Battery implements ContentsMapper {
	
	public interface Callback {
		void onReceive(Context context, Intent intent);
	}
	
	// TODO caching context
	private final Context mAppContext;
	private final Callback mCallback;
	private final BatteryChangedBroadcastReceiver mBatteryReceiver;
	private final IntentFilter mIntentFilter;
	private boolean mIsListening;
	
	private long mTimestamp;
	
	private int mLevel;
	private int mMaxLevel;
	private int mVoltage; 	// mV
	private float mTemp; 		// C
	private String mTechnology;
	private int mStatus;
	private int mHealth;
	private int mPluggedStatus;
	private int mIconResourceId;
	private boolean mPresent; // battery exists?	
	
	private ImageView mIconImageView;
	
	
	public Battery(Context context, Callback callback) {
		mAppContext = context.getApplicationContext();
		mCallback = callback;
		mBatteryReceiver = new BatteryChangedBroadcastReceiver();
		mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		mIsListening = false;
	}
	
	public void startListening() {
		if (mIsListening) return;
		mAppContext.registerReceiver(mBatteryReceiver, mIntentFilter);
		mIsListening = true;
	}
	
	public void stopListening() {
		if (!mIsListening) return;
		mAppContext.unregisterReceiver(mBatteryReceiver);
		mIsListening = false;
	}
	
	public Context getContext() {
		return mAppContext;
	}
	
	public Callback getCallback() {
		return mCallback;
	}
	
	public BroadcastReceiver getReceiver() {
		return mBatteryReceiver;
	}
	
	public IntentFilter getIntentFilter() {
		return mIntentFilter;
	}
	
	public boolean isListening() {
		return mIsListening;
	}
	
	private class BatteryChangedBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) return;	
			mTimestamp = DeviceInfo.getTimestamp();
			mLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			mMaxLevel = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
			mVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
			mTemp = (float) (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10);
			mTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
			mStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
			mHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
			mPluggedStatus = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
			mIconResourceId = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
			mPresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true);
			
			mIconImageView = new ImageView(context);
			mIconImageView.setImageResource(mIconResourceId);
			
			if (mCallback != null) mCallback.onReceive(context, intent);
	  	}	
	}
	
	public long getTimestamp() {
		return mTimestamp;
	}
	
	public int getLevel() {
		return mLevel;
	}
	
	public int getLevelMax() {
		return mMaxLevel;
	} 
	
	/** Get the reported voltage in milli volts */
	public int getVoltage() {
		return mVoltage;
	}
	
	/** Get the reported temperature in celsius */
	public float getTemperature() {
		return mTemp;
	}
	
	public String getTechnology() {
		return mTechnology;
	}
	
	public int getStatus() {
		return mStatus;
	}
	
	public int getHealth() {
		return mHealth;
	}
	
	public int getPluggedInStatus() {
		return mPluggedStatus;
	}
	
	public int getIconResourceId() {
		return mIconResourceId;
	}
	
	public boolean isBatteryPresent() {
		return mPresent;
	}
	
	public ImageView getIconImageView() {
		return mIconImageView;
	}
	
	// TODO ui facing strings
	public String getHealthString() {
		if (Build.VERSION.SDK_INT >= 11 && mHealth == BatteryManager.BATTERY_HEALTH_COLD) return "BATTERY_HEALTH_COLD";
		else if (mHealth == BatteryManager.BATTERY_HEALTH_DEAD) return "BATTERY_HEALTH_DEAD";
		else if (mHealth == BatteryManager.BATTERY_HEALTH_GOOD) return "BATTERY_HEALTH_GOOD";
		else if (mHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) return "BATTERY_HEALTH_OVER_VOLTAGE";
		else if (mHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT) return "BATTERY_HEALTH_OVERHEAT";
		else if (mHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN) return "BATTERY_HEALTH_UNKNOWN";
		else if (mHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) return "BATTERY_HEALTH_UNSPECIFIED_FAILURE";
		else return null;
	}
	
	// TODO ui facing strings
	public String getPluggedInStatusString() {
		if (mPluggedStatus == BatteryManager.BATTERY_PLUGGED_AC) return "BATTERY_PLUGGED_AC";
		else if (mPluggedStatus == BatteryManager.BATTERY_PLUGGED_USB) return "BATTERY_PLUGGED_USB";			
		else return null;
	}
	
	// TODO ui facing strings
	public String getStatusString() {
		if (mStatus == BatteryManager.BATTERY_STATUS_CHARGING) return "BATTERY_STATUS_CHARGING";
		else if (mStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) return "BATTERY_STATUS_DISCHARGING";			
		else if (mStatus == BatteryManager.BATTERY_STATUS_FULL) return "BATTERY_STATUS_FULL";
		else if (mStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING) return "BATTERY_STATUS_NOT_CHARGING";
		else if (mStatus == BatteryManager.BATTERY_STATUS_UNKNOWN) return "BATTERY_STATUS_UNKNOWN";
		else return null;
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		contents.put("Listening for Changes", String.valueOf(isListening()));
		contents.put("Event Timestamp", String.valueOf(getTimestamp()));
		contents.put("Battery Is Present", String.valueOf(isBatteryPresent()));
		contents.put("Level", String.valueOf(getLevel()));
		contents.put("Level Max", String.valueOf(getLevelMax()));
		contents.put("Voltage (mV)", String.valueOf(getVoltage()));
		contents.put("Temperature (C)", String.valueOf(getTemperature()));
		contents.put("Technology", getTechnology());
		contents.put("Status", getStatusString());
		contents.put("Health", getHealthString());
		contents.put("Plugged In Status", getPluggedInStatusString());
		contents.put("Icon Resource ID", String.valueOf(getIconResourceId()));
		
		return contents;
	}

}
