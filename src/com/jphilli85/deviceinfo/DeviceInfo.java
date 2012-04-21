package com.jphilli85.deviceinfo;

import android.content.Context;
import android.os.Looper;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class DeviceInfo {
	private DeviceInfo() { throw new AssertionError(); }
	
	public static final boolean DEBUG = true;
	public static Context sAppContext;
	
//	public static boolean isOnMainThread() {
//		return Looper.getMainLooper().getThread() == Thread.currentThread();
//	}
	
	
	
	// TODO get rid of this
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
