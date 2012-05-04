package com.jphilli85.deviceinfo;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class DeviceInfo {
	private DeviceInfo() { throw new AssertionError(); }
	
	public static final boolean DEBUG = true;
	private static Context sAppContext;	
	
	private static String sWeeks;
	private static String sDays;
	private static String sHours;
	private static String sMinutes;
	private static String sSeconds;
	
	
	public static void setAppContext(Context context) {
		if (sAppContext == null) {
			sWeeks = context.getString(R.string.unit_week);
			sDays = context.getString(R.string.unit_day);
			sHours = context.getString(R.string.unit_hour);
			sMinutes = context.getString(R.string.unit_minute);
			sSeconds = context.getString(R.string.unit_second);
		}
		sAppContext = context.getApplicationContext();
	}
	
	public static Context getAppContext() {
		return sAppContext;
	}
	
	
//	public static boolean isOnMainThread() {
//		return Looper.getMainLooper().getThread() == Thread.currentThread();
//	}
	
	/** 
	 * Transforms a length of time in seconds to a more comprehensible value.  
	 * i.e. 12w 23d 5h 44m 50s or 12 weeks, 23 days, 5 hours, 44 minutes, 50 seconds.
	 * (Long form not implemented yet.)
	 */
	public static String getDuration(long seconds) {
		return (int) (seconds / (60 * 60 * 24 * 7)) + sWeeks + " "
			+ (seconds / (60 * 60 * 24)) % 7 + sDays + " "
			+ (seconds / (60 * 60)) % 24 + sHours + " "
			+ (seconds / 60) % 60 + sMinutes + " "
			+ seconds % 60 + sSeconds;
	}
	
	/** 
	 * Transforms a length of time in seconds to a more comprehensible value.  
	 * i.e. 12w 23d 5h 44m 50s or 12 weeks, 23 days, 5 hours, 44 minutes, 50 seconds.
	 * (Long form not implemented yet.)
	 */
	public static String getDuration(float seconds) {
		return (int) (seconds / (60 * 60 * 24 * 7)) + sWeeks + " "
			+ (int) (seconds / (60 * 60 * 24)) % 7 + sDays + " "
			+ (int)(seconds / (60 * 60)) % 24 + sHours + " "
			+ (int) (seconds / 60) % 60 + sMinutes + " "
			+ (int) seconds % 60 + sSeconds;
	}
	
	
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
