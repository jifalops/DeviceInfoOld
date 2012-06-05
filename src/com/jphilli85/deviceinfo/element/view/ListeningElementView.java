package com.jphilli85.deviceinfo.element.view;

import android.content.Context;

import com.jphilli85.deviceinfo.element.ListeningElement;


public abstract class ListeningElementView extends ElementView implements PlayableSection.Callback {
	protected ListeningElementView(Context context) {
		super(context);
		mHeader.setCallback(this);
	}
	
	public final boolean isPlaying() {
		return mHeader.isPlaying();
	}
		
	public void onActivityPause() {
		//TODO make preference
		mHeader.pause();
	}
	
	public void onActivityResume() {
		//TODO make preference (caused error)
//		mHeader.play();
	}
	
	@Override
	public void onPlay(PlayableSection section) {
		if (getElement() == null) return;
		((ListeningElement) getElement()).startListening();
	}
	
	@Override
	public void onPause(PlayableSection section) {
		if (getElement() == null) return;
		((ListeningElement) getElement()).stopListening();	
	}
	
	@Override
	protected void onInitialized() {	
		super.onInitialized();
		mHeader.play();
	}
}
