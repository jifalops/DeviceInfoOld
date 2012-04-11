package com.jphilli85.deviceinfo.app.component;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.element.Sensors;
import com.jphilli85.deviceinfo.element.Sensors.SensorWrapper;

public class SensorsView {
	public static final int FLAG_PROPERTIES = 0x1;
	public static final int FLAG_EVENTS = 0x2;
	
	private final Sensors mSensors;
	
	private final ViewGroup mOuterLayout;
	private final ViewGroup mMotionGroup;
	private final ViewGroup mPositionGroup;
	private final ViewGroup mEnvironmentGroup;
	
	private final SensorView[] mSensorViews;
	
	public SensorsView(Context context) {
		mSensors = new Sensors(context);
		
		mOuterLayout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.sensor_views_wrapper, null);
		mMotionGroup = (ViewGroup) mOuterLayout.findViewById(R.id.categoryMotionWrapper);
		mPositionGroup = (ViewGroup) mOuterLayout.findViewById(R.id.categoryPositionWrapper);
		mEnvironmentGroup = (ViewGroup) mOuterLayout.findViewById(R.id.categoryEnvironmentWrapper);
		
		((TextView) mOuterLayout.findViewById(R.id.sensorsTitleTextView))
			.setText(R.string.sensors_title);
		((TextView) mOuterLayout.findViewById(R.id.categoryMotionTextView))
		.setText(R.string.sensor_category_motion);
		((TextView) mOuterLayout.findViewById(R.id.categoryPositionTextView))
		.setText(R.string.sensor_category_position);
		((TextView) mOuterLayout.findViewById(R.id.categoryEnvironmentTextView))
		.setText(R.string.sensor_category_environment);
		
		List<SensorView> list = new ArrayList<SensorView>();
		SensorView sv;
		for (SensorWrapper sw : mSensors.getSensorsByCategory(Sensors.CATEGORY_MOTION)) {
			sv = new SensorView(context, sw);
			mMotionGroup.addView(sv.getLayoutWrapper());
			list.add(sv);
		}
		for (SensorWrapper sw : mSensors.getSensorsByCategory(Sensors.CATEGORY_POSITION)) {
			sv = new SensorView(context, sw);
			mPositionGroup.addView(sv.getLayoutWrapper());
			list.add(sv);
		}
		for (SensorWrapper sw : mSensors.getSensorsByCategory(Sensors.CATEGORY_ENVIRONMENT)) {
			sv = new SensorView(context, sw);
			mEnvironmentGroup.addView(sv.getLayoutWrapper());
			list.add(sv);
		}
		mSensorViews = list.toArray(new SensorView[list.size()]);
	}
	
	public Sensors getSensors() {
		return mSensors;
	}
	
	public SensorView[] getSensorViews() { 
		return mSensorViews;
	}
	
	public ViewGroup getLayoutWrapper() {
		return mOuterLayout;
	}
	
	public ViewGroup getMotionsWrapper() {
		return mMotionGroup;
	}
	
	public ViewGroup getPositionsWrapper() {
		return mPositionGroup;
	}
	
	public ViewGroup getEnvironmentsWrapper() {
		return mEnvironmentGroup;
	}
	
	public void setDetails(int flags) {
		for (SensorView sv : mSensorViews) {
			sv.setDetails(flags);
		}
	}

	public static class SensorView implements Sensors.Callback {
		private static final String LOG_TAG = SensorView.class.getSimpleName();
		private static final int API = Build.VERSION.SDK_INT;
		
		private final SensorWrapper mSensor;
		
		private final ViewGroup mLayout;
		private final ViewGroup mProperties;
		private final ViewGroup mLiveValues;
		
		private TextView mValue0TextView;
		private TextView mValue1TextView;
		private TextView mValue2TextView;
		private TextView mValue3TextView;
		private TextView mTimestampTextView;
		private TextView mAccuracyTextView;
		
		private int mFlags;
		private boolean mHasInitProperties;
		private boolean mHasInitEvents;
		
		public SensorView(Context context, SensorWrapper sw) {	
			mSensor = sw;
			
			mLayout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.sensor_view, null);		
			mProperties = (ViewGroup) mLayout.findViewById(R.id.propertiesWrapper);
			mLiveValues = (ViewGroup) mLayout.findViewById(R.id.liveValuesWrapper);
			
			String def = "";
			if (sw.isDefaultForType()) def = " (" + context.getString(R.string.sensor_default) + ")";
			((TextView) mLayout.findViewById(R.id.typeTextView)).setText(sw.getTypeString() + def);		
			
			setDetails(FLAG_PROPERTIES | FLAG_EVENTS);
		}
		
		public void setDetails(int flags) {
			mFlags = flags;
			
			if ((flags & FLAG_PROPERTIES) > 0) {
				if (!mHasInitProperties) {
					initProperties();
				}
				mProperties.setVisibility(View.VISIBLE);
			}
			else {
				mProperties.setVisibility(View.GONE);
			}
			
			if ((flags & FLAG_EVENTS) > 0) {
				if (!mHasInitEvents) {
					initEvents();
				}
				mLiveValues.setVisibility(View.VISIBLE);
				mSensor.setCallback(this);
			}
			else {
				mLiveValues.setVisibility(View.GONE);
				mSensor.setCallback(null);
			}
		}
		
		public int getDetailsFlags() {
			return mFlags;
		}				
		
		private void initProperties() {
			Context context = mLayout.getContext();
			
			((TextView) mLayout.findViewById(R.id.nameLabelTextView)).setText(
					context.getString(R.string.sensor_name));
			((TextView) mLayout.findViewById(R.id.vendorLabelTextView)).setText(
					context.getString(R.string.sensor_vendor));
			((TextView) mLayout.findViewById(R.id.versionLabelTextView)).setText(
					context.getString(R.string.sensor_version));
			((TextView) mLayout.findViewById(R.id.powerLabelTextView)).setText(
					context.getString(R.string.sensor_power) 
					+ " (" + context.getString(R.string.unit_milli_amps) + ")");
			((TextView) mLayout.findViewById(R.id.resolutionLabelTextView)).setText(
					context.getString(R.string.sensor_resolution) 
					+ " (" + mSensor.getUnit() + ")");
			((TextView) mLayout.findViewById(R.id.maxRangeLabelTextView)).setText(
					context.getString(R.string.sensor_max_range) 
					+ " (" + mSensor.getUnit() + ")");
			((TextView) mLayout.findViewById(R.id.minDelayLabelTextView)).setText(
					context.getString(R.string.sensor_min_delay) 
					+ " (" + context.getString(R.string.unit_micro_seconds) + ")");
			
			((TextView) mLayout.findViewById(R.id.nameTextView)).setText(mSensor.getName());
			((TextView) mLayout.findViewById(R.id.vendorTextView)).setText(mSensor.getVendor());
			((TextView) mLayout.findViewById(R.id.versionTextView)).setText(String.valueOf(mSensor.getVersion()));
			((TextView) mLayout.findViewById(R.id.powerTextView)).setText(String.valueOf(mSensor.getPower()));
			((TextView) mLayout.findViewById(R.id.resolutionTextView)).setText(String.valueOf(mSensor.getResolution()));
			((TextView) mLayout.findViewById(R.id.maxRangeTextView)).setText(String.valueOf(mSensor.getMaximumRange()));
			((TextView) mLayout.findViewById(R.id.minDelayTextView)).setText(String.valueOf(mSensor.getMinDelay()));
			
			mHasInitProperties = true;
		}
		
		private void initEvents() {
			Context context = mLayout.getContext();
			
			mValue0TextView = (TextView) mLayout.findViewById(R.id.value0TextView);
			mValue1TextView = (TextView) mLayout.findViewById(R.id.value1TextView);
			mValue2TextView = (TextView) mLayout.findViewById(R.id.value2TextView);
			mValue3TextView = (TextView) mLayout.findViewById(R.id.value3TextView);
			mTimestampTextView = (TextView) mLayout.findViewById(R.id.timestampTextView);
			mAccuracyTextView = (TextView) mLayout.findViewById(R.id.accuracyTextView);
			
			((TextView) mLayout.findViewById(R.id.timestampLabelTextView)).setText(
					context.getString(R.string.sensor_value_timestamp) 
					+ " (" + context.getString(R.string.unit_nano_seconds) + ")");
			((TextView) mLayout.findViewById(R.id.accuracyLabelTextView)).setText(
					R.string.sensor_value_accuracy);		
			
			TextView v0 = (TextView) mLayout.findViewById(R.id.value0LabelTextView);
			TextView v1 = (TextView) mLayout.findViewById(R.id.value1LabelTextView); 
			TextView v2 = (TextView) mLayout.findViewById(R.id.value2LabelTextView); 
			TextView v3 = (TextView) mLayout.findViewById(R.id.value3LabelTextView);		
			int type = mSensor.getType();		
			if (type == Sensor.TYPE_ACCELEROMETER) { 
				v0.setText(R.string.sensor_value_x);
				v0.append(" (" + mSensor.getUnit() + ")");
				v1.setText(R.string.sensor_value_y);
				v1.append(" (" + mSensor.getUnit() + ")");
				v2.setText(R.string.sensor_value_z);
				v2.append(" (" + mSensor.getUnit() + ")");
				v3.setVisibility(View.GONE);
				mValue3TextView.setVisibility(View.GONE);
			}
	        else if (API >= 14 && type == Sensor.TYPE_AMBIENT_TEMPERATURE) { 
				v0.setText(R.string.sensor_value_value);
	        	v0.append(" (" + mSensor.getUnit() + ")");
				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				v3.setVisibility(View.GONE);
				mValue1TextView.setVisibility(View.GONE);
				mValue2TextView.setVisibility(View.GONE);
				mValue3TextView.setVisibility(View.GONE);
			}
	        else if (API >= 9 && type == Sensor.TYPE_GRAVITY) { 
				v0.setText(R.string.sensor_value_x);
				v0.append(" (" + mSensor.getUnit() + ")");
				v1.setText(R.string.sensor_value_y);
				v1.append(" (" + mSensor.getUnit() + ")");
				v2.setText(R.string.sensor_value_z);
				v2.append(" (" + mSensor.getUnit() + ")");
				v3.setVisibility(View.GONE);
				mValue3TextView.setVisibility(View.GONE);
			}
	        else if (type == Sensor.TYPE_GYROSCOPE) { 			 
	        	v0.setText(R.string.sensor_value_angular_speed);
	        	v0.append(" " + context.getString(R.string.sensor_value_x));
				v0.append(" (" + mSensor.getUnit() + ")");
				v1.setText(R.string.sensor_value_angular_speed);
	        	v1.append(" " + context.getString(R.string.sensor_value_y));
				v1.append(" (" + mSensor.getUnit() + ")");
				v2.setText(R.string.sensor_value_angular_speed);
	        	v2.append(" " + context.getString(R.string.sensor_value_z));
				v2.append(" (" + mSensor.getUnit() + ")");
				v3.setVisibility(View.GONE);
				mValue3TextView.setVisibility(View.GONE);
			}
	        else if (type == Sensor.TYPE_LIGHT) { 
	        	v0.setText(R.string.sensor_value_value);
	        	v0.append(" (" + mSensor.getUnit() + ")");
				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				v3.setVisibility(View.GONE);
				mValue1TextView.setVisibility(View.GONE);
				mValue2TextView.setVisibility(View.GONE);
				mValue3TextView.setVisibility(View.GONE);
			}
	        else if (API >= 9 && type == Sensor.TYPE_LINEAR_ACCELERATION) { 
				 v0.setText(R.string.sensor_value_x);
				 v0.append(" (" + mSensor.getUnit() + ")");
				 v1.setText(R.string.sensor_value_y);
				 v1.append(" (" + mSensor.getUnit() + ")");
				 v2.setText(R.string.sensor_value_z);
				 v2.append(" (" + mSensor.getUnit() + ")");
				 v3.setVisibility(View.GONE);
				 mValue3TextView.setVisibility(View.GONE);
			}
	        else if (type == Sensor.TYPE_MAGNETIC_FIELD) { 
				 v0.setText(R.string.sensor_value_x);
				 v0.append(" (" + mSensor.getUnit() + ")");
				 v1.setText(R.string.sensor_value_y);
				 v1.append(" (" + mSensor.getUnit() + ")");
				 v2.setText(R.string.sensor_value_z);
				 v2.append(" (" + mSensor.getUnit() + ")");
				 v3.setVisibility(View.GONE);
				 mValue3TextView.setVisibility(View.GONE);
			}
	        else if (type == Sensor.TYPE_ORIENTATION) { 
				 v0.setText(R.string.sensor_value_azimuth);
				 v0.append(" (" + mSensor.getUnit() + ")");
				 v1.setText(R.string.sensor_value_pitch);
				 v1.append(" (" + mSensor.getUnit() + ")");
				 v2.setText(R.string.sensor_value_roll);
				 v2.append(" (" + mSensor.getUnit() + ")");
				 v3.setVisibility(View.GONE);
				 mValue3TextView.setVisibility(View.GONE);
			}
	        else if (type == Sensor.TYPE_PRESSURE) { 
	        	v0.setText(R.string.sensor_value_value);
	        	v0.append(" (" + mSensor.getUnit() + ")");
				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				v3.setVisibility(View.GONE);
				mValue1TextView.setVisibility(View.GONE);
				mValue2TextView.setVisibility(View.GONE);
				mValue3TextView.setVisibility(View.GONE);
			}
	        else if (type == Sensor.TYPE_PROXIMITY) { 
	        	v0.setText(R.string.sensor_value_value);
	        	v0.append(" (" + mSensor.getUnit() + ", " + context.getString(R.string.sensor_proximity_disclaimer) + ")");
				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				v3.setVisibility(View.GONE);
				mValue1TextView.setVisibility(View.GONE);
				mValue2TextView.setVisibility(View.GONE);
				mValue3TextView.setVisibility(View.GONE);
			}
	        else if (API >= 14 && type == Sensor.TYPE_RELATIVE_HUMIDITY) { 
	        	v0.setText(R.string.sensor_value_value);
	        	v0.append(" (" + mSensor.getUnit() + ")");
				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				v3.setVisibility(View.GONE);
				mValue1TextView.setVisibility(View.GONE);
				mValue2TextView.setVisibility(View.GONE);
				mValue3TextView.setVisibility(View.GONE);
			}
	        else if (API >= 9 && type == Sensor.TYPE_ROTATION_VECTOR) { 
				 v0.setText(R.string.sensor_value_rotation_x);
				 v0.append(" (" + mSensor.getUnit() + ")");
				 v1.setText(R.string.sensor_value_rotation_y);
				 v1.append(" (" + mSensor.getUnit() + ")");
				 v2.setText(R.string.sensor_value_rotation_z);
				 v2.append(" (" + mSensor.getUnit() + ")");
				 v3.setText(R.string.sensor_value_rotation_extra);
				 v3.append(" (" + mSensor.getUnit() + ")");			 
			}
	        else if (type == Sensor.TYPE_TEMPERATURE) { 
	        	v0.setText(R.string.sensor_value_value);
	        	v0.append(" (" + mSensor.getUnit() + ")");
				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				v3.setVisibility(View.GONE);
				mValue1TextView.setVisibility(View.GONE);
				mValue2TextView.setVisibility(View.GONE);
				mValue3TextView.setVisibility(View.GONE);
			}
			
			mHasInitEvents = true;
		}
		
		public SensorWrapper getSensor() { 
			return mSensor;
		}
		
		public ViewGroup getLayoutWrapper() {
			return mLayout;
		}
		
		public ViewGroup getPropertiesWrapper() {
			return mProperties;
		}
		
		public ViewGroup getLiveValuesWrapper() {
			return mLiveValues;
		}
		
		
		@Override
		public void onAccuracyChanged(SensorWrapper sw) {		
			mAccuracyTextView.setText(sw.getAccuracyString(sw.getLastAccuracyStatus()));
			Log.i(LOG_TAG, "onAccuracyChanged called on " + sw.getTypeString());
		}
	
		@Override
		public void onSensorChanged(SensorWrapper sw) {		
			int type = sw.getType();		
			if (type == Sensor.TYPE_ACCELEROMETER) { 
				mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
				mValue1TextView.setText(String.valueOf(sw.getLastValue(1)));
				mValue2TextView.setText(String.valueOf(sw.getLastValue(2)));
			}
	        else if (API >= 14 && type == Sensor.TYPE_AMBIENT_TEMPERATURE) { 
				mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
			}
	        else if (API >= 9 && type == Sensor.TYPE_GRAVITY) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
				mValue1TextView.setText(String.valueOf(sw.getLastValue(1)));
				mValue2TextView.setText(String.valueOf(sw.getLastValue(2)));
			}
	        else if (type == Sensor.TYPE_GYROSCOPE) { 			 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
				mValue1TextView.setText(String.valueOf(sw.getLastValue(1)));
				mValue2TextView.setText(String.valueOf(sw.getLastValue(2)));
			}
	        else if (type == Sensor.TYPE_LIGHT) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
			}
	        else if (API >= 9 && type == Sensor.TYPE_LINEAR_ACCELERATION) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
				mValue1TextView.setText(String.valueOf(sw.getLastValue(1)));
				mValue2TextView.setText(String.valueOf(sw.getLastValue(2)));
			}
	        else if (type == Sensor.TYPE_MAGNETIC_FIELD) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
				mValue1TextView.setText(String.valueOf(sw.getLastValue(1)));
				mValue2TextView.setText(String.valueOf(sw.getLastValue(2)));
			}
	        else if (type == Sensor.TYPE_ORIENTATION) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
				mValue1TextView.setText(String.valueOf(sw.getLastValue(1)));
				mValue2TextView.setText(String.valueOf(sw.getLastValue(2)));
			}
	        else if (type == Sensor.TYPE_PRESSURE) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
			}
	        else if (type == Sensor.TYPE_PROXIMITY) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
			}
	        else if (API >= 14 && type == Sensor.TYPE_RELATIVE_HUMIDITY) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
			}
	        else if (API >= 9 && type == Sensor.TYPE_ROTATION_VECTOR) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
				mValue1TextView.setText(String.valueOf(sw.getLastValue(1)));
				mValue2TextView.setText(String.valueOf(sw.getLastValue(2)));
				mValue3TextView.setText(String.valueOf(sw.getLastValue(3)));
			}
	        else if (type == Sensor.TYPE_TEMPERATURE) { 
	        	mValue0TextView.setText(String.valueOf(sw.getLastValue(0)));
			}
			
			mAccuracyTextView.setText(sw.getAccuracyString(sw.getLastAccuracy()));
			mTimestampTextView.setText(String.valueOf(sw.getLastEventTimestamp()));
		}
	}

}