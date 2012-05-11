package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jphilli85.deviceinfo.R;

public class TableSection extends ElementViewSection {
	private TableLayout mTable;
	
	public TableSection(Context context) {
		super(context);
		mTable = (TableLayout) inflate(R.layout.element_table);
	}
	
	public void add(CharSequence label, CharSequence value) {
		TableRow tr = newRow();
		((TextView) tr.getChildAt(0)).setText(label);
		((TextView) tr.getChildAt(1)).setText(value);
		mTable.addView(tr);
	}
	
	public void add(CharSequence label, View view) {
		TableRow tr = newRow();
		((TextView) tr.getChildAt(0)).setText(label);
		((TextView) tr.getChildAt(1)).setVisibility(View.GONE);
		tr.addView(view);
		mTable.addView(tr);
	}
	
	private TableRow newRow() {
		return (TableRow) inflate(R.layout.element_table_row);
	}
	
	public TextView getValueTextView() {
		TableRow tr = newRow();
		TextView tv = (TextView) tr.getChildAt(1);
		tr.removeView(tv);
		return tv;
	}

	@Override
	public void addToLayout(ViewGroup layout) {
		layout.addView(mTable);
	}

}
