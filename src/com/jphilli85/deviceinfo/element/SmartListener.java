package com.jphilli85.deviceinfo.element;

public interface SmartListener {
	void startListening();
	void startListening(boolean onlyIfCallbackSet);
	void stopListening();
	boolean isListening();
	void onPause();
	void onResume();
}
