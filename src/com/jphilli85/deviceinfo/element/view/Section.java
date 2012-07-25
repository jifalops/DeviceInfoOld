package com.jphilli85.deviceinfo.element.view;

import android.content.Context;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.DeviceInfo;

public class Section extends PlayableSection {
	public static final int INDEX_LABEL = 1;
	public static final int INDEX_PLAY_PAUSE = 2;
	
	Section(Context context, String label) {		
		super(context, R.layout.element_section, INDEX_LABEL, label, INDEX_PLAY_PAUSE);	
		setHeaderIndent(context.getResources().getInteger(R.integer.section_header_indent));
		setContentIndent(context.getResources().getInteger(R.integer.section_content_indent));
//		collapse();
	}
}
