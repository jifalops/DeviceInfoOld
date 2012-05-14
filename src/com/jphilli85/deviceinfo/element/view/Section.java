package com.jphilli85.deviceinfo.element.view;

import com.jphilli85.deviceinfo.R;

public class Section extends PlayableSection {
	public static final int INDEX_LABEL = 1;
	public static final int INDEX_PLAY_PAUSE = 2;
	
	Section(String label) {
		super(R.layout.element_section, INDEX_LABEL, label, INDEX_PLAY_PAUSE);			
	}
}
