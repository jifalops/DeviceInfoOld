package com.jphilli85.deviceinfo.element.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.DeviceInfo;

public class Header extends PlayableSection {
	public static final int INDEX_ICON = 1;
	public static final int INDEX_LABEL = 2;
	public static final int INDEX_PLAY_PAUSE = 3;
	public static final int INDEX_SAVE = 4;
	public static final int INDEX_PROGRESS_BAR = 5;
	
	private final LinearLayout mContainer;
	
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
		
		mContainer = new LinearLayout(DeviceInfo.getContext());
		mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mContainer.setOrientation(LinearLayout.VERTICAL);
	}
	
	public void showProgressBar() {
		ViewGroup header = getHeader();
		header.getChildAt(INDEX_PLAY_PAUSE).setVisibility(View.GONE);
		header.getChildAt(INDEX_SAVE).setVisibility(View.GONE);
		header.getChildAt(INDEX_PROGRESS_BAR).setVisibility(View.VISIBLE);
	}
	
	public void hideProgressBar() {
		ViewGroup header = getHeader();		
		header.getChildAt(INDEX_PROGRESS_BAR).setVisibility(View.GONE);		
		header.getChildAt(INDEX_SAVE).setVisibility(View.VISIBLE);
		if (getCallback() != null) {
			header.getChildAt(INDEX_PLAY_PAUSE).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void addToLayout(ViewGroup layout) {
		layout.addView(mContainer);
		mContainer.addView(getHeader());
	}
	
	public void showContent() {
		Animation contentAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF,  0.0f, Animation.RELATIVE_TO_SELF,  0.0f,
				Animation.RELATIVE_TO_SELF,  -1.0f, Animation.RELATIVE_TO_SELF,   0.0f
				);
		contentAnimation.setDuration(500);
		contentAnimation.setInterpolator(new AccelerateInterpolator());
		
		getContent().setAnimation(contentAnimation);
		mContainer.addView(getContent());		
	}
}
