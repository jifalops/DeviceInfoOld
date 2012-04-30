package com.jphilli85.deviceinfo.element;

public abstract class ThrottledListeningElement extends ListeningElement implements ThrottledEvents {
	
	private final int[] mThrottles;
	
	public ThrottledListeningElement(int numEventTypes) {
		super(numEventTypes);
		mThrottles = new int[numEventTypes];
	}
	
	@Override
	public void setUpdateThrottle(int index, int milliseconds) {
		if (!isValidIndex(index)) return;
		mThrottles[index] = milliseconds;
	}

	@Override
	public int getUpdateThrottle(int index) {
		if (!isValidIndex(index)) return 0;		
		return mThrottles[index];
	}

	@Override
	public boolean isUpdateAllowed(int index) {
		if (!isValidIndex(index)) return false;
		return System.currentTimeMillis() - getTimestamp(index) >= mThrottles[index];
	}
}
