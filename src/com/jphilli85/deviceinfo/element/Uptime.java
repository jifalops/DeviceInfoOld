package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;
import java.util.List;

import android.os.Handler;

import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.ShellHelper;

public class Uptime implements ContentsMapper, ElementListener {
	
	public interface Callback {
		void onUptimeUpdated(float uptimeTotal, float uptimeAsleep);
	}
	
	private float mUptimeTotal;
	private float mUptimeAsleep;
	
	private final Handler mHandler;
	private final Runnable mUpdateRunnable;
	
	private boolean mIsPaused;
	private boolean mIsListening;
	private Callback mCallback;
	
	public Uptime() {
		mHandler = new Handler();
		
		mUpdateRunnable = new Runnable() {
			public void run() {		        
				updateUptime();
			    mHandler.postDelayed(this, 500);
		   }
		};
	}
	
	public float getUptimeTotal() {
		return mUptimeTotal;
	}
	
	public float getUptimeAsleep() {
		return mUptimeAsleep;
	}
	
	public float getUptimeAwake() {
		return mUptimeTotal - mUptimeAsleep;
	}
	
	
	private void updateUptime() {
		List<String> list = ShellHelper.getProc("uptime");
		if (list == null || list.isEmpty()) return;
		String[] parts = list.get(0).split("\\s+");
		try {
			if (parts.length >= 2) {
				mUptimeTotal = Float.valueOf(parts[0]);
				mUptimeAsleep = Float.valueOf(parts[1]);
			}
			else if (parts.length == 1) mUptimeTotal = Float.valueOf(parts[0]);
		}
		catch (NumberFormatException ignored) {}
		if (mCallback != null) mCallback.onUptimeUpdated(mUptimeTotal, mUptimeAsleep);
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();		
		contents.put("Uptime Total", DeviceInfo.getDuration(mUptimeTotal));
		contents.put("Uptime Sleep", DeviceInfo.getDuration(mUptimeAsleep));
		contents.put("Uptime Awake", DeviceInfo.getDuration(mUptimeTotal - mUptimeAsleep));
		contents.put("IsListening", String.valueOf(mIsListening));
		contents.put("IsPaused", String.valueOf(mIsPaused));
		return contents;
	}

	@Override
	public boolean startListening() {
		return startListening(true);
	}

	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		if (mIsListening || (onlyIfCallbackSet && mCallback == null)) return false;		
		mHandler.post(mUpdateRunnable);
		mIsListening = true;
		return mIsListening;
	}

	@Override
	public boolean stopListening() {
		if (!mIsListening) return false;
		mHandler.removeCallbacks(mUpdateRunnable);
		mIsListening = false;
		return !mIsListening;
	}

	@Override
	public boolean isListening() {
		return mIsListening;
	}

	@Override
	public boolean pause() {
		if (mIsPaused) return false;
		mIsPaused = stopListening();
		return mIsPaused;
	}

	@Override
	public boolean resume() {
		if (!mIsPaused) return false;
		mIsPaused = !startListening();
		return !mIsPaused;
	}

	@Override
	public boolean isPaused() {
		return mIsPaused;
	}

	@Override
	public Object getCallback() {
		return mCallback;
	}

	@Override
	public boolean setCallback(Object callback) {
		if (callback instanceof Callback) {
			mCallback = (Callback) callback;
			return true;
		}
		return false;
	}

}
