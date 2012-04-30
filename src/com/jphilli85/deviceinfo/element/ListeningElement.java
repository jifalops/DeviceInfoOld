package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

public abstract class ListeningElement extends Element implements ElementListener, TimestampedEvents {

	protected interface Callback {}
	
	private Callback mCallback;	
	private boolean mIsListening;
	private final long[] mTimestamps;	
	
	public ListeningElement(int numEventTypes) {
		mTimestamps = new long[numEventTypes];
	}
	
	@Override
	public long getTimestamp(int index) {
		if (!isValidIndex(index)) return 0L;
		return mTimestamps[index];
	}
	
	// Rather this not be public :[
	@Override
	public void setTimestamp(int index, long timestamp) {
		if (!isValidIndex(index)) return;
		mTimestamps[index] = timestamp;
	}
	
	@Override
	public boolean isValidIndex(int index) {
		return index >= 0 && index < mTimestamps.length;
	}
	
	
	
	@Override
	public final boolean startListening() {
		return startListening(true);
	}
	
	/** 
	 * Subclasses should call this to determine whether to continue with their
	 * implementation of startListening(boolean). This does not change the state
	 * of the member mIsListening.
	 */
	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		return !mIsListening && (!onlyIfCallbackSet || mCallback != null);		
	}
	
	/** 
	 * Subclasses should call this to determine whether to continue with their
	 * implementation of stopListening(). This does not change the state
	 * of the member mIsListening.
	 */
	@Override
	public boolean stopListening() {
		return mIsListening;
	}
	
	protected boolean setListening(boolean listening) {
		mIsListening = listening;
		return listening;
	}

	@Override
	public final boolean isListening() {
		return mIsListening;
	}
	
	public final Callback getCallback() {
		return mCallback;
	}
	
	/** 
	 * Subclasses should make sure the callback is of the correct type.
	 * Using the generic callback defined in this class (ListeningElement)
	 * will result in a runtime exception. 
	 */
	public final void setCallback(Callback callback) {
		mCallback = callback;
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		contents.put("Is Listening", String.valueOf(mIsListening));
		return contents;
	}
}
