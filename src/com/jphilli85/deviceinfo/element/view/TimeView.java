package com.jphilli85.deviceinfo.element.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.TextView;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.ForegroundRepeatingTask;

public class TimeView extends ForegroundRepeatingTask {	
	private TextView mTextView;
	private long mOffset;
	private long mValue;
	private CharSequence mPrefix = "";
	private CharSequence mSuffix = "";
	
	public TimeView(TextView textView) {		
		mTextView = textView;
		setTask(new Runnable() {		
			@Override
			public void run() {
				mValue = System.currentTimeMillis() - mOffset;
				setText(DeviceInfo.getDuration(
					(int) (mValue / 1000 + 0.5f)));
			}
		}); 
		setInterval(1000);
	}
	
	public TimeView(TextView textView, final SimpleDateFormat format) {
		mTextView = textView;
		setTask(new Runnable() {		
			@Override
			public void run() {
				mValue = System.currentTimeMillis() - mOffset;
				setText(format.format(new Date(mValue)));
			}
		}); 
		setInterval(1000);
	}
	
	private void setText(CharSequence text) {
		mTextView.setText(mPrefix);
		mTextView.append(text);
		mTextView.append(mSuffix);
	}

	public void setOffset(long offset) {
		mOffset = offset;
	}
	
	public long getOffset() {
		return mOffset;
	}
	
	/** The most recent timestamp - the offset */
	public long getValue() {
		return mValue;
	}
	
	public void setPrefix(CharSequence prefix) {
		mPrefix = prefix;
	}
	
	public void setSuffix(CharSequence suffix) {
		mSuffix = suffix;
	}
	
	public CharSequence getPrefix() {
		return mPrefix;
	}
	
	public CharSequence getSuffix() {
		return mSuffix;
	}
}
