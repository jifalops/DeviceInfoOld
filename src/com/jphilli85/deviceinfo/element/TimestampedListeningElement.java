package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

import android.content.Context;

public abstract class TimestampedListeningElement extends ListeningElement implements TimestampedEvents {
	private final long[] mTimestamps;
	
	public TimestampedListeningElement(Context context, int numTimestamps) {
		super(context);
		mTimestamps = new long[numTimestamps];
	}
	
	@Override
	public long getTimestamp(int index) {
		if (!isValidIndex(index)) return 0L;
		return mTimestamps[index];
	}
	
	// Rather this not be public :[
	@Override
	public void setTimestamp(int index) {
		if (!isValidIndex(index)) return;
		mTimestamps[index] = System.currentTimeMillis();
	}
	
	@Override
	public int getNumTimestamps() {
		return mTimestamps.length;
	}
	
	@Override
	public boolean isValidIndex(int index) {
		return index >= 0 && index < mTimestamps.length;
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = super.getContents();
		for (int i = 0; i < mTimestamps.length; ++i) {
			contents.put("Timestamp " + i, String.valueOf(mTimestamps[i]));
		}
		return contents;
	}
}
