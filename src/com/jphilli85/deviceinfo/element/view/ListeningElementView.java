package com.jphilli85.deviceinfo.element.view;

import com.jphilli85.deviceinfo.element.ElementListener;

import android.content.Context;

public abstract class ListeningElementView extends ElementView implements CollapsibleSection.Callback {

	public ListeningElementView() {
		this(null);
	}
	
	public ListeningElementView(Context context) {
		super(context, true);
		mHeader.setCallback(this);
	}
	
	protected void setElementListener(ElementListener listener) {
		mHeader.setElementListener(listener);
	}
	
	protected void play() {
		mHeader.play();
	}
	
	protected void pause() {
		mHeader.pause();
	}
	
	/** subclasses can use this to pause when the activity pauses */
	public abstract void onActivityPause();
	
	public abstract boolean isPlaying();
}
