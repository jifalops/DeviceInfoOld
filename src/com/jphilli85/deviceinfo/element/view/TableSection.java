package com.jphilli85.deviceinfo.element.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jphilli85.deviceinfo.R;

public class TableSection extends AbstractSection {
	private ViewGroup mTable;
	
	public TableSection() {
		super();
		mTable = (ViewGroup) inflate(R.layout.element_table);
	}
	
	public void add(CharSequence label, CharSequence value) {
		ViewGroup tr = newRow();
		((TextView) tr.getChildAt(0)).setText(label);
		((TextView) tr.getChildAt(1)).setText(value);
		mTable.addView(tr);
	}
	
	public void add(CharSequence label, View view) {
		ViewGroup tr = newRow();
		((TextView) tr.getChildAt(0)).setText(label);
		((TextView) tr.getChildAt(1)).setVisibility(View.GONE);
		tr.addView(view);
		mTable.addView(tr);
	}
	
	private ViewGroup newRow() {
		return (ViewGroup) inflate(R.layout.element_table_row);
	}
	
	public TextView getValueTextView() {
		ViewGroup tr = newRow();
		TextView tv = (TextView) tr.getChildAt(1);
		tr.removeView(tv);
		return tv;
	}

	@Override
	public void addToLayout(ViewGroup layout) {
		layout.addView(mTable);
	}

}
