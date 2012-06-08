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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;

public abstract class ElementView extends AbstractElementView {
	private final Toast mSaveSuccessfulToast;
	private final Toast mSaveUnsuccessfulToast;
	
	protected Header mHeader;
	private String mLabel;	
	
	private final InitializeTask mInitializeTask;
	
	protected ElementView(Context context) {	
		Context c = context.getApplicationContext();
		mSaveSuccessfulToast = Toast.makeText(c, c.getString(R.string.save_successful), Toast.LENGTH_SHORT);
		mSaveUnsuccessfulToast = Toast.makeText(c, c.getString(R.string.save_unsuccessful), Toast.LENGTH_SHORT);
		
		String[] elements = DeviceInfo.getElements();
		for (int i = 1; i < elements.length; ++i) {
			if (this.getClass() == DeviceInfo.getAbstractView(i)) {
				mLabel = elements[i];
				mHeader = new Header(DeviceInfo.getElementIconResId(i), mLabel, this);
				break;
			}
		}
		mInitializeTask = new InitializeTask();
		mInitializeTask.execute(context);
	}

	protected abstract void initialize(Context context);	
	protected abstract Element getElement();
	
	protected void onInitialized() {
		showContent();
	}

	protected final void add(View view) {
		if (mHeader != null) mHeader.add(view);	
	}
	
	protected final void add(AbstractSection section) {
		if (mHeader != null) mHeader.add(section);
	}
	
	protected final void showContent() {
		if (mHeader != null) mHeader.showContent();
	}
	
	public final void addToLayout(ViewGroup layout) {
		if (mHeader != null) mHeader.addToLayout(layout);
	}
	
	final void save() {	
		new SaveTask().execute();
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
		private final String LOG_TAG = InitializeTask.class.getSimpleName();
		
		@Override
		protected void onPreExecute() {		
			mHeader.showProgressBar();
		}
		
		@Override
		protected Void doInBackground(Context... params) {
			try { initialize(params[0]); }
			catch (Throwable t) {
				if (t.getMessage() != null)	Log.e(LOG_TAG, t.getMessage());
				t.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mHeader.hideProgressBar();
			onInitialized();
		}
	}
	
	private class SaveTask extends AsyncTask<Void, Void, Toast> {
		@Override
		protected Toast doInBackground(Void... params) {			
			Element element = getElement();
			if (element == null) return mSaveUnsuccessfulToast;
			List<String[]> list = new ArrayList<String[]>();
			LinkedHashMap<String, String> contents;
			synchronized (element) {
				contents = element.getContents();
			}
			if (contents == null) return mSaveUnsuccessfulToast;
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
				return mSaveSuccessfulToast;
			}
			catch (SecurityException e) {
				return mSaveUnsuccessfulToast;
			} 
			catch (IOException e) {
				return mSaveUnsuccessfulToast;
			}
		}
		
		
		@Override
		protected void onPostExecute(Toast result) {
			if (result != null) result.show();
		}
	}
	
	public void onActivityPause() {
		mInitializeTask.cancel(true);
	}
	
	public void onActivityResume() {

	}
}
