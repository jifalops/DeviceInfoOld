package com.jphilli85.deviceinfo.element.view;

import android.content.Context;


public abstract class ListeningElementView extends ElementView implements PlayableSection.Callback {
	protected ListeningElementView(Context context) {
		super(context);
		mHeader.setCallback(this);
	}
	
	public final boolean isPlaying() {
		return mHeader.isPlaying();
	}
		
	public final void onActivityPause() {
		//TODO make preference
		mHeader.pause();
	}
}
