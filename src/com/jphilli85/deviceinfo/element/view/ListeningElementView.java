package com.jphilli85.deviceinfo.element.view;

import android.content.Context;


public abstract class ListeningElementView extends ElementView implements PlayableSection.Callback {
	protected ListeningElementView(Context context) {
		super(context);
		mHeader.setCallback(this);
	}
	
	/** subclasses can use this to pause when the activity pauses */
	public abstract void onActivityPause();
}
