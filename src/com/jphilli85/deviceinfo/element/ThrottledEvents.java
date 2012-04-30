package com.jphilli85.deviceinfo.element;

public interface ThrottledEvents extends TimestampedEvents {
	void setUpdateThrottle(int index, int milliseconds);
	int getUpdateThrottle(int index);
	boolean isUpdateAllowed(int index);
}
