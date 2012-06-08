package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;

import com.jphilli85.deviceinfo.element.ListeningElement;


public abstract class ListeningElementView extends ElementView implements PlayableSection.Callback {
	protected ListeningElementView(Context context) {
		super(context);
		mHeader.setCallback(this);		
		mHeader.hidePlayPause();
	}
	
	public final boolean isPlaying() {
		return mHeader.isPlaying();
	}
		
	@Override
	public void onActivityPause() {
		super.onActivityPause();
		//TODO make preference
		mHeader.pause();
	}
	
	@Override
	public void onActivityResume() {
		super.onActivityResume();
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
		//mHeader.play();
	}
}
