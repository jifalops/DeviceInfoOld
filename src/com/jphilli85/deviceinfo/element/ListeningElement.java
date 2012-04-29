package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

public abstract class ListeningElement extends Element implements ElementListener {

	protected interface Callback {}
	
	protected Callback mCallback;
	
	private boolean mIsListening;
	

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
