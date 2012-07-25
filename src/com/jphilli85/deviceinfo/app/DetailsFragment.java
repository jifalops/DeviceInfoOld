package com.jphilli85.deviceinfo.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.element.view.AbstractElementView;
import com.jphilli85.deviceinfo.element.view.AudioView;
import com.jphilli85.deviceinfo.element.view.BatteryView;
import com.jphilli85.deviceinfo.element.view.BluetoothView;
import com.jphilli85.deviceinfo.element.view.CameraView;
import com.jphilli85.deviceinfo.element.view.CellularView;
import com.jphilli85.deviceinfo.element.view.CpuView;
import com.jphilli85.deviceinfo.element.view.DisplayView;
import com.jphilli85.deviceinfo.element.view.ElementView;
import com.jphilli85.deviceinfo.element.view.FeaturesView;
import com.jphilli85.deviceinfo.element.view.GraphicsView;
import com.jphilli85.deviceinfo.element.view.IdentifiersView;
import com.jphilli85.deviceinfo.element.view.KeysView;
import com.jphilli85.deviceinfo.element.view.ListeningElementView;
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

public class DetailsFragment extends SherlockFragment {
	
	public static final String KEY_ELEMENTS = DetailsFragment.class.getName() + ".ELEMENTS";
	
	private int[] mElements;
	private ElementView[] mElementViews;
	
	private ScrollView mScroller;
	private LinearLayout mLayout;
	
	// Will only work correctly if expand/collapses are saved
	// This wasn't working with instance state and isn't important
	// enough to bother managing its state persistently
	private static int mScrollPos;
	
    public static DetailsFragment newInstance(int[] elements) {    	
        DetailsFragment f = new DetailsFragment();
        f.setElements(elements);
        return f;
    }
    
    public int[] getElements() {
        return mElements;
    }
    
    private void setElements(int[] elements) { 
    	if (elements == null || elements.length == 0) {
        	elements = GroupListFragment.getElements(0);
        }
    	mElements = elements;
    	mElementViews = new ElementView[elements.length];
    }
	
	public final boolean add(int elementIndex) {	
		
		
		return true;
	}
    
    private void loadElements() {
    	if (mLayout == null) return;
//    	Class<? extends AbstractElementView> ev = null;
    	for (int i = 0; i < mElements.length; ++i) {
    		switch (mElements[i]) {
    		case 0: break;
    		case 1: mElementViews[i] = new AudioView(getActivity());	break;
    		case 2:  mElementViews[i] = new BatteryView(getActivity());	break;
    		case 3:  mElementViews[i] = new BluetoothView(getActivity());	break;
    		case 4:  mElementViews[i] = new CameraView(getActivity());	break;
    		case 5:  mElementViews[i] = new CellularView(getActivity());	break;
    		case 6:  mElementViews[i] = new CpuView(getActivity());	break;
    		case 7:  mElementViews[i] = new DisplayView(getActivity());	break;
    		case 8:  mElementViews[i] = new FeaturesView(getActivity());	break;
    		case 9:  mElementViews[i] = new GraphicsView(getActivity());	break;
    		case 10:  mElementViews[i] = new IdentifiersView(getActivity());	break;
    		case 11:  mElementViews[i] = new KeysView(getActivity());	break;
    		case 12:  mElementViews[i] = new LocationView(getActivity());	break;
    		case 13:  mElementViews[i] = new NetworkView(getActivity());	break;
    		case 14:  mElementViews[i] = new PlatformView(getActivity());	break;
    		case 15:  mElementViews[i] = new PropertiesView(getActivity());	break;
    		case 16:  mElementViews[i] = new RamView(getActivity());	break;
    		case 17:  mElementViews[i] = new SensorsView(getActivity());	break;
    		case 18:  mElementViews[i] = new StorageView(getActivity());	break;
    		case 19:  mElementViews[i] = new UptimeView(getActivity());	break;
    		case 20:  mElementViews[i] = new WifiView(getActivity());	break;
    		}
    		if (mElementViews[i] != null) mElementViews[i].addToLayout(mLayout);
    		
//    		ev = DeviceInfo.getAbstractView(mElements[i]);
//    		if (ev == null) continue;
//    		
//    		try { 
//    			mElementViews[i] = ((ElementView) ev.newInstance());
//    			mElementViews[i].addToLayout(mLayout);
//			} 
//    		catch (java.lang.InstantiationException e) {} 
//    		catch (IllegalAccessException e) {} 
//    		catch (ClassCastException e) {}     		
    	}
    	    	
    	
//    	mSensorsView = new SensorsView(getActivity());
    	
//    	mLayout.addView(mSensorsView.getLayoutWrapper());
    }
    
    
    private void pauseElements() {
    	if (mElementViews == null) return;
    	for (ElementView ev : mElementViews) {
    		if (ev == null) continue;
    		ev.onActivityPause();
    	}
    	
    	
//		if (mGraphics != null) mGraphics.onPause();
//		if (mBattery != null) mBattery.stopListening();		
//		if (mLocation != null) mLocation.stopListeningAll();
//		if (mCellular != null) mCellular.stopListening();
//		if (mWifi != null) mWifi.pause();
//		if (mBluetooth != null) mBluetooth.pause();
//		if (mUptime != null) mUptime.pause();
    	
//		if (mSensorsView != null) mSensorsView.onPause();
    }
    
    private void resumeElements() {
    	if (mElementViews == null) return;
    	for (ElementView ev : mElementViews) {
    		if (ev == null) continue;
    		ev.onActivityResume();
    	}
    	
//		if (mGraphics != null) mGraphics.onResume();
//		if (mBattery != null) mBattery.startListening();		
//		if (mLocation != null) mLocation.startListening();
//		if (mWifi != null) mWifi.resume();
//		if (mSensorsView != null) mSensorsView.onResume();
		
		
    }
    
    private void restoreElements(Bundle state) {
//    	if (mSensorsView != null) mSensorsView.restoreState(state);
    }
    
    private void saveElements(Bundle outState) {
    	outState.putIntArray(KEY_ELEMENTS, mElements);
//    	outState.put
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mElements == null) {
			if (savedInstanceState == null) setElements(null);
			else {
				
				setElements(savedInstanceState.getIntArray(KEY_ELEMENTS));
			}
		}
		setHasOptionsMenu(true);
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) return null;
		mScroller = (ScrollView) inflater.inflate(R.layout.details, container, false);		
		mLayout = (LinearLayout) mScroller.getChildAt(0);	
		loadElements();
		return mScroller; 		
	}	
	
	@Override
	public void onActivityCreated(final Bundle state) {
		super.onActivityCreated(state);
		if (getActivity().isFinishing()) return;
//		setRetainInstance(true);
		restoreElements(state);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (getActivity().isFinishing()) return;
		saveElements(outState);
	}
	
	@Override
	public void onResume() {	
		super.onResume();
		if (mScroller == null || getActivity().isFinishing()) return;		
		resumeElements();
//		mScroller.post(new Runnable() {
//			@Override
//			public void run() {
//				mScroller.scrollTo(0, mScrollPos);
//			}
//		});
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		if (getActivity().isFinishing()) return;
		pauseElements();
//		mScrollPos = mScroller.getScrollY();
	}
	
//	@Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        String[] projection = {
//        	//Subgroup.TABLE_NAME +"."+ Subgroup.COL_ID,
//    		Subgroup.TABLE_NAME +"."+ Subgroup.COL_NAME, 
//    		Subgroup.TABLE_NAME +"."+ Subgroup.COL_LABEL 
//		};
//
//        CursorLoader cursorLoader = new CursorLoader(getActivity(),
//                Uri.parse(Group.CONTENT_URI.toString() 
//                		+ "/" + String.valueOf(getShownIndex())), 
//        		projection, 
//                //Group.COL_NAME + "="	+ String.valueOf(getShownIndex())
//                null, null, null);
//        return cursorLoader;
//    }

//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//    	if (cursor == null) return;
//    	if (DeviceInfo.DEBUG) dumpResult(cursor);
//    	if (cursor.moveToFirst()) {  
//	    	do {
//	    		// name, label
//	    		mSubgroups.put(cursor.getString(0), cursor.getString(1));
//	    	} while(cursor.moveToNext());   
//	    	loadSubgroups();
//    	}
//    	cursor.close();
//    }

//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//       // Empty
//    }
    
    
//    private void loadSubgroups() {
//    	for (String name : mSubgroups.keySet()) {
//    		loadSubgroup(name);
//    	}
//    	showContents();
//    }
//    
//    private void loadSubgroup(final String name) {
//    	if (mLayout == null) return;
//    	if (name.equals(Subgroup.SUBGROUP_CPU)) {
//    		mCpu = new Cpu();
//    		mCpu.updateCpuStats();    		
//    	}
//    	else if (name.equals(Subgroup.SUBGROUP_DISPLAY)) {
//    		mDisplay = new Display(getActivity());    		
//    	}
//    	else if (name.equals(Subgroup.SUBGROUP_GRAPHICS)) {
//        	GLSurfaceView glSurfaceView = new GLSurfaceView(getActivity());
//        	mGraphics = new Graphics(glSurfaceView, this);
////    		Callback listener = new Callback() {
////				@Override
////				public void onGLSurfaceViewCreated() {
////					showContents(graphics, name);
////				}
////			};
//        	// TODO maybe pausing the surfaceview in this fragment is needed.
//    		if (mLayout != null) mLayout.addView(glSurfaceView);
//    	}
//    	else if (name.equals(Subgroup.SUBGROUP_RAM)) {
//    		mRam = new Ram();    		
//    	}
//    	else if (name.equals(Subgroup.SUBGROUP_STORAGE)) {
//    		mStorage = new Storage();    		
//    	}
//    	else if (name.equals(Subgroup.SUBGROUP_AUDIO)) {
//    		mAudio = new Audio(getActivity());    		
//    	}
//    	else if (name.equals(Subgroup.SUBGROUP_CAMERA)) {
//    		mCamera = new Camera(getActivity());    		
//    	}
////    	else if (name.equals(Subgroup.SUBGROUP_BATTERY)) {
////    		mBattery = new Battery(getActivity(), this);
//////    		battery.getReceiver().setOnBatteryChangedListener(new OnBatteryChangedListener() {				
//////				@Override
//////				public void onBatteryChanged() {
//////					// Called on UI thread										
//////					showContents(battery, name);
//////					ImageView iv = new ImageView(getActivity());
//////					iv.setImageResource(battery.getReceiver().getIconResourceId());
//////					if (mLayout != null) mLayout.addView(iv);
//////				}
//////			});
////    		if (!mIsPaused) mBattery.startListening();
////    	}
//    	else if (name.equals(Subgroup.SUBGROUP_SENSORS)) {
////    		mSensorMap = new HashMap<SensorWrapper, ViewGroup>();
////    		mSensors = new Sensors(getActivity());
//////    		View v;
////    		
////    		SensorView sv;
////    		for (SensorWrapper sw : mSensors.getAllSensors()) {
////    			sv = new SensorView(getActivity(), sw);
////    			mSensorViews.add(sv);
////    			mLayout.addView(sv.getLayoutWrapper());
////    			if (!mIsPaused) sw.startListening();
////    		}
//    		/*mSensorsView = new SensorsView(getActivity());
//    		mLayout.addView(mSensorsView.getLayoutWrapper());    */		
////    		if (!mIsPaused) mSensorsView.getSensors().startListening();
//    	
//    		
//    	}
//    	else if (name.equals(Subgroup.SUBGROUP_GPS)) {
//    		mLocation = new Location(getActivity());	 
//    		if (!mIsPaused) mLocation.startListeningAll(false);
//    	}    	
////    	else if (name.equals(Subgroup.SUBGROUP_MOBILE)) {
////    		mCellular = new Cellular(getActivity());	 
////    		if (!mIsPaused) mCellular.startListening(false);
////    	}
//    	else if (name.equals(Subgroup.SUBGROUP_WIFI)) {
////    		mNetwork = new Network(getActivity());	 
//    		
////    		mWifi = new Wifi(getActivity());
//    		
//    		try { mBluetooth = new Bluetooth(getActivity()); } 
//    		catch (UnavailableFeatureException ignored) {}
//    		if (mBluetooth != null) {
//    			mBluetooth.startListening(false);
//    		}
//    	}
//    	else if (name.equals(Subgroup.SUBGROUP_PLATFORM)) {
//    		mUptime = new Uptime();
////    		mUptime.startListening(false);
//    		mPlatform = new Platform(getActivity());
//		}
//    	else if (name.equals(Subgroup.SUBGROUP_PROPERTIES)) {
//    		mProperties = new Properties();
//    		mFeatures = new Features(getActivity());
//		}
//    	else if (name.equals(Subgroup.SUBGROUP_IDENTIFIERS)) {
//    		mIdentifiers = new Identifiers(getActivity());
//    		try {
//				mKeys = new Keys(getActivity());
//			} catch (UnavailableFeatureException e) {
//				Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//			}
//		}
//    	
////    	if (unit != null && !(unit instanceof Graphics) 
////    			 && !(unit instanceof Battery)) {
////	    	showContents(unit, name);
////    	}
//    }
    
//    private void showContents(/*final Unit unit, final String name*/) {
//    	if (mLayout == null) return;		
//		mLayout.postDelayed(new Runnable() {			
//			@Override
//			public void run() {		
//				Map<String, String> contents = new LinkedHashMap<String, String>();				
//				if (mAudio != null) contents.putAll(mAudio.getContents());
//				if (mBattery != null) contents.putAll(mBattery.getContents());
//				if (mCamera != null) contents.putAll(mCamera.getContents());
//				if (mCpu != null) contents.putAll(mCpu.getContents());
//				if (mDisplay != null) contents.putAll(mDisplay.getContents());
//				if (mGraphics != null) contents.putAll(mGraphics.getContents());
//				if (mLocation != null) contents.putAll(mLocation.getContents());
//				if (mRam != null) contents.putAll(mRam.getContents());
////				if (mSensors != null) contents.putAll(mSensors.getContents());
//				if (mStorage != null) contents.putAll(mStorage.getContents());
//				if (mCellular != null) contents.putAll(mCellular.getContents());
//				if (mNetwork != null) contents.putAll(mNetwork.getContents());
//				if (mWifi != null) contents.putAll(mWifi.getContents());
//				if (mBluetooth != null) contents.putAll(mBluetooth.getContents());
//				if (mUptime != null) contents.putAll(mUptime.getContents());
//				if (mPlatform != null) contents.putAll(mPlatform.getContents());
//				if (mProperties != null) contents.putAll(mProperties.getContents());
//				if (mIdentifiers != null) contents.putAll(mIdentifiers.getContents());
//				if (mFeatures != null) contents.putAll(mFeatures.getContents());
//				if (mKeys != null) contents.putAll(mKeys.getContents());
//				
//				TextView tv = new DetailsTextView(getActivity().getApplicationContext(), null);
//				for (String s : contents.keySet()) {
//					tv.append(s + ": " + contents.get(s) + "\n");
//				}				
//				mLayout.addView(tv);
//			}
//		}, 10000);
//    }
    
//    private void dumpResult(Cursor c) {    	
//    	if (c == null || !c.moveToFirst() || mLayout == null) return;    	   
//    	
//    	String rows = getShownIndex() + "\n";
//    	
//    	for (int i = 0; i < c.getColumnCount(); ++i) {
//			rows += c.getColumnName(i) + "|";
//		}
//    	rows += "\n";
//    	do {    		
//    		for (int i = 0; i < c.getColumnCount(); ++i) {
//    			rows += c.getString(i) + "|";
//    		}
//    		rows += "\n";
//    	} while (c.moveToNext());     	    	
//		mLayout.addView(new DetailsTextView(getActivity(), rows));
//    }
    
    
    
    // TODO Map<SensorWrapper, TextView> textViewMap;
//    public void setLiveSensorInfo(SensorWrapper sw) {
//    	String type;
//    	String accuracyStatus;
//    	int accuracy;
//    	long timestamp;
//    	float[] values;
////    	SensorWrapper sw = mSensors.getSensor(index);
////    	TextView tv = mLiveSensorsInfo.get(index);
//    	
////    	mLiveSensorsInfo.append(mSensors.getAbsoluteHumidity() + "\n" + mSensors.getDewPoint() + "\n");
////    	values = mSensors.getOrientationInWorldCoordinateSystem();
////    	if (values != null) {
////    		for (float v : values) {
////    			mLiveSensorsInfo.append(v + "\n");
////    		}
////		}
////    	
////		type = sw.getTypeString();
////		accuracyStatus = sw.getLastAccuracyStatusString();
////		accuracy = sw.getLastAccuracy();
////		timestamp = sw.getLastEventTimestamp();
////		values = sw.getLastValues();
////		
////		tv.setText(type + "\n" + timestamp + "\n" + accuracy + "\n" + accuracyStatus + "\n");
////		if (values != null) {
////    		for (float v : values) {
////    			tv.append(v + "\n");
////    		}
////		}
//    	
//    }

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
}
