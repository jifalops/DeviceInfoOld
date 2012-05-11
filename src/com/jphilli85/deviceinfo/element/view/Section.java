package com.jphilli85.deviceinfo.element.view;

import android.content.Context;

import com.jphilli85.deviceinfo.R;

public class Section extends CollapsibleSection {
	public static final int INDEX_LABEL = 1;
	public static final int INDEX_PLAY_PAUSE = 2;
	
	Section(Context context, String label, boolean supportsPlayPause) {
		super(context, R.layout.element_section, label, INDEX_LABEL,
				supportsPlayPause ? INDEX_PLAY_PAUSE 
						: CollapsibleSection.INDEX_PLAY_PAUSE_NOT_SUPPORTED);			
	}
}
