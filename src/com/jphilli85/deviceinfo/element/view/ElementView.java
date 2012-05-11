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
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.ElementListener;

public abstract class ElementView {
	private static final List<ElementView> sElementViews = new ArrayList<ElementView>();
	private final Context mContext;	
	protected Header mHeader;
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
		this(context, false);
	}
	
	protected ElementView(Context context, boolean supportsPlayPause) {
		mContext = context.getApplicationContext();			
		
		String[] elements = DeviceInfo.getElements();
		for (int i = 0; i < elements.length; ++i) {
			if (this.getClass() == DeviceInfo.getElementView(i)) {
				mLabel = elements[i];
				if (this.getClass() != OverviewView.class) {
					mHeader = new Header(mContext, DeviceInfo.getElementIconResId(i), 
							mLabel, new SaveListener(), supportsPlayPause);
				}
				break;
			}
		}
	}

	protected abstract Element getElement();

	protected final void add(View view) {
		if (mHeader != null) mHeader.add(view);	
	}
	
	protected final void add(ElementViewSection section) {
		if (mHeader != null) mHeader.add(section);
	}
	
	public static final void addToLayout(ViewGroup layout) {
	 	for (ElementView ev : sElementViews) {
	 		if (ev.mHeader != null) ev.mHeader.addToLayout(layout); 
	 	}
		
	}
	
	private class SaveListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			save();
		}			
	}
	
	
	protected final void save() {		
		List<String[]> list = new ArrayList<String[]>();
		LinkedHashMap<String, String> contents = getElement().getContents();
		for (Entry<String, String> e : contents.entrySet()) {
			list.add(new String[] { e.getKey(), e.getValue() });
		}
		
		File file = new File(DeviceInfo.getSnapshotDirectory()
				+ "/" + mLabel
				+ "_" + DeviceInfo.getFilenameTimestamp()
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
}
