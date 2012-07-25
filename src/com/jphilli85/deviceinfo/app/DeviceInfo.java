package com.jphilli85.deviceinfo.app;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Looper;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.ShellHelper;
import com.jphilli85.deviceinfo.element.view.AbstractElementView;
import com.jphilli85.deviceinfo.element.view.AudioView;
import com.jphilli85.deviceinfo.element.view.BatteryView;
import com.jphilli85.deviceinfo.element.view.BluetoothView;
import com.jphilli85.deviceinfo.element.view.CameraView;
import com.jphilli85.deviceinfo.element.view.CellularView;
import com.jphilli85.deviceinfo.element.view.CpuView;
import com.jphilli85.deviceinfo.element.view.DisplayView;
import com.jphilli85.deviceinfo.element.view.FeaturesView;
import com.jphilli85.deviceinfo.element.view.GraphicsView;
import com.jphilli85.deviceinfo.element.view.IdentifiersView;
import com.jphilli85.deviceinfo.element.view.KeysView;
import com.jphilli85.deviceinfo.element.view.LocationView;
import com.jphilli85.deviceinfo.element.view.NetworkView;
import com.jphilli85.deviceinfo.element.view.Overview;
import com.jphilli85.deviceinfo.element.view.PlatformView;
import com.jphilli85.deviceinfo.element.view.PropertiesView;
import com.jphilli85.deviceinfo.element.view.RamView;
import com.jphilli85.deviceinfo.element.view.SensorsView;
import com.jphilli85.deviceinfo.element.view.StorageView;
import com.jphilli85.deviceinfo.element.view.UptimeView;
import com.jphilli85.deviceinfo.element.view.WifiView;

public class DeviceInfo extends Application {
	
	public static final boolean DEBUG = true;
	
	public static final String PACKAGE = DeviceInfo.class.getPackage().getName();
	
	public static final int THEME_HOLO_DARK = 1;
	public static final int THEME_HOLO_LIGHT = 2;
	public static final int THEME_HOLO_LIGHT_DARKACTIONBAR = 3;
	
	public static final String KEY_THEME = PACKAGE + ".THEME";
	
	
	private static Context sAppContext;	
	
	private static SharedPreferences sPrefs;
	private static int sTheme;
	private static String sDataDir;
	private static String sSnapshotDir;
	private static String sWeeks;
	private static String sDays;
	private static String sHours;
	private static String sMinutes;
	private static String sSeconds;
	private static SimpleDateFormat sFilenameDateFormat;
	private static SimpleDateFormat sDisplayDateFormat;
	private static int[][] sGroups;
	private static String[] sElements;

	@Override
	public void onCreate() {
		super.onCreate();
		sAppContext = this;
		
		// initialize directories		
		sDataDir = Environment.getExternalStorageDirectory().getAbsolutePath() 
				+ "/" + getString(R.string.data_directory);
		sSnapshotDir = DeviceInfo.getDataDirectory()
				+ "/" + getString(R.string.snapshots_directory);
		
		try {
			ShellHelper.exec("mkdir " + sDataDir);
			ShellHelper.exec("mkdir " + sSnapshotDir);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// initialize date strings
		sWeeks = getString(R.string.unit_week);
		sDays = getString(R.string.unit_day);
		sHours = getString(R.string.unit_hour);
		sMinutes = getString(R.string.unit_minute);
		sSeconds = getString(R.string.unit_second);
		
		// initialize date formats
		sFilenameDateFormat = new SimpleDateFormat(getString(R.string.timestamp_filename));
		sDisplayDateFormat = new SimpleDateFormat(getString(R.string.timestamp_display),
				Locale.getDefault());
		
		// Dynamically support up to 100 groups defined in xml
		// TODO use a DB
		List<int[]> list = new ArrayList<int[]>();
		try {
			int resId;				
			for (int i = 0; i < 100; ++i) {
				Field f = R.array.class.getField("group" + i);
				resId = 0;
				try { resId = f.getInt(null); } 
				catch (IllegalArgumentException ignored) {} 
				catch (IllegalAccessException ignored) {}
				if (resId == 0) continue;
				list.add(getResources().getIntArray(resId));
			}
		}
		catch (NoSuchFieldException ignored) {}
		sGroups = list.toArray(new int[list.size()][]);
		
		
		//
		sElements = getResources().getStringArray(R.array.elements);
		
		//
		sPrefs = getSharedPreferences(PACKAGE + "_preferences", MODE_PRIVATE);
		
		//
		sTheme = sPrefs.getInt(KEY_THEME, THEME_HOLO_DARK);
	}
	
//	public static Context getContext() {
//		return sAppContext;
//	}
	
	public static SharedPreferences getPrefs() {
		return sPrefs;
	}
	
	public static int getHoloTheme() {
		return sTheme;
	}
	
	public static String getDataDirectory() {
		return sDataDir;
	}

	public static String getSnapshotDirectory() {
		return sSnapshotDir;
	}
	
	/** 
	 * Transforms a length of time in seconds to a more comprehensible value.  
	 * i.e. 12w 23d 5h 44m 50s or 12 weeks, 23 days, 5 hours, 44 minutes, 50 seconds.
	 * (Long form not implemented yet.)
	 */
	public static String getDuration(long seconds) {
		int w = (int) (seconds / (60 * 60 * 24 * 7));
		int d = (int) (seconds / (60 * 60 * 24)) % 7;
		int h = (int) (seconds / (60 * 60)) % 24;
		int m = (int) (seconds / 60) % 60;
		int s = (int) seconds % 60;
		String result = "";
		if (w > 0) result += w + sWeeks + " ";
		if (d > 0) result += d + sDays + " ";
		if (h > 0) result += h + sHours + " ";
		if (m > 0) result += m + sMinutes + " ";
		result += s + sSeconds;
		return result;
	}
	
	/** 
	 * Transforms a length of time in seconds to a more comprehensible value.  
	 * i.e. 12w 23d 5h 44m 50s or 12 weeks, 23 days, 5 hours, 44 minutes, 50 seconds.
	 * (Long form not implemented yet.)
	 */
	public static String getDuration(float seconds) {
		int w = (int) (seconds / (60 * 60 * 24 * 7));
		int d = (int) (seconds / (60 * 60 * 24)) % 7;
		int h = (int) (seconds / (60 * 60)) % 24;
		int m = (int) (seconds / 60) % 60;
		float s = seconds % 60;
		String result = "";
		if (w > 0) result += w + sWeeks + " ";
		if (d > 0) result += d + sDays + " ";
		if (h > 0) result += h + sHours + " ";
		if (m > 0) result += m + sMinutes + " ";
		result += s + sSeconds;
		return result;
	}	
	
	public static String getFilenameTimestamp() {
		return sFilenameDateFormat.format(new Date());
	}
	
	public static String getDisplayTimestamp() {
		return sDisplayDateFormat.format(new Date());
	}

	public static boolean isOnMainThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}

	public static int[][] getGroups() {
		return sGroups;
	}
	
	public static String[] getElements() {
		return sElements;
	}
		
	public static Class<? extends AbstractElementView> getAbstractView(int elementIndex) {
		switch (elementIndex) {
		case 0: return Overview.class;
		case 1: return AudioView.class;
		case 2: return BatteryView.class;
		case 3: return BluetoothView.class;
		case 4: return CameraView.class;
		case 5: return CellularView.class;
		case 6: return CpuView.class;
		case 7: return DisplayView.class;
		case 8: return FeaturesView.class;
		case 9: return GraphicsView.class;
		case 10: return IdentifiersView.class;
		case 11: return KeysView.class;
		case 12: return LocationView.class;
		case 13: return NetworkView.class;
		case 14: return PlatformView.class;
		case 15: return PropertiesView.class;
		case 16: return RamView.class;
		case 17: return SensorsView.class;
		case 18: return StorageView.class;
		case 19: return UptimeView.class;
		case 20: return WifiView.class;
		default: return null;
		}
	}
	
	public static int getElementIconResId(int elementIndex) {
		switch (sTheme) {
		case THEME_HOLO_DARK: 
			switch (elementIndex) {
			case 0: return 0;
			case 1: return R.drawable.holo_dark_audio;
			case 2: return R.drawable.holo_dark_battery;
			case 3: return R.drawable.holo_dark_bluetooth;
			case 4: return R.drawable.holo_dark_camera;
			case 5: return R.drawable.holo_dark_cell;
			case 6: return 0;
			case 7: return R.drawable.holo_dark_phone;
			case 8: return 0;
			case 9: return 0;
			case 10: return 0;
			case 11: return R.drawable.holo_dark_keyboard;
			case 12: return R.drawable.holo_dark_location;
			case 13: return R.drawable.holo_dark_globe;
			case 14: return 0;
			case 15: return 0;
			case 16: return 0;
			case 17: return 0;
			case 18: return R.drawable.holo_dark_storage;
			case 19: return 0;
			case 20: return R.drawable.holo_dark_wifi;
			default: return 0;
			}
		case THEME_HOLO_LIGHT:			
			// Fall through
		case THEME_HOLO_LIGHT_DARKACTIONBAR:
			// TODO support light theme
		default: return 0;
		}
	}
	
	public static double getPercent(double value, double outOf) {
		if (value == 0 || outOf == 0) return 0;		
		return value / outOf * 100;
	}
}
