package com.jphilli85.deviceinfo.element;

public interface SmartListenerContainer {
	void startListeningAll();
	void startListeningAll(boolean onlyIfCallbackSet);
	void stopListeningAll();
	boolean isListeningAny();
	boolean isListeningAll();
	void onPauseAll();
	void onResumeAll();
}
