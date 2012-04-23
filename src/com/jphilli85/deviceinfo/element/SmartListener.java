package com.jphilli85.deviceinfo.element;

public interface SmartListener {
	boolean startListening();
	boolean startListening(boolean onlyIfCallbackSet);
	boolean stopListening();
	boolean isListening();
	boolean pause();
	boolean resume();
	boolean isPaused();
	Object getCallback();
	boolean setCallback(Object callback);
}
