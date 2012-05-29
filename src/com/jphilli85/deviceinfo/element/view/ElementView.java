package com.jphilli85.deviceinfo.element.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;

public abstract class ElementView extends AbstractElementView {
	private static final List<ElementView> sElementViews = new ArrayList<ElementView>();
	protected Header mHeader;
	private String mLabel;	
	
	public static final boolean add(int[] elementIndexes) {
		sElementViews.clear();
		if (elementIndexes == null) return false;
		boolean result = true;		
		for (int i : elementIndexes) {
			if(!add(i)) result = false;			
		}
		return result;
	}
	
	public static final boolean add(int elementIndex) {	
		Class<? extends AbstractElementView> ev = DeviceInfo.getAbstractView(elementIndex);
		if (ev == null) return false;
		try {
			sElementViews.add((ElementView) ev.newInstance());
		} catch (InstantiationException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		} catch (ClassCastException e) {
			return false;
		}
		
		return true;
	}
	
	public static final List<ElementView> getElementViews() {
		return sElementViews;
	}
	
	public static final void addToLayout(ViewGroup layout) {
	 	for (ElementView ev : sElementViews) {
	 		if (ev.mHeader != null) ev.mHeader.addToLayout(layout); 
	 	}
		
	}
	
	protected ElementView(Context context) {		
		String[] elements = DeviceInfo.getElements();
		for (int i = 1; i < elements.length; ++i) {
			if (this.getClass() == DeviceInfo.getAbstractView(i)) {
				mLabel = elements[i];
				mHeader = new Header(DeviceInfo.getElementIconResId(i), mLabel, this);
				break;
			}
		}
		new InitializeTask().execute(context);
	}

	protected abstract void initialize(Context context);
	protected abstract void onInitialized();
	protected abstract Element getElement();

	protected final void add(View view) {
		if (mHeader != null) mHeader.add(view);	
	}
	
	protected final void add(AbstractSection section) {
		if (mHeader != null) mHeader.add(section);
	}	
	
	final void save() {	
		if (getElement() == null) return;
		List<String[]> list = new ArrayList<String[]>();
		LinkedHashMap<String, String> contents = getElement().getContents();
		for (Entry<String, String> e : contents.entrySet()) {
			list.add(new String[] { e.getKey(), e.getValue() });
		}
		
		File file = new File(DeviceInfo.getSnapshotDirectory()
				+ "/" + mLabel
				+ "_" + DeviceInfo.getFilenameTimestamp()
				+ ".csv.txt");
		
		Context c = DeviceInfo.getContext();
		
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(file));
			writer.writeAll(list);
			writer.close();
			Toast.makeText(c, c.getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
		}
		catch (SecurityException e) {}
		catch (IOException e) {
			Toast.makeText(c, c.getString(R.string.save_unsuccessful), Toast.LENGTH_SHORT).show();
		}
	}
	
	protected final void showElementContents() {		
		TableSection table = new TableSection();		
		LinkedHashMap<String, String> map = getElement().getContents();
		for (String key : map.keySet()) {
			table.add(key,  map.get(key));
		}
		mHeader.getContent().removeAllViews();
		add(table);
	}
	
	private class InitializeTask extends AsyncTask<Context, Void, Void> {
		@Override
		protected void onPreExecute() {		
			//TODO mHeader.showProgressThingy
		}
		
		@Override
		protected Void doInBackground(Context... params) {
			initialize(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO mHeader.hideProgressThingy
			onInitialized();
		}
	}
}
