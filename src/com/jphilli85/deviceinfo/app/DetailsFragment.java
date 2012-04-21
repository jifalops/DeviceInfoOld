package com.jphilli85.deviceinfo.app;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.DeviceInfo.DetailsTextView;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.component.SensorsView;
import com.jphilli85.deviceinfo.data.DeviceInfoContract.Group;
import com.jphilli85.deviceinfo.data.DeviceInfoContract.Subgroup;
import com.jphilli85.deviceinfo.element.Audio;
import com.jphilli85.deviceinfo.element.Battery;
import com.jphilli85.deviceinfo.element.Camera;
import com.jphilli85.deviceinfo.element.Cellular;
import com.jphilli85.deviceinfo.element.Cpu;
import com.jphilli85.deviceinfo.element.Display;
import com.jphilli85.deviceinfo.element.Graphics;
import com.jphilli85.deviceinfo.element.Location;
import com.jphilli85.deviceinfo.element.Location.ProviderWrapper;
import com.jphilli85.deviceinfo.element.Network;
import com.jphilli85.deviceinfo.element.Ram;
import com.jphilli85.deviceinfo.element.Sensors.SensorWrapper;
import com.jphilli85.deviceinfo.element.Storage;
import com.jphilli85.deviceinfo.element.Wifi;

public class DetailsFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<Cursor>,
		Battery.Callback, Graphics.Callback, Location.ProviderCallback {
	
	private static final int SUBGROUP_LOADER = 1;
	
	private LinearLayout mLayout;
	private Map<String, String> mSubgroups;
	
	private Audio mAudio;
	private Battery mBattery;
	private Camera mCamera;
	private Cpu mCpu;
	private Display mDisplay;
	private Graphics mGraphics;
	private Location mLocation;
	private Ram mRam;
//	private Sensors mSensors;
	private Storage mStorage;
	private Cellular mCellular;
	private Network mNetwork;
	private Wifi mWifi;
	
	private boolean mIsPaused;
	
	private TextView mLiveBatteryInfo;
	private TextView mLiveCpuInfo;
	private TextView mLiveLocationInfo;
	private TextView mLiveRamInfo;	
	private TextView mLiveStorageInfo;
	
	private SensorsView mSensorsView;
	
	/**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailsFragment newInstance(int index) {
        DetailsFragment f = new DetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }
	
    public int getShownIndex() {
    	Bundle args = getArguments();
        return args != null ? args.getInt("index", 1) : 1;
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setRetainInstance(true);
		mSubgroups = new HashMap<String, String>();
		getLoaderManager().initLoader(SUBGROUP_LOADER, null, this);		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) return null;
	
		View v = inflater.inflate(R.layout.details, container, false);
		mLayout = (LinearLayout) v.findViewById(R.id.detailsLayout);
		if (mLayout != null) {
			mLiveBatteryInfo = new DetailsTextView(getActivity());
			mLiveCpuInfo = new DetailsTextView(getActivity());
			mLiveLocationInfo = new DetailsTextView(getActivity());
			mLiveRamInfo = new DetailsTextView(getActivity());
//			mLiveSensorsInfo = new DetailsTextView(getActivity());
			mLiveStorageInfo = new DetailsTextView(getActivity());
			mLayout.addView(mLiveBatteryInfo);
			mLayout.addView(mLiveCpuInfo);
			mLayout.addView(mLiveLocationInfo);
			mLayout.addView(mLiveRamInfo);
//			mLayout.addView(mLiveSensorsInfo);
			mLayout.addView(mLiveStorageInfo);
		}
		return v; 		
	}
	
	@Override
	public void onResume() {	
		super.onResume();
		mIsPaused = false;
		if (mGraphics != null) mGraphics.onResume();
//		if (mBattery != null) mBattery.startListening();		
//		if (mLocation != null) mLocation.startListening();
		if (mSensorsView != null) mSensorsView.onResume();
		if (mWifi != null) mWifi.resume();
		mIsPaused = false;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mGraphics != null) mGraphics.onPause();
		if (mBattery != null) mBattery.stopListening();		
		if (mLocation != null) mLocation.stopListeningAll();
		if (mSensorsView != null) mSensorsView.onPause();
		if (mCellular != null) mCellular.stopListening();
		if (mWifi != null) mWifi.pause();
		mIsPaused = true;
	}
	
	@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
        	//Subgroup.TABLE_NAME +"."+ Subgroup.COL_ID,
    		Subgroup.TABLE_NAME +"."+ Subgroup.COL_NAME, 
    		Subgroup.TABLE_NAME +"."+ Subgroup.COL_LABEL 
		};

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                Uri.parse(Group.CONTENT_URI.toString() 
                		+ "/" + String.valueOf(getShownIndex())), 
        		projection, 
                //Group.COL_NAME + "="	+ String.valueOf(getShownIndex())
                null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    	if (cursor == null) return;
    	if (DeviceInfo.DEBUG) dumpResult(cursor);
    	if (cursor.moveToFirst()) {  
	    	do {
	    		// name, label
	    		mSubgroups.put(cursor.getString(0), cursor.getString(1));
	    	} while(cursor.moveToNext());   
	    	loadSubgroups();
    	}
    	cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       // Empty
    }
    
    
    private void loadSubgroups() {
    	for (String name : mSubgroups.keySet()) {
    		loadSubgroup(name);
    	}
    	showContents();
    }
    
    private void loadSubgroup(final String name) {
    	if (mLayout == null) return;
    	if (name.equals(Subgroup.SUBGROUP_CPU)) {
    		mCpu = new Cpu();
    		mCpu.updateCpuStats();    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_DISPLAY)) {
    		mDisplay = new Display(getActivity());    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_GRAPHICS)) {
        	GLSurfaceView glSurfaceView = new GLSurfaceView(getActivity());
        	mGraphics = new Graphics(glSurfaceView, this);
//    		Callback listener = new Callback() {
//				@Override
//				public void onGLSurfaceViewCreated() {
//					showContents(graphics, name);
//				}
//			};
        	// TODO maybe pausing the surfaceview in this fragment is needed.
    		if (mLayout != null) mLayout.addView(glSurfaceView);
    	}
    	else if (name.equals(Subgroup.SUBGROUP_RAM)) {
    		mRam = new Ram();    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_STORAGE)) {
    		mStorage = new Storage();    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_AUDIO)) {
    		mAudio = new Audio(getActivity());    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_CAMERA)) {
    		mCamera = new Camera(getActivity());    		
    	}
//    	else if (name.equals(Subgroup.SUBGROUP_BATTERY)) {
//    		mBattery = new Battery(getActivity(), this);
////    		battery.getReceiver().setOnBatteryChangedListener(new OnBatteryChangedListener() {				
////				@Override
////				public void onBatteryChanged() {
////					// Called on UI thread										
////					showContents(battery, name);
////					ImageView iv = new ImageView(getActivity());
////					iv.setImageResource(battery.getReceiver().getIconResourceId());
////					if (mLayout != null) mLayout.addView(iv);
////				}
////			});
//    		if (!mIsPaused) mBattery.startListening();
//    	}
    	else if (name.equals(Subgroup.SUBGROUP_SENSORS)) {
//    		mSensorMap = new HashMap<SensorWrapper, ViewGroup>();
//    		mSensors = new Sensors(getActivity());
////    		View v;
//    		
//    		SensorView sv;
//    		for (SensorWrapper sw : mSensors.getAllSensors()) {
//    			sv = new SensorView(getActivity(), sw);
//    			mSensorViews.add(sv);
//    			mLayout.addView(sv.getLayoutWrapper());
//    			if (!mIsPaused) sw.startListening();
//    		}
    		/*mSensorsView = new SensorsView(getActivity());
    		mLayout.addView(mSensorsView.getLayoutWrapper());    */		
//    		if (!mIsPaused) mSensorsView.getSensors().startListening();
    	
    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_GPS)) {
    		mLocation = new Location(getActivity());	 
    		if (!mIsPaused) mLocation.startListeningAll(false);
    	}    	
//    	else if (name.equals(Subgroup.SUBGROUP_MOBILE)) {
//    		mCellular = new Cellular(getActivity());	 
//    		if (!mIsPaused) mCellular.startListening(false);
//    	}
    	else if (name.equals(Subgroup.SUBGROUP_WIFI)) {
//    		mNetwork = new Network(getActivity());	 
    		
    		mWifi = new Wifi(getActivity());
    		mWifi.getWifiManager().startScan();
    		
//    		if (!mIsPaused) mCellular.startListening(false);
    	}
    	
//    	if (unit != null && !(unit instanceof Graphics) 
//    			 && !(unit instanceof Battery)) {
//	    	showContents(unit, name);
//    	}
    }
    
    private void showContents(/*final Unit unit, final String name*/) {
    	if (mLayout == null) return;		
		mLayout.postDelayed(new Runnable() {			
			@Override
			public void run() {		
				Map<String, String> contents = new LinkedHashMap<String, String>();				
				if (mAudio != null) contents.putAll(mAudio.getContents());
				if (mBattery != null) contents.putAll(mBattery.getContents());
				if (mCamera != null) contents.putAll(mCamera.getContents());
				if (mCpu != null) contents.putAll(mCpu.getContents());
				if (mDisplay != null) contents.putAll(mDisplay.getContents());
				if (mGraphics != null) contents.putAll(mGraphics.getContents());
				if (mLocation != null) contents.putAll(mLocation.getContents());
				if (mRam != null) contents.putAll(mRam.getContents());
//				if (mSensors != null) contents.putAll(mSensors.getContents());
				if (mStorage != null) contents.putAll(mStorage.getContents());
				if (mCellular != null) contents.putAll(mCellular.getContents());
				if (mNetwork != null) contents.putAll(mNetwork.getContents());
				if (mWifi != null) contents.putAll(mWifi.getContents());
				
				TextView tv = new DetailsTextView(getActivity().getApplicationContext(), null);
				for (String s : contents.keySet()) {
					tv.append(s + ": " + contents.get(s) + "\n");
				}				
				mLayout.addView(tv);
			}
		}, 10000);
    }
    
    private void dumpResult(Cursor c) {    	
    	if (c == null || !c.moveToFirst() || mLayout == null) return;    	   
    	
    	String rows = getShownIndex() + "\n";
    	
    	for (int i = 0; i < c.getColumnCount(); ++i) {
			rows += c.getColumnName(i) + "|";
		}
    	rows += "\n";
    	do {    		
    		for (int i = 0; i < c.getColumnCount(); ++i) {
    			rows += c.getString(i) + "|";
    		}
    		rows += "\n";
    	} while (c.moveToNext());     	    	
		mLayout.addView(new DetailsTextView(getActivity(), rows));
    }
    
    
    
    // TODO Map<SensorWrapper, TextView> textViewMap;
    public void setLiveSensorInfo(SensorWrapper sw) {
    	String type;
    	String accuracyStatus;
    	int accuracy;
    	long timestamp;
    	float[] values;
//    	SensorWrapper sw = mSensors.getSensor(index);
//    	TextView tv = mLiveSensorsInfo.get(index);
    	
//    	mLiveSensorsInfo.append(mSensors.getAbsoluteHumidity() + "\n" + mSensors.getDewPoint() + "\n");
//    	values = mSensors.getOrientationInWorldCoordinateSystem();
//    	if (values != null) {
//    		for (float v : values) {
//    			mLiveSensorsInfo.append(v + "\n");
//    		}
//		}
//    	
//		type = sw.getTypeString();
//		accuracyStatus = sw.getLastAccuracyStatusString();
//		accuracy = sw.getLastAccuracy();
//		timestamp = sw.getLastEventTimestamp();
//		values = sw.getLastValues();
//		
//		tv.setText(type + "\n" + timestamp + "\n" + accuracy + "\n" + accuracyStatus + "\n");
//		if (values != null) {
//    		for (float v : values) {
//    			tv.append(v + "\n");
//    		}
//		}
    	
    }

//	@Override
//	public void onAccuracyChanged(SensorWrapper sw) {
//		//setLiveSensorInfo(sw);
//	}
//
//	@Override
//	public void onSensorChanged(SensorWrapper sw) {
//		//setLiveSensorInfo(sw);
//		float[] values = sw.getLastValues();
//		if (values == null) return;
//		((TextView) mSensorMap.get(sw).findViewById(R.id.value0TextView)).setText(String.valueOf(values[0]));
//		((TextView) mSensorMap.get(sw).findViewById(R.id.value1TextView)).setText(String.valueOf(values[1]));
//		((TextView) mSensorMap.get(sw).findViewById(R.id.value2TextView)).setText(String.valueOf(values[2]));
//	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(ProviderWrapper providerWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(ProviderWrapper providerWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(ProviderWrapper providerWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(ProviderWrapper providerWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddressChanged(ProviderWrapper providerWrapper) {
		// TODO Auto-generated method stub
		
	}  
}
