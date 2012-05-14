package com.jphilli85.deviceinfo.element.view;

import android.view.View;

import com.jphilli85.deviceinfo.R;

public class Header extends PlayableSection {
	public static final int INDEX_ICON = 1;
	public static final int INDEX_LABEL = 2;
	public static final int INDEX_PLAY_PAUSE = 3;
	public static final int INDEX_SAVE = 4;
	
	
	Header(int iconRes, String label, final ElementView elementView) {
		super(R.layout.element_header, INDEX_LABEL, label, INDEX_PLAY_PAUSE);
		setIcon(iconRes, INDEX_ICON);
		setIcon(R.drawable.holo_dark_save, INDEX_SAVE);
		setListener(INDEX_ICON, new ExpandCollapseListener());
		setListener(INDEX_SAVE, new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				elementView.save();				
			}
		});
	}
}
