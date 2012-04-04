package com.jphilli85.deviceinfo.unit;

import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.ImageView;

public class Battery extends Unit {
	
	// TODO caching context
	private final Context mAppContext;
	private final BatteryChangedBroadcastReceiver mBatteryReceiver;
	private final IntentFilter mIntentFilter;
	private boolean mIsReceiving;
	
	
	public Battery(Context context) {
		mAppContext = context.getApplicationContext();
		mBatteryReceiver = new BatteryChangedBroadcastReceiver();
		mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		mIsReceiving = false;
	}
	
	public void startReceiving() {
		mAppContext.registerReceiver(mBatteryReceiver, mIntentFilter);
		mIsReceiving = true;
	}
	
	public void stopReceiving() {
		mAppContext.unregisterReceiver(mBatteryReceiver);
		mIsReceiving = false;
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (mIsReceiving) stopReceiving();
		super.finalize();
	}
	
	public BatteryChangedBroadcastReceiver getReceiver() {
		return mBatteryReceiver;
	}
	
	public boolean isReceiverActive() {
		return mIsReceiving;
	}
	
	
			
	public interface OnBatteryChangedListener {
		void onBatteryChanged();
	}
	
	public class BatteryChangedBroadcastReceiver extends BroadcastReceiver {
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
		
//		private ImageView mIconImageView;
		
		private OnBatteryChangedListener mListener;
		
		public void setOnBatteryChangedListener(OnBatteryChangedListener l) {
			mListener = l;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) return;		
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
			
//			mIconImageView = (ImageView) ((Activity) context).findViewById(mIconResourceId);
			
			if (mListener != null) mListener.onBatteryChanged();
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
		
//		public ImageView getIconImageView() {
//			return mIconImageView;
//		}
		
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
		
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();

			contents.put("Battery Is Present", String.valueOf(isBatteryPresent()));
			contents.put("Battery Level", String.valueOf(getLevel()));
			contents.put("Battery Level Max", String.valueOf(getLevelMax()));
			contents.put("Battery Voltage (mV)", String.valueOf(getVoltage()));
			contents.put("Battery Temperature (C)", String.valueOf(getTemperature()));
			contents.put("Battery Technology", getTechnology());
			contents.put("Battery Status", getStatusString());
			contents.put("Battery Health", getHealthString());
			contents.put("Battery Plugged In Status", getPluggedInStatusString());
			contents.put("Battery Icon Resource ID", String.valueOf(getIconResourceId()));
			
			return contents;
		}
	}
	

	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		contents.put("Listening for Changes", String.valueOf(isReceiverActive()));
		contents.putAll(mBatteryReceiver.getContents());
		
		return contents;
	}

}
