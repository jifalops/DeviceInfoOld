package com.jphilli85.deviceinfo.app;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.DeviceInfo.DetailsTextView;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.data.DeviceInfoContract.Group;
import com.jphilli85.deviceinfo.data.DeviceInfoContract.Subgroup;
import com.jphilli85.deviceinfo.element.Audio;
import com.jphilli85.deviceinfo.element.Battery;
import com.jphilli85.deviceinfo.element.Camera;
import com.jphilli85.deviceinfo.element.Cpu;
import com.jphilli85.deviceinfo.element.Display;
import com.jphilli85.deviceinfo.element.Graphics;
import com.jphilli85.deviceinfo.element.Graphics.Callback;
import com.jphilli85.deviceinfo.element.Location;
import com.jphilli85.deviceinfo.element.Ram;
import com.jphilli85.deviceinfo.element.Sensors;
import com.jphilli85.deviceinfo.element.Storage;

public class DetailsFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<Cursor>,
		Battery.Callback, Graphics.Callback, Location.Callback,
		Sensors.Callback {
	
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
	private Sensors mSensors;
	private Storage mStorage;
	
	private boolean mIsPaused;
	
	private TextView mLiveBatteryInfo;
	private TextView mLiveCpuInfo;
	private TextView mLiveLocationInfo;
	private TextView mLiveRamInfo;
	private TextView mLiveSensorsInfo;
	private TextView mLiveStorageInfo;

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
			mLiveSensorsInfo = new DetailsTextView(getActivity());
			mLiveStorageInfo = new DetailsTextView(getActivity());
			mLayout.addView(mLiveBatteryInfo);
			mLayout.addView(mLiveCpuInfo);
			mLayout.addView(mLiveLocationInfo);
			mLayout.addView(mLiveRamInfo);
			mLayout.addView(mLiveSensorsInfo);
			mLayout.addView(mLiveStorageInfo);
		}
		return v; 		
	}
	
	@Override
	public void onResume() {	
		super.onResume();
		mIsPaused = false;
		if (mGraphics != null) mGraphics.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mBattery != null) mBattery.stopListening();
		if (mGraphics != null) mGraphics.onPause();
		if (mLocation != null) mLocation.stopListening();
		if (mSensors != null) mSensors.stopListening();
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
    	if (!cursor.moveToFirst()) return;  
    	do {
    		// name, label
    		mSubgroups.put(cursor.getString(0), cursor.getString(1));
    	} while(cursor.moveToNext());   
    	loadSubgroups();
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
    	else if (name.equals(Subgroup.SUBGROUP_BATTERY)) {
    		mBattery = new Battery(getActivity(), this);
//    		battery.getReceiver().setOnBatteryChangedListener(new OnBatteryChangedListener() {				
//				@Override
//				public void onBatteryChanged() {
//					// Called on UI thread										
//					showContents(battery, name);
//					ImageView iv = new ImageView(getActivity());
//					iv.setImageResource(battery.getReceiver().getIconResourceId());
//					if (mLayout != null) mLayout.addView(iv);
//				}
//			});
    		if (!mIsPaused) mBattery.startListening();
    	}
    	else if (name.equals(Subgroup.SUBGROUP_SENSORS)) {
    		mSensors = new Sensors(getActivity(), this);	
    		if (!mIsPaused) mSensors.startListening();
    	}
    	else if (name.equals(Subgroup.SUBGROUP_GPS)) {
    		mLocation = new Location(getActivity(), this);	 
    		if (!mIsPaused) mLocation.startListening();
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
				if (mSensors != null) contents.putAll(mSensors.getContents());
				if (mStorage != null) contents.putAll(mStorage.getContents());

				TextView tv = new DetailsTextView(getActivity().getApplicationContext(), null);
				for (String s : contents.keySet()) {
					tv.append(s + ": " + contents.get(s) + "\n");
				}				
				mLayout.addView(tv);
			}
		}, 2000);
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

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		// TODO Auto-generated method stub
		
	}

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
    
   
}
