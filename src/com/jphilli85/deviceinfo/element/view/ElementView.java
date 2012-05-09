package com.jphilli85.deviceinfo.element.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.ShellHelper;
import com.jphilli85.deviceinfo.element.Element;

public abstract class ElementView {
	private static final List<ElementView> sElementViews = new ArrayList<ElementView>();
	private final Context mContext;	
	protected final List<BasicSection> mSections;
	private String mLabel;
	
	public static final boolean add(Context context, int elementIndex) {	
		Class<? extends ElementView> ev = DeviceInfo.getElementView(elementIndex);
		if (ev == null) return false;
		try {
			sElementViews.add(ev.newInstance());
		} catch (InstantiationException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		}
		return true;
	}
	
	protected ElementView() {
		this(DeviceInfo.getAppContext());
	}
	
	protected ElementView(Context context) {
		mContext = context.getApplicationContext();				
		mSections = new ArrayList<BasicSection>();	
		
		String[] elements = DeviceInfo.getElements();
		for (int i = 0; i < elements.length; ++i) {
			if (this.getClass() == DeviceInfo.getElementView(i)) {
				mLabel = elements[i];
				if (this.getClass() == OverviewView.class) {
					mSections.add(new BasicSection());
				}
				else {
					mSections.add(new Header(DeviceInfo.getElementIconResId(i)));					
				}				
			}
		}
	}	

	public abstract Element getElement();
	
	public final void add(View view) {
		mSections.get(mSections.size() - 1).add(view);
	}
	
	public final void add(BasicSection section) {
		mSections.get(mSections.size() - 1).mViews.addAll(section.mViews);
	}
	
	public final void addSection(BasicSection section) {
		mSections.add(section);
	}
	
	public static final void addToLayout(ViewGroup layout) {
	 	for (ElementView ev : sElementViews) {
	 		for (BasicSection as : ev.mSections) as.addToLayout(layout);	
	 	}
		
	}
	
	protected final View inflate(int resId) {
		return View.inflate(mContext, resId, null);
	}
	
	public final void save() {		
		List<String[]> list = new ArrayList<String[]>();
		LinkedHashMap<String, String> contents = getElement().getContents();
		for (Entry<String, String> e : contents.entrySet()) {
			list.add(new String[] { e.getKey(), e.getValue() });
		}
		
		File file = new File(DeviceInfo.getSnapshotDirectory()
				+ "/" + mLabel
				+ "_" + DeviceInfo.getTimestamp()
				+ ".csv.txt");
		

		try {			
			CSVWriter writer = new CSVWriter(new FileWriter(file));
			writer.writeAll(list);
			writer.close();
			Toast.makeText(mContext, mContext.getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
		}
		catch (SecurityException e) {}
		catch (IOException e) {
			Toast.makeText(mContext, mContext.getString(R.string.save_unsuccessful), Toast.LENGTH_SHORT).show();
		}
	}
	
	public class BasicSection {
		protected final List<View> mViews;
		
		public BasicSection() {
			mViews = new ArrayList<View>();	
		}
		
		public void add(View v) {
			mViews.add(v);
		}
		
		private void addToLayout(ViewGroup layout) {
			for (View v : mViews) layout.addView(v);
		}
		
	}
	
	public abstract class CollapsibleSection extends BasicSection {		
		public static final int INDEX_EXPAND_COLLAPSE = 0;
		
		
		private boolean mIsCollapsed;
		
		private CollapsibleSection(int layoutRes, String label, int labelIndex) {		
			mViews.add(inflate(layoutRes));
			setLabel(label, labelIndex);
			setIcon(R.drawable.holo_dark_collapse, INDEX_EXPAND_COLLAPSE);
			ExpandCollapseListener listener = new ExpandCollapseListener();
			setListener(INDEX_EXPAND_COLLAPSE, listener);
			setListener(labelIndex, listener);
		}
		
		private void setLabel(String label, int index) {
			((TextView) ((ViewGroup) mViews.get(0)).getChildAt(index)).setText(label);
		}
		
		protected void setIcon(int iconRes, int index) {
			if (iconRes == 0) return;
			((ImageView) ((ViewGroup) mViews.get(0)).getChildAt(index)).setImageResource(iconRes);
		}
		
		protected void setListener(int index, View.OnClickListener listener) {
			((ViewGroup) mViews.get(0)).getChildAt(index).setOnClickListener(listener);
		}
		
		private void expand() {
			for (int i = 1; i < mViews.size(); ++i) mViews.get(i).setVisibility(View.VISIBLE);
			setIcon(R.drawable.holo_dark_collapse, INDEX_EXPAND_COLLAPSE);
			mIsCollapsed = false;
		}
		
		private void collapse() {
			for (int i = 1; i < mViews.size(); ++i) mViews.get(i).setVisibility(View.GONE);
			setIcon(R.drawable.holo_dark_expand, INDEX_EXPAND_COLLAPSE);
			mIsCollapsed = true;
		}
		
		protected class ExpandCollapseListener implements View.OnClickListener {
			@Override
			public void onClick(View v) {
				if (mIsCollapsed) expand();
				else collapse();
			}			
		}
	}
	
	private class Header extends CollapsibleSection {
		public static final int INDEX_ICON = 1;
		public static final int INDEX_LABEL = 2;
		public static final int INDEX_SAVE = 3;
		
		private Header(int iconRes) {
			super(R.layout.element_header, mLabel, INDEX_LABEL);
			setIcon(iconRes, INDEX_ICON);
			setIcon(R.drawable.holo_dark_save, INDEX_SAVE);
			setListener(INDEX_ICON, new ExpandCollapseListener());
			setListener(INDEX_SAVE, new SaveListener());
		}	 	
		
		private class SaveListener implements View.OnClickListener {
			@Override
			public void onClick(View v) {
				save();
			}			
		}
	}
	
	protected class Section extends CollapsibleSection {
		public static final int INDEX_LABEL = 1;		
		
		Section(String label) {
			super(R.layout.element_section, label, INDEX_LABEL);			
		}
	}
	
	protected class Subsection extends CollapsibleSection {		
		public static final int INDEX_LABEL = 1;		
		
		Subsection(String label) {
			super(R.layout.element_subsection, label, INDEX_LABEL);			
		}
	}
	
	protected class TableSection extends BasicSection {
		private TableLayout mTable;
		
		public TableSection() {
			mTable = (TableLayout) inflate(R.layout.element_table);
			add(mTable);
		}
		
		public void add(CharSequence label, CharSequence value) {
			TableRow tr = (TableRow) inflate(R.layout.element_table_row);
			((TextView) tr.getChildAt(0)).setText(label);
			((TextView) tr.getChildAt(1)).setText(value);
			mTable.addView(tr);
		}
	}
	
	protected class ListSection extends BasicSection {					
		public void add(CharSequence label, CharSequence value) {
			TextView tv1 = (TextView) inflate(R.layout.element_list_label);
			TextView tv2 = (TextView) inflate(R.layout.element_list_value);			
			tv1.setText(label);
			tv2.setText(value);
			add(tv1);
			add(tv2);		
		}
	}
}
