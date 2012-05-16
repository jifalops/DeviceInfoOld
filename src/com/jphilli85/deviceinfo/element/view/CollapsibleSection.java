package com.jphilli85.deviceinfo.element.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jphilli85.deviceinfo.Convert;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.DeviceInfo;

public abstract class CollapsibleSection extends AbstractSection {
	public static final int INDEX_EXPAND_COLLAPSE = 0;
	

	private ViewGroup mHeader;
	private ViewGroup mContent;
	
	private boolean mIsCollapsed;

	CollapsibleSection(int layoutRes, int labelIndex, String label) {
		mHeader = (ViewGroup) inflate(layoutRes);
		mContent = (ViewGroup) inflate(R.layout.element_collapsible_content);

		setLabel(label, labelIndex);
		setIcon(R.drawable.holo_dark_collapse, INDEX_EXPAND_COLLAPSE);	
		ExpandCollapseListener listener = new ExpandCollapseListener();
		setListener(INDEX_EXPAND_COLLAPSE, listener);
		setListener(labelIndex, listener);
	}
	
	private void setLabel(String label, int index) {
		((TextView) mHeader.getChildAt(index)).setText(label);
	}
	
	protected ViewGroup getHeader() {
		return mHeader;
	}
	
	protected ViewGroup getContent() {
		return mContent;
	}
	
	protected void setHeaderIndent(int dp) {
		setIndent(mHeader, dp);
	}
	
	protected void setContentIndent(int dp) {
		setIndent(mContent, dp);
	}
	
	private void setIndent(ViewGroup vg, int dp) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(Convert.dpToPx(DeviceInfo.getContext(), dp), 
				lp.topMargin, lp.rightMargin, lp.bottomMargin);
		vg.setLayoutParams(lp);
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
	

	
	public CollapsibleSection add(View view) {
		mContent.addView(view);
		return this;
	}
	
	public CollapsibleSection add(AbstractSection section) {
		section.addToLayout(mContent);		
		return this;
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
}
