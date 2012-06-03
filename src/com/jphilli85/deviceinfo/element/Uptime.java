package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;
import java.util.List;

import android.os.Handler;

import com.jphilli85.deviceinfo.ShellHelper;
import com.jphilli85.deviceinfo.app.DeviceInfo;

public class Uptime extends ListeningElement {
	
	public interface Callback extends ListeningElement.Callback {
		void onUptimeUpdated(float uptimeTotal, float uptimeAsleep);
	}
	
	private float mUptimeTotal;
	private float mUptimeAsleep;
	
	private final ForegroundRepeatingTask mUpdateTask;

	public Uptime() {
		mUpdateTask = new ForegroundRepeatingTask(new Runnable() {
			public void run() {		        
				updateUptime();
		   }
		});
		mUpdateTask.setInterval(1000);		
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
		if (getCallback() != null) ((Callback) getCallback()).onUptimeUpdated(mUptimeTotal, mUptimeAsleep);
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = super.getContents();
		contents.put("Uptime Total", DeviceInfo.getDuration((long) mUptimeTotal));
		contents.put("Uptime Sleep", DeviceInfo.getDuration((long) mUptimeAsleep));
		contents.put("Uptime Awake", DeviceInfo.getDuration((long) (mUptimeTotal - mUptimeAsleep)));
		return contents;
	}


	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		if (!super.startListening(onlyIfCallbackSet)) return false;				
		mUpdateTask.start();		
		return setListening(true);
	}

	@Override
	public boolean stopListening() {
		if (!super.stopListening()) return false;
		mUpdateTask.stop();
		return !setListening(false);
	}


//	@Override
//	public boolean pause() {
//		if (mIsPaused) return false;
//		mIsPaused = stopListening();
//		return mIsPaused;
//	}
//
//	@Override
//	public boolean resume() {
//		if (!mIsPaused) return false;
//		mIsPaused = !startListening();
//		return !mIsPaused;
//	}
//
//	@Override
//	public boolean isPaused() {
//		return mIsPaused;
//	}
//
//	@Override
//	public Object getCallback() {
//		return mCallback;
//	}
//
//	@Override
//	public boolean setCallback(Object callback) {
//		if (callback instanceof Callback) {
//			mCallback = (Callback) callback;
//			return true;
//		}
//		return false;
//	}

}
