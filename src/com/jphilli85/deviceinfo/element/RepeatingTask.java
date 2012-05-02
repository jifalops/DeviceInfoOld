package com.jphilli85.deviceinfo.element;

import android.os.AsyncTask;
import android.os.Handler;

public class RepeatingTask {
	private final Handler mHandler;
	private final Runnable mTask;
	
	private Runnable mCallback;
	private BackgroundTask mBackgroundTask;	
	private int mInterval;
	private boolean mIsRunning;
	private boolean mIsBackground;
	
	public RepeatingTask(Runnable task) {
		mHandler = new Handler();
		mTask = task;
		mInterval = 100;
	}
	

	public void start(boolean doInBackground) {	
		if (mIsRunning) return;
		mIsBackground = doInBackground;
		
		if (doInBackground) {
			mBackgroundTask = new BackgroundTask();
			mBackgroundTask.execute();
			
		}
		else {			
			mHandler.post(new Runnable() {				
				@Override
				public void run() {
					mTask.run();
					if (mCallback != null) mCallback.run();
					mHandler.postDelayed(this, mInterval);
				}
			});
		}
		mIsRunning = true;
	}
	
	public void stop() {
		if (!mIsRunning) return;
		
		if (mIsBackground) {
			mBackgroundTask.cancel(true);
		}
		else {
			mHandler.removeCallbacks(mTask);
		}		
		mIsRunning = false;
	}
	
	public boolean isRunning() {
		return mIsRunning;
	}

	public synchronized void setInterval(int interval) {
		mInterval = interval;
	}
	
	public synchronized int getInterval() {
		return mInterval;
	}
	
	public void setCallback(Runnable callback) {
		mCallback = callback;
	}
	
	public Runnable getCallback() {
		return mCallback;
	}
	
	private class BackgroundTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			while (true) {
				mTask.run();
				publishProgress();
				try { Thread.sleep(mInterval); } 
				catch (InterruptedException e) {
//					break;
				}
			}
//			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			if (mCallback != null) mCallback.run();
		}
	}
}
