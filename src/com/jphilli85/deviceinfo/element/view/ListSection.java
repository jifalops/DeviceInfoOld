package com.jphilli85.deviceinfo.element.view;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jphilli85.deviceinfo.R;

public class ListSection extends AbstractSection {
	private List<View> mViews;
	
	public ListSection() {
		mViews = new ArrayList<View>();
	}
	
	public void add(View view) {
		mViews.add(view);
	}
	
	public void add(CharSequence value) {
		add(value, false);
	}
	
	public void add(CharSequence value, boolean labelStyle) {
		TextView tv;
		if (labelStyle)  tv = (TextView) inflate(R.layout.element_list_label);
		else tv = (TextView) inflate(R.layout.element_list_value);
		tv.setText(value);
		mViews.add(tv);
	}
	
	public void add(CharSequence label, CharSequence value) {
		TextView tv1 = (TextView) inflate(R.layout.element_list_label);
		TextView tv2 = (TextView) inflate(R.layout.element_list_value);			
		tv1.setText(label);
		tv2.setText(value);
		mViews.add(tv1);
		mViews.add(tv2);		
	}

	@Override
	public void addToLayout(ViewGroup layout) {
		for (View v : mViews) layout.addView(v);
	}
}
