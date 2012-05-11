package com.jphilli85.deviceinfo.element;


public abstract class RepeatingTask {
	
	protected Runnable mTask;	
	protected Runnable mCallback;		
	protected int mInterval;
	protected boolean mIsRunning;
	
	public RepeatingTask() {
		this(null);
	}
	
	public RepeatingTask(Runnable task) {		
		mTask = task;
		mInterval = 100;
	}
	

	public abstract boolean start();
	
	public abstract boolean stop();
	
	public final boolean isRunning() {
		return mIsRunning;
	}

	public void setInterval(int interval) {
		mInterval = interval;
	}
	
	public int getInterval() {
		return mInterval;
	}
	
	public final void setCallback(Runnable callback) {
		mCallback = callback;
	}
	
	public final Runnable getCallback() {
		return mCallback;
	}
	
	public final void setTask(Runnable task) {
		mTask = task;
	}
	
	public final Runnable getTask() {
		return mTask;
	}
}
