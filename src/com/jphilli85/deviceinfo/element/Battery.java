package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.ImageView;

import com.jphilli85.deviceinfo.R;

public class Battery extends ListeningElement {
	
	public interface Callback extends ListeningElement.Callback {
		void onReceive(Context context, Intent intent);
	}
	
	// BatteryManager constants
	public final String HEALTH_COLD;
	public final String HEALTH_DEAD;
	public final String HEALTH_GOOD;
	public final String HEALTH_OVERHEAT;
	public final String HEALTH_OVER_VOLTAGE;
	public final String HEALTH_UNKNOWN;
	public final String HEALTH_UNSPECIFIED_FAILURE;
	public final String PLUGGED_AC;
	public final String PLUGGED_USB;
	public final String STATUS_CHARGING;
	public final String STATUS_DISCHARGING;
	public final String STATUS_FULL;
	public final String STATUS_NOT_CHARGING;
	public final String STATUS_UNKNOWN;
	
	private final Context mAppContext;
	private final BatteryChangedBroadcastReceiver mBatteryReceiver;
	private final IntentFilter mIntentFilter;
	
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
	private boolean mPresent; // battery exists
	
	private ImageView mIconImageView;
	
	
	public Battery(Context context) {
		HEALTH_COLD = context.getString(R.string.battery_health_cold);
		HEALTH_DEAD = context.getString(R.string.battery_health_dead);
		HEALTH_GOOD = context.getString(R.string.battery_health_good);
		HEALTH_OVERHEAT = context.getString(R.string.battery_health_overheat);
		HEALTH_OVER_VOLTAGE = context.getString(R.string.battery_health_over_voltage);
		HEALTH_UNKNOWN = context.getString(R.string.battery_health_unknown);
		HEALTH_UNSPECIFIED_FAILURE = context.getString(R.string.battery_health_unspecified_failure);
		PLUGGED_AC = context.getString(R.string.battery_plugged_ac);
		PLUGGED_USB = context.getString(R.string.battery_plugged_usb);
		STATUS_CHARGING = context.getString(R.string.battery_status_charging);
		STATUS_DISCHARGING = context.getString(R.string.battery_status_discharging);
		STATUS_FULL = context.getString(R.string.battery_status_full);
		STATUS_NOT_CHARGING = context.getString(R.string.battery_status_not_charging);
		STATUS_UNKNOWN = context.getString(R.string.battery_status_unknown);
		
		mAppContext = context.getApplicationContext();
		mBatteryReceiver = new BatteryChangedBroadcastReceiver();
		mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	}
	
	public String getHealth(int health) {
		switch (health) {
		case BatteryManager.BATTERY_HEALTH_DEAD: return HEALTH_DEAD;
		case BatteryManager.BATTERY_HEALTH_GOOD: return HEALTH_GOOD;
		case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE: return HEALTH_OVER_VOLTAGE;
		case BatteryManager.BATTERY_HEALTH_OVERHEAT: return HEALTH_OVERHEAT;
		case BatteryManager.BATTERY_HEALTH_UNKNOWN: return HEALTH_UNKNOWN;
		case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE: return HEALTH_UNSPECIFIED_FAILURE;
		}
		if (Build.VERSION.SDK_INT >= 11 && mHealth == BatteryManager.BATTERY_HEALTH_COLD) return HEALTH_COLD;		
		return null;
	}
	
	public String getPluggedInStatus(int status) {
		switch (status) {
		case BatteryManager.BATTERY_PLUGGED_AC: return PLUGGED_AC;
		case BatteryManager.BATTERY_PLUGGED_USB: return PLUGGED_USB;
		default: return null;
		}
	}
	
	public String getStatus(int status) {
		switch (status) {
		case BatteryManager.BATTERY_STATUS_CHARGING: return STATUS_CHARGING;
		case BatteryManager.BATTERY_STATUS_DISCHARGING: return STATUS_DISCHARGING;
		case BatteryManager.BATTERY_STATUS_NOT_CHARGING: return STATUS_NOT_CHARGING;
		case BatteryManager.BATTERY_STATUS_FULL: return STATUS_FULL;
		case BatteryManager.BATTERY_STATUS_UNKNOWN: return STATUS_UNKNOWN;
		default: return null;
		}		
	}
	
	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		if (!super.startListening(onlyIfCallbackSet)) return false;
		mAppContext.registerReceiver(mBatteryReceiver, mIntentFilter);
		return setListening(true);		
	}
	
	@Override
	public boolean stopListening() {
		if (!super.stopListening()) return false;
		mAppContext.unregisterReceiver(mBatteryReceiver);
		return !setListening(false);
	}
	
	public Context getContext() {
		return mAppContext;
	}
	
	public BroadcastReceiver getReceiver() {
		return mBatteryReceiver;
	}
	
	public IntentFilter getIntentFilter() {
		return mIntentFilter;
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
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = super.getContents();
		
		contents.put("Event Timestamp", String.valueOf(getTimestamp()));
		contents.put("Battery Is Present", String.valueOf(isBatteryPresent()));
		contents.put("Level", String.valueOf(getLevel()));
		contents.put("Level Max", String.valueOf(getLevelMax()));
		contents.put("Voltage (mV)", String.valueOf(getVoltage()));
		contents.put("Temperature (C)", String.valueOf(getTemperature()));
		contents.put("Technology", getTechnology());
		contents.put("Status", getStatus(mStatus));
		contents.put("Health", getHealth(mHealth));
		contents.put("Plugged In Status", getPluggedInStatus(mPluggedStatus));
		contents.put("Icon Resource ID", String.valueOf(getIconResourceId()));
		
		return contents;
	}

	private class BatteryChangedBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) return;	
			mTimestamp = System.currentTimeMillis();
			mLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			mMaxLevel = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
			mVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
			mTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f;
			mTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
			mStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
			mHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
			mPluggedStatus = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
			mIconResourceId = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
			mPresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true);
			
			mIconImageView = new ImageView(context);
			mIconImageView.setImageResource(mIconResourceId);
			
			if (mCallback != null) ((Callback) mCallback).onReceive(context, intent);
		}
	}
}
