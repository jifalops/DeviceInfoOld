package com.jphilli85.deviceinfo.element;

public interface SmartListenersContainer {
	boolean startListeningAll();
	boolean startListeningAll(boolean onlyIfCallbackSet);
	boolean stopListeningAll();
	boolean isListeningAny();
	boolean isListeningAll();
	boolean isPausedAny();
	boolean isPausedAll();
	boolean pauseAll();
	boolean resumeAll();
}
