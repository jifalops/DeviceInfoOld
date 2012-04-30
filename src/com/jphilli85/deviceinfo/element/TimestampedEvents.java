package com.jphilli85.deviceinfo.element;

public interface TimestampedEvents {
	long getTimestamp(int index);	
	void setTimestamp(int index, long timestamp);	
	boolean isValidIndex(int index);
}
