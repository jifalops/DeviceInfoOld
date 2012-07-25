package com.jphilli85.deviceinfo.element.view;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.widget.TextView;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Ram;


public class RamView extends ListeningElementView implements Ram.Callback {
	private Ram mRam;
	private int mLastSize;
	private TextView[] mValues;
	
	public RamView(Context context) {
		super(context);
	}

	@Override
	public Element getElement() {
		return mRam;
	}

	@Override
	protected void initialize(Context context) {
		mRam = new Ram(context);
		mRam.setCallback(this);
	}

	@Override
	public void onUpdated(LinkedHashMap<String, String> meminfo) {
		if (meminfo.size() != mLastSize) {
			mLastSize = meminfo.size();
			mValues = new TextView[mLastSize + 1];					
			
			mHeader.getContent().removeAllViews();
			TableSection table = new TableSection(getContext()) ;
			
			mValues[0] = table.getValueTextView();
			table.add("Usage (%)", mValues[0]);
			mValues[0].setText(mRam.getUsagePercent());
			
			int i = 1;
			for (Entry<String, String> e : meminfo.entrySet()) {	
				mValues[i] = table.getValueTextView();
				table.add(e.getKey(), mValues[i]);
				mValues[i].setText(e.getValue());
				++i;
			}
			add(table);
		}
		else {
			mValues[0].setText(mRam.getUsagePercent());
			int i = 1;
			for (Entry<String, String> e : meminfo.entrySet()) {				
				mValues[i].setText(e.getValue());
				++i;
			}
		}
	}

}
