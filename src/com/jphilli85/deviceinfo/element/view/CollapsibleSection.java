package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.element.ElementListener;

public abstract class CollapsibleSection extends ElementViewSection {
	public static final int INDEX_EXPAND_COLLAPSE = 0;
	public static final int INDEX_PLAY_PAUSE_NOT_SUPPORTED = -1;
	
	public interface Callback {
		void onPlay();
		void onPause();
	}
	
	private ViewGroup mHeader;
	private ViewGroup mContent;
	
	private boolean mIsCollapsed;
	private Callback mCallback;
	
	private ElementListener mListener;
	private boolean mIsPlaying;
	private int mPlayPauseIndex;
	
	CollapsibleSection(Context context, int layoutRes, String label, int labelIndex, int playPauseIndex) {
		super(context);
		mHeader = (ViewGroup) inflate(layoutRes);
		mContent = (ViewGroup) inflate(R.layout.element_collapsible_content);
		mPlayPauseIndex = playPauseIndex;
		setLabel(label, labelIndex);
		setIcon(R.drawable.holo_dark_collapse, INDEX_EXPAND_COLLAPSE);	
		ExpandCollapseListener listener = new ExpandCollapseListener();
		setListener(INDEX_EXPAND_COLLAPSE, listener);
		setListener(labelIndex, listener);
		
		if (playPauseIndex != INDEX_PLAY_PAUSE_NOT_SUPPORTED) {
			mHeader.getChildAt(playPauseIndex).setVisibility(View.VISIBLE);
			setIcon(R.drawable.holo_dark_play, playPauseIndex);
			setListener(playPauseIndex, new PlayPauseListener());
		}
	}
	
	void setCallback(Callback callback) {
		mCallback = callback;
	}
	
	Callback getCallback() {
		return mCallback;
	}
	
	
	private void setLabel(String label, int index) {
		((TextView) mHeader.getChildAt(index)).setText(label);
	}
	
	protected void setIcon(int iconRes, int index) {
		if (iconRes == 0) return;
		((ImageView) mHeader.getChildAt(index)).setImageResource(iconRes);
	}
	
	protected void setListener(int index, View.OnClickListener listener) {
		mHeader.getChildAt(index).setOnClickListener(listener);
	}
	
	private void expand() {
		mContent.setVisibility(View.VISIBLE);
		setIcon(R.drawable.holo_dark_collapse, INDEX_EXPAND_COLLAPSE);
		mIsCollapsed = false;
	}
	
	private void collapse() {
		mContent.setVisibility(View.GONE);
		setIcon(R.drawable.holo_dark_expand, INDEX_EXPAND_COLLAPSE);
		mIsCollapsed = true;
	}
	
	public void setElementListener(ElementListener listener) {
		mListener = listener;		
	}
	
	protected void pause() {
		mListener.stopListening();
		setIcon(R.drawable.holo_dark_play, mPlayPauseIndex);
		mIsPlaying = false;
		if (mCallback != null) mCallback.onPause();
	}
	
	protected void play() {
		mListener.startListening();
		setIcon(R.drawable.holo_dark_pause, mPlayPauseIndex);
		mIsPlaying = true;
		if (mCallback != null) mCallback.onPlay();
	}
	
	public void add(View view) {
		mContent.addView(view);
	}
	
	public void add(ElementViewSection section) {
		section.addToLayout(mContent);		
	}
	
	@Override
	public final void addToLayout(ViewGroup layout) {
		layout.addView(mHeader);
		layout.addView(mContent);
	}
	
	
	protected class ExpandCollapseListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (mIsCollapsed) expand();
			else collapse();
		}			
	}
	
	private class PlayPauseListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (mIsPlaying) pause();
			else play();
		}
	}
}
