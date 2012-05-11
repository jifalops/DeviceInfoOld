package com.jphilli85.deviceinfo.element;

import android.os.Handler;

public class ForegroundRepeatingTask extends RepeatingTask {
	private final Handler mHandler;
	
	public ForegroundRepeatingTask() {
		this(null);
	}
	
	public ForegroundRepeatingTask(Runnable task) {
		super(task);
		mHandler = new Handler();
	}
	
	@Override
	public boolean start() {
		if (mIsRunning) return false;
		mIsRunning = true;
		mHandler.post(new Runnable() {				
			@Override
			public void run() {	
				if (!mIsRunning) return;
				mTask.run();
				if (mCallback != null) mCallback.run();
				mHandler.postDelayed(this, mInterval);
			}
		});		
		return true;
	}
	
	@Override
	public boolean stop() {
		if (!mIsRunning) return false;
		mHandler.removeCallbacks(mTask);
		mIsRunning = false;
		return true;
	}
}
