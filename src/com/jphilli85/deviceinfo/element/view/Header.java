package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.view.View;

import com.jphilli85.deviceinfo.R;

public class Header extends CollapsibleSection {
	public static final int INDEX_ICON = 1;
	public static final int INDEX_LABEL = 2;
	public static final int INDEX_PLAY_PAUSE = 3;
	public static final int INDEX_SAVE = 4;
	
	
	Header(Context context, int iconRes, String label, 
			View.OnClickListener saveListener, boolean supportsPlayPause) {
		super(context, R.layout.element_header, label, INDEX_LABEL, 
				supportsPlayPause ? INDEX_PLAY_PAUSE 
						: CollapsibleSection.INDEX_PLAY_PAUSE_NOT_SUPPORTED);
		setIcon(iconRes, INDEX_ICON);
		setIcon(R.drawable.holo_dark_save, INDEX_SAVE);
		setListener(INDEX_ICON, new ExpandCollapseListener());
		setListener(INDEX_SAVE, saveListener);
	}
}
