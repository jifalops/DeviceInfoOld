package com.jphilli85.deviceinfo;

import android.content.Context;
import android.os.Looper;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class DeviceInfo {
	private DeviceInfo() {}
	
	public static final boolean DEBUG = true;
	public static Context sAppContext;
	
	public static long getTimestamp() {
		return System.currentTimeMillis();
//		long ts = 0;
//		try { ts = Calendar.getInstance().getTimeInMillis(); }
//		catch (Exception ignored) {}
//		return ts;
	}
	
	public static boolean isOnMainThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}
	
	public static class DetailsTextView extends TextView {
		public DetailsTextView(Context context) {
			this (context, null);
		}
		public DetailsTextView(Context context, CharSequence text) {
			super(context);
			setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT));
			setText(text);
		}
	}
}
