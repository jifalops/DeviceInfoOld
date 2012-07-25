package com.jphilli85.deviceinfo.element.view;

import android.content.Context;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.DeviceInfo;

public class Subsection extends PlayableSection {
	public static final int INDEX_LABEL = 1;	
	public static final int INDEX_PLAY_PAUSE = 2;
	
	Subsection(Context context, String label) {
		super(context, R.layout.element_subsection, INDEX_LABEL, label, INDEX_PLAY_PAUSE);		
		setHeaderIndent(getContext().getResources().getInteger(R.integer.subsection_header_indent));
		setContentIndent(getContext().getResources().getInteger(R.integer.subsection_content_indent));
	}
}
