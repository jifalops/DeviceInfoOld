package com.jphilli85.deviceinfo.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import com.jphilli85.deviceinfo.R;

// TODO add microphone
public class Sensors extends ListeningElement {
	private static final String LOG_TAG = Sensors.class.getSimpleName();
	private static final int API = Build.VERSION.SDK_INT;
	
	public static final int CATEGORY_UNKNOWN = 0;
	public static final int CATEGORY_MOTION = 1;
	public static final int CATEGORY_POSITION = 2;
	public static final int CATEGORY_ENVIRONMENT = 3;
	
	public static final int FREQUENCY_HIGH = 100;
	public static final int FREQUENCY_MEDIUM = 200;
	public static final int FREQUENCY_LOW = 500;
	
	public interface Callback extends ListeningElement.Callback {
		/** Corresponds to SensorEventListener.onAccuracyChanged() */
		void onAccuracyChanged(SensorWrapper sensorWrapper);
		/** Corresponds to SensorEventListener.onSensorChanged() */
		void onSensorChanged(SensorWrapper sensorWrapper);		
	}
	
	private final SensorManager mSensorManager;
	
	// Sensor types
	private final List<AccelerometerSensor> mAccelerometerSensors;
	private final List<AmbientTemperatureSensor> mAmbientTemperatureSensors;
	private final List<GravitySensor> mGravitySensors;
	private final List<GyroscopeSensor> mGyroscopeSensors;
	private final List<LightSensor> mLightSensors;
	private final List<LinearAccelerationSensor> mLinearAccelerationSensors;
	private final List<MagneticFieldSensor> mMagneticFieldSensors;
	private final List<OrientationSensor> mOrientationSensors;
	private final List<PressureSensor> mPressureSensors;
	private final List<ProximitySensor> mProximitySensors;
	private final List<RelativeHumiditySensor> mRelativeHumiditySensors;
	private final List<RotationVectorSensor> mRotationVectorSensors;
	private final List<TemperatureSensor> mTemperatureSensors;	
	
	private final SensorWrapper[] mSensors;
	
	private final String ACCURACY_HIGH;
	private final String ACCURACY_MEDIUM;
	private final String ACCURACY_LOW;
	private final String ACCURACY_UNRELIABLE;

	// TODO singleton?
	public Sensors(Context context) {
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);	
		
		mAccelerometerSensors = new ArrayList<AccelerometerSensor>(1);
		mAmbientTemperatureSensors = new ArrayList<AmbientTemperatureSensor>(1);
		mGravitySensors = new ArrayList<GravitySensor>(1);
		mGyroscopeSensors = new ArrayList<GyroscopeSensor>(1);
		mLightSensors = new ArrayList<LightSensor>(1);
		mLinearAccelerationSensors = new ArrayList<LinearAccelerationSensor>(1);
		mMagneticFieldSensors = new ArrayList<MagneticFieldSensor>(1);
		mOrientationSensors = new ArrayList<OrientationSensor>(1);
		mPressureSensors = new ArrayList<PressureSensor>(1);
		mProximitySensors = new ArrayList<ProximitySensor>(1);
		mRelativeHumiditySensors = new ArrayList<RelativeHumiditySensor>(1);
		mRotationVectorSensors = new ArrayList<RotationVectorSensor>(1);
		mTemperatureSensors = new ArrayList<TemperatureSensor>(1);
		
		ACCURACY_HIGH = context.getString(R.string.sensor_accuracy_high);
		ACCURACY_MEDIUM = context.getString(R.string.sensor_accuracy_medium);
		ACCURACY_LOW = context.getString(R.string.sensor_accuracy_low);
		ACCURACY_UNRELIABLE = context.getString(R.string.sensor_accuracy_unreliable);
		
		
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		// Add default sensors first.
		for (Sensor s : sensors) {
			if (s == mSensorManager.getDefaultSensor(s.getType())) 
				addSensor(context, s, true); 				
		}
		// Then other sensors.
		for (Sensor s : sensors) {
			if (s != mSensorManager.getDefaultSensor(s.getType())) 
				addSensor(context, s, false);
		}
		
		List<SensorWrapper> list = new ArrayList<SensorWrapper>();
		list.addAll(mAccelerometerSensors);
		list.addAll(mAmbientTemperatureSensors);
		list.addAll(mGravitySensors);
		list.addAll(mGyroscopeSensors);
		list.addAll(mLightSensors);
		list.addAll(mLinearAccelerationSensors);
		list.addAll(mMagneticFieldSensors);
		list.addAll(mOrientationSensors);
		list.addAll(mPressureSensors);
		list.addAll(mProximitySensors);
		list.addAll(mRelativeHumiditySensors);
		list.addAll(mRotationVectorSensors);
		list.addAll(mTemperatureSensors);	
		mSensors = list.toArray(new SensorWrapper[list.size()]);
	}
	
	private void addSensor(Context context, Sensor sensor, boolean isDefault) {
		int type = sensor.getType();
		
		if (type == Sensor.TYPE_ACCELEROMETER) 
			 mAccelerometerSensors.add(new AccelerometerSensor(context, sensor, isDefault));
        else if (API >= 14 && type == Sensor.TYPE_AMBIENT_TEMPERATURE)
        	mAmbientTemperatureSensors.add(new AmbientTemperatureSensor(context, sensor, isDefault));
        else if (API >= 9 && type == Sensor.TYPE_GRAVITY) 
        	mGravitySensors.add(new GravitySensor(context, sensor, isDefault));
        else if (type == Sensor.TYPE_GYROSCOPE) 
        	mGyroscopeSensors.add(new GyroscopeSensor(context, sensor, isDefault));
        else if (type == Sensor.TYPE_LIGHT) 
        	mLightSensors.add(new LightSensor(context, sensor, isDefault));
        else if (API >= 9 && type == Sensor.TYPE_LINEAR_ACCELERATION) 
        	mLinearAccelerationSensors.add(new LinearAccelerationSensor(context, sensor, isDefault));
        else if (type == Sensor.TYPE_MAGNETIC_FIELD)
        	mMagneticFieldSensors.add(new MagneticFieldSensor(context, sensor, isDefault));
        else if (type == Sensor.TYPE_ORIENTATION) 
        	mOrientationSensors.add(new OrientationSensor(context, sensor, isDefault));
        else if (type == Sensor.TYPE_PRESSURE) 
        	mPressureSensors.add(new PressureSensor(context, sensor, isDefault));
        else if (type == Sensor.TYPE_PROXIMITY) 
        	mProximitySensors.add(new ProximitySensor(context, sensor, isDefault));
        else if (API >= 14 && type == Sensor.TYPE_RELATIVE_HUMIDITY)
        	mRelativeHumiditySensors.add(new RelativeHumiditySensor(context, sensor, isDefault));
        else if (API >= 9 && type == Sensor.TYPE_ROTATION_VECTOR) 
        	mRotationVectorSensors.add(new RotationVectorSensor(context, sensor, isDefault));
        else if (type == Sensor.TYPE_TEMPERATURE)
        	mTemperatureSensors.add(new TemperatureSensor(context, sensor, isDefault));		
	}

	public abstract class SensorWrapper implements ContentsMapper, SensorEventListener {		
		private final Sensor mSensor;	
		private final boolean mIsDefault;		
		private final String mSensorTypeString;
		private final int mCategory;
		private final String mCategoryString;
		private final String mUnit;
		
		private Callback mCallback;
		
		private int mLastAccuracyStatus;
		private int mLastAccuracy;
		private long mLastEventTimestamp;
		protected float[] mLastValues;		
		
		private boolean mIsListening;		
		private int mFrequency;
		private long mLastAccuracyTimestamp;
		
		private boolean mIsPaused;
		
		private SensorWrapper(Context context, Sensor sensor, boolean isDefault,
				String type, int category, String unit) {
			mSensor = sensor;
			mIsDefault = isDefault;
			mSensorTypeString = type;
			mCategory = category;
			mUnit = unit;
			mFrequency = FREQUENCY_MEDIUM;
			
			switch (category) {
			case CATEGORY_MOTION: 
				mCategoryString = context.getString(R.string.sensor_category_motion);
				break;
			case CATEGORY_POSITION: 
				mCategoryString = context.getString(R.string.sensor_category_position);
				break;
			case CATEGORY_ENVIRONMENT: 
				mCategoryString = context.getString(R.string.sensor_category_environment);
				break;
			default:
				mCategoryString = null;
			}
		}

		public Sensor getSensor() {
			return mSensor;
		}			
		
		public Callback getCallback() {
			return mCallback;
		}
		
		public String getUnit() {
			return mUnit;
		}
		
		public String getTypeString() {
			return mSensorTypeString;
		}
		
		public boolean isDefaultForType() {
			return mIsDefault;
		}

		public float getMaximumRange() {
			return mSensor.getMaximumRange();
		}
		
		/** Minimum delay in microseconds (us) between two events. May only report value when data changes. */
		public int getMinDelay() {
			return API >= 9 ? mSensor.getMinDelay() : 0;
		}
		
		public String getName() {
			return mSensor.getName();
		}
		
		/** Power in milli-Amps (mA) */
		public float getPower() {
			return mSensor.getPower();
		}
		
		public float getResolution() {
			return mSensor.getResolution();
		}
		
		public int getType() {
			return mSensor.getType();
		}
		
		public String getVendor() {
			return mSensor.getVendor();
		}
		
		public int getVersion() {
			return mSensor.getVersion();
		}
		
		public int getLastAccuracyStatus() {
			return mLastAccuracyStatus;
		}

		public String getAccuracyString(int accuracy) {
			switch(accuracy) {
			case SensorManager.SENSOR_STATUS_ACCURACY_HIGH: return ACCURACY_HIGH;
			case SensorManager.SENSOR_STATUS_ACCURACY_LOW: return ACCURACY_LOW;
			case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM: return ACCURACY_MEDIUM;
			case SensorManager.SENSOR_STATUS_UNRELIABLE: return ACCURACY_UNRELIABLE;			
			}
			return null;
		}
		
		public String getAccuracyString() {
			return getAccuracyString(mLastAccuracy);
		}
		
		/** The SensorEvent's timestamp in nanoseconds */
		public long getLastEventTimestamp() {
			return mLastEventTimestamp;
		}
		
		public int getLastAccuracy() {
			return mLastAccuracy;
		}
		
		public float[] getLastValues() {
			return mLastValues;
		}
		
		public float getLastValue(int index) {
			if (mLastValues == null || mLastValues.length <= index) return 0;
			return mLastValues[index];
		}
		
		public int getCategory() {
			return mCategory;
		}
		
		public String getCategoryString() {
			return mCategoryString;
		}
		
		/** The last onAccuracyChanged() event's timestamp in milliseconds */
		public long getLastAccuracyTimestamp() {
			return mLastAccuracyTimestamp;
		}
		
		/** 
		 * Returns the minimum time between two events in milliseconds 
		 * that this sensor's listener will respond to events.
		 */
		public int getMinUpdateFrequency() {
			return mFrequency;
		}
		
		/** 
		 * Sets the minimum time between two events in milliseconds 
		 * that this sensor's listener will respond to events.
		 */
		public void setMinUpdateFrequency(int frequency) {
			mFrequency = frequency;
		}
		
		public void setCallback(Callback callback) {
			mCallback = callback;
		}				
		
		
		public boolean isListening() {
			return mIsListening;
		}
		
		/** Whether onPause() was used to stop listening */
		public boolean isPaused() {
			return mIsPaused;
		}

		public void startListening() {
			startListening(true);
		}
		
		public void startListening(boolean onlyIfCallbackSet) {
			if (mIsListening || (onlyIfCallbackSet && mCallback == null)) return;
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
			mIsListening = true;
		}
		
		public void stopListening() {
			if (!mIsListening) return;
			mSensorManager.unregisterListener(this);
			mIsListening = false;
		}
		
		/** 
		 * Meant to be used with the Activity or Fragment life-cycle.  
		 * If this is used to stop listening then onResume() will cause it
		 * to continue listening.
		 */
		public void onPause() {
			if (mIsListening) {
				stopListening();
				mIsPaused = true;				
			}
		}
		
		/** 
		 * Meant to be used with the Activity or Fragment life-cycle.  
		 * If onPause() was used to stop listening then this will cause it
		 * to continue listening.
		 */
		public void onResume() {
			if (mIsPaused) {
				startListening();
				mIsPaused = false;
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			long time = System.currentTimeMillis();
			if (time - mLastAccuracyTimestamp < mFrequency) return;
			mLastAccuracyTimestamp = time;
			mLastAccuracyStatus = accuracy;
			if (mCallback != null) mCallback.onAccuracyChanged(this);
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.timestamp - mLastEventTimestamp < (mFrequency * 1E6)) return;
			mLastEventTimestamp = event.timestamp;
			mLastAccuracy = event.accuracy;
			mLastValues = event.values.clone();
			if (mCallback != null) mCallback.onSensorChanged(this);
		}
		
		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			
			contents.put("Type", getTypeString());
			contents.put("Category", getCategoryString());
			contents.put("Unit", getUnit());
			contents.put("Default for Type", String.valueOf(isDefaultForType()));
			contents.put("Name", getName());
			contents.put("Vendor", getVendor());
			contents.put("Version", String.valueOf(getVersion()));
			contents.put("Power (mA)", String.valueOf(getPower()));
			contents.put("Resolution (" + getUnit() + ")", String.valueOf(getResolution()));
			contents.put("Max Range (" + getUnit() + ")", String.valueOf(getMaximumRange()));
			contents.put("Min Delay (us)", String.valueOf(getMinDelay()));
			contents.put("Is Listening", String.valueOf(isListening()));
			contents.put("Is Listening Stopped By Pause", String.valueOf(isPaused()));
			contents.put("MinUpdateFrequency (ms)", String.valueOf(getMinUpdateFrequency()));
			contents.put("Last Event Timestmp (ns)", String.valueOf(getLastEventTimestamp()));
//			contents.put("Last Event Accuracy", String.valueOf(getLastAccuracy())); 
			//FIXME accuracy shit
			contents.put("Last Accuracy Status", getAccuracyString(getLastAccuracy()));
			contents.put("Last Accuracy Status Timestamp (ms)", String.valueOf(getLastAccuracyTimestamp()));
			
			contents.putAll(getValuesContents());
			
			return contents;
		}
		
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();						
			for (int i = 0; i < mLastValues.length; ++i) {
				contents.put("Last Event Values " + i, String.valueOf(mLastValues[i]));				
			}			
			return contents;
		}
	}
	
	public class AccelerometerSensor extends SensorWrapper {		
		private AccelerometerSensor(Context context, Sensor sensor, boolean isDefault) {
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_accelerometer), 
					CATEGORY_MOTION, 
					context.getString(R.string.unit_meters_per_second_squared));			
		}
		
		/** Acceleration minus Gx on the x-axis in m/s² */
		public float getAccelerationX() {
			return getLastValue(0);
		}
		
		/** Acceleration minus Gy on the y-axis in m/s² */
		public float getAccelerationY() {
			return getLastValue(1);
		}
		
		/** Acceleration minus Gz on the z-axis in m/s² */
		public float getAccelerationZ() {
			return getLastValue(2);
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X Acceleration (" + getUnit() + ")", String.valueOf(getAccelerationX()));
			contents.put("Y Acceleration (" + getUnit() + ")", String.valueOf(getAccelerationY()));
			contents.put("Z Acceleration (" + getUnit() + ")", String.valueOf(getAccelerationZ()));
			return contents;
		}
	}
	
	public class AmbientTemperatureSensor extends SensorWrapper {		
		private AmbientTemperatureSensor(Context context, Sensor sensor, boolean isDefault) {
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_ambient_temperature), 
					CATEGORY_ENVIRONMENT, 
					context.getString(R.string.unit_degrees_celsius));			
		}
	
		/** Ambient (room) temperature in °C */
		public float getAmbientTemperature() {
			return getLastValue(0);
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Ambient temperature (" + getUnit() + ")", String.valueOf(getAmbientTemperature()));
			return contents;
		}
	}
	
	public class GravitySensor extends SensorWrapper {
		private GravitySensor(Context context, Sensor sensor, boolean isDefault) {			
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_gravity), 
					CATEGORY_MOTION, 
					context.getString(R.string.unit_meters_per_second_squared));
		}
		
		/** Gravity on the x-axis in m/s² */
		public float getGravityX() {
			return getLastValue(0);
		}
		
		/** Gravity on the y-axis in m/s² */
		public float getGravityY() {
			return getLastValue(1);
		}
		
		/** Gravity on the z-axis in m/s² */
		public float getGravityZ() {
			return getLastValue(2);
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X Gravity (" + getUnit() + ")", String.valueOf(getGravityX()));
			contents.put("Y Gravity (" + getUnit() + ")", String.valueOf(getGravityY()));
			contents.put("Z Gravity (" + getUnit() + ")", String.valueOf(getGravityZ()));
			return contents;
		}
	}
	
	public class GyroscopeSensor extends SensorWrapper {		
		private GyroscopeSensor(Context context, Sensor sensor, boolean isDefault) {			
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_gyroscope), 
					CATEGORY_MOTION, 
					context.getString(R.string.unit_radians_per_second));
		}
		
		/** Angular speed around the x-axis in rad/s */
		public float getAngularSpeedX() {
			return getLastValue(0);
		}
		
		/** Angular speed around the y-axis in rad/s */
		public float getAngularSpeedY() {
			return getLastValue(1);
		}
		
		/** Angular speed around the z-axis in rad/s */
		public float getAngularSpeedZ() {
			return getLastValue(2);
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X AngularSpeed (" + getUnit() + ")", String.valueOf(getAngularSpeedX()));
			contents.put("Y AngularSpeed (" + getUnit() + ")", String.valueOf(getAngularSpeedY()));
			contents.put("Z AngularSpeed (" + getUnit() + ")", String.valueOf(getAngularSpeedZ()));
			return contents;
		}
	}
	
	public class LightSensor extends SensorWrapper {
		private LightSensor(Context context, Sensor sensor, boolean isDefault) {			
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_light), 
					CATEGORY_ENVIRONMENT, 
					context.getString(R.string.unit_lux));
		}
		
		/** Ambient light level in lux */
		public float getLightLevel() {
			return getLastValue(0);
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Ambient light level (" + getUnit() + ")", String.valueOf(getLightLevel()));
			return contents;
		}
	}
	
	public class LinearAccelerationSensor extends SensorWrapper {
		private LinearAccelerationSensor(Context context, Sensor sensor, boolean isDefault) {
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_linear_acceleration), 
					CATEGORY_MOTION, 
					context.getString(R.string.unit_meters_per_second_squared));
		}
		
		/** Linear acceleration on the x-axis in m/s² */
		public float getLinearAccelerationX() {
			return getLastValue(0);
		}
		
		/** Linear acceleration on the y-axis in m/s² */
		public float getLinearAccelerationY() {
			return getLastValue(1);
		}
		
		/** Linear acceleration on the z-axis in m/s² */
		public float getLinearAccelerationZ() {
			return getLastValue(2);
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X LinearAcceleration (" + getUnit() + ")", String.valueOf(getLinearAccelerationX()));
			contents.put("Y LinearAcceleration (" + getUnit() + ")", String.valueOf(getLinearAccelerationY()));
			contents.put("Z LinearAcceleration (" + getUnit() + ")", String.valueOf(getLinearAccelerationZ()));
			return contents;
		}
	}
	
	public class MagneticFieldSensor extends SensorWrapper {		
		private MagneticFieldSensor(Context context, Sensor sensor, boolean isDefault) {			
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_magnetic_field), 
					CATEGORY_POSITION, 
					context.getString(R.string.unit_micro_tesla));
		}
		
		/** Ambient magnetic field on the x-axis in μT */
		public float getMagneticFieldX() {
			return getLastValue(0);
		}
		
		/** Ambient magnetic field on the y-axis in μT */
		public float getMagneticFieldY() {
			return getLastValue(1);
		}
		
		/** Ambient magnetic field on the z-axis in μT */
		public float getMagneticFieldZ() {
			return getLastValue(2);
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X MagneticField (" + getUnit() + ")", String.valueOf(getMagneticFieldX()));
			contents.put("Y MagneticField (" + getUnit() + ")", String.valueOf(getMagneticFieldY()));
			contents.put("Z MagneticField (" + getUnit() + ")", String.valueOf(getMagneticFieldZ()));
			return contents;
		}
	}
	
	public class OrientationSensor extends SensorWrapper {
		private OrientationSensor(Context context, Sensor sensor, boolean isDefault) {			
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_orientation), 
					CATEGORY_POSITION, 
					context.getString(R.string.unit_degrees));
		}
		
		/** 
		 * Angle between the magnetic north direction and the y-axis, 
		 * around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West 
		 */
		public float getAzimuth() {
			return getLastValue(0);
		}
		
		/** 
		 * Rotation around x-axis (-180 to 180), with positive 
		 * values when the z-axis moves toward the y-axis.
		 */
		public float getPitch() {
			return getLastValue(1);
		}
		
		/**  
		 * Rotation around y-axis (-90 to 90), with positive 
		 * values when the x-axis moves toward the z-axis. 
		 */
		public float getRoll() {
			return getLastValue(2);
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Azimuth (" + getUnit() + ")", String.valueOf(getAzimuth()));
			contents.put("Pitch (" + getUnit() + ")", String.valueOf(getPitch()));
			contents.put("Roll (" + getUnit() + ")", String.valueOf(getRoll()));
			return contents;
		}
	}

	public class PressureSensor extends SensorWrapper {
		private PressureSensor(Context context, Sensor sensor, boolean isDefault) {			
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_pressure), 
					CATEGORY_ENVIRONMENT, 
					context.getString(R.string.unit_hecto_pascal));
		}
		
		/** Atmospheric pressure in hPa (millibar) */
		public float getPressure() {
			return getLastValue(0);
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Atmospheric pressure (" + getUnit() + ")", String.valueOf(getPressure()));
			return contents;
		}
	}
	
	public class ProximitySensor extends SensorWrapper {
		private ProximitySensor(Context context, Sensor sensor, boolean isDefault) {
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_proximity), 
					CATEGORY_POSITION, 
					context.getString(R.string.unit_centimeter));
		}
		
		/** 
		 * Proximity sensor distance in cm.
		 * Note: Some proximity sensors only support a binary near or far 
		 * measurement. In this case, the sensor should report its maximum 
		 * range value in the far state and a lesser value in the near state.  
		 */
		public float getProximity() {
			return getLastValue(0);
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Proximity (" + getUnit() + ") (may only indicate 'near' or 'far')", String.valueOf(getProximity()));
			return contents;
		}
	}

	public class RelativeHumiditySensor extends SensorWrapper {
		private RelativeHumiditySensor(Context context, Sensor sensor, boolean isDefault) {			
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_relative_humidity), 
					CATEGORY_ENVIRONMENT, 
					context.getString(R.string.unit_percent));
		}
		
		/** Relative ambient air humidity in % */
		public float getRelativeHumidity() {
			return getLastValue(0);
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Relative humidity (" + getUnit() + ")", String.valueOf(getRelativeHumidity()));
			return contents;
		}
	}
	
	// TODO Math!
	public class RotationVectorSensor extends SensorWrapper {
		private RotationVectorSensor(Context context, Sensor sensor, boolean isDefault) {			
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_rotation_vector), 
					CATEGORY_MOTION, 
					context.getString(R.string.unit_unitless));
		}
		
		/** x*sin(θ/2) (unitless) */
		public float getRotationVectorX() {
			return getLastValue(0);
		}
		
		/** y*sin(θ/2) (unitless) */
		public float getRotationVectorY() {
			return getLastValue(1);
		}
		
		/** z*sin(θ/2) (unitless) */
		public float getRotationVectorZ() {
			return getLastValue(2);
		}	
		
		/** cos(θ/2) (unitless) */
		public float getRotationVectorExtra() {
			if (mLastValues == null || mLastValues.length < 4) return 0;
			return mLastValues[3];
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("RotationVectorX (unitless)", String.valueOf(getRotationVectorX()));
			contents.put("RotationVectorY (unitless)", String.valueOf(getRotationVectorY()));
			contents.put("RotationVectorZ (unitless)", String.valueOf(getRotationVectorZ()));
			contents.put("RotationVectorExtra (unitless)", String.valueOf(getRotationVectorExtra()));
			return contents;
		}
	}
	
	public class TemperatureSensor extends SensorWrapper {
		private TemperatureSensor(Context context, Sensor sensor, boolean isDefault) {
			super (context, sensor, isDefault, 
					context.getString(R.string.sensor_type_temperature), 
					CATEGORY_ENVIRONMENT, 
					context.getString(R.string.unit_degrees_celsius));
		}
		
		/** Temperature in °C */
		public float getTemperature() {
			return getLastValue(0);
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Temperature (" + getUnit() + ")", String.valueOf(getTemperature()));
			return contents;
		}
	}
	
	public SensorManager getSensorManager() {
		return mSensorManager;
	}

	public SensorWrapper[] getAllSensors() {
		return mSensors;
	}
	
	/** 
	 * Gathers the default sensor from each type of sensor list into a single list.
	 * Not optimized for repetitive use.<br><br>
	 * <b>Note:</b> This returns the first sensor in each type of sensor list, which should be
	 * the one that is marked <i>default</i>. However, if there is at least one sensor of that type
	 * and none of them are defined as <i>default</i>, the first of those sensors would be included 
	 * in this list.
	 * @return List of default sensors
	 */
	public SensorWrapper[] getDefaultSensors() {
		List<SensorWrapper> list = new ArrayList<SensorWrapper>();
		if (mAccelerometerSensors.size() > 0) list.add(mAccelerometerSensors.get(0));
		if (mAmbientTemperatureSensors.size() > 0) list.add(mAmbientTemperatureSensors.get(0));
		if (mGravitySensors.size() > 0) list.add(mGravitySensors.get(0));
		if (mGyroscopeSensors.size() > 0) list.add(mGyroscopeSensors.get(0));
		if (mLightSensors.size() > 0) list.add(mLightSensors.get(0));
		if (mLinearAccelerationSensors.size() > 0) list.add(mLinearAccelerationSensors.get(0));
		if (mMagneticFieldSensors.size() > 0) list.add(mMagneticFieldSensors.get(0));
		if (mOrientationSensors.size() > 0) list.add(mOrientationSensors.get(0));
		if (mPressureSensors.size() > 0) list.add(mPressureSensors.get(0));
		if (mProximitySensors.size() > 0) list.add(mProximitySensors.get(0));
		if (mRelativeHumiditySensors.size() > 0) list.add(mRelativeHumiditySensors.get(0));
		if (mRotationVectorSensors.size() > 0) list.add(mRotationVectorSensors.get(0));
		if (mTemperatureSensors.size() > 0) list.add(mTemperatureSensors.get(0));
		return list.toArray(new SensorWrapper[list.size()]);
	}
	
	/** Not optimized for repetitive use */
	public SensorWrapper[] getSensorsByCategory(int category) {
		List<SensorWrapper> list = new ArrayList<SensorWrapper>();
		for (SensorWrapper sw : mSensors) {
			if (sw.getCategory() == category) {
				list.add(sw);
			}
		}
		return list.toArray(new SensorWrapper[list.size()]);
	}
	
	/** The default sensor will be the first element */
	public List<AccelerometerSensor> getAccelerometerSensors() {
		return mAccelerometerSensors;
	}
	
	/** The default sensor will be the first element */
	public List<AmbientTemperatureSensor> getAmbientTemperatureSensors() {
		return mAmbientTemperatureSensors;
	}
	
	/** The default sensor will be the first element */
	public List<GravitySensor> getGravitySensors() {
		return mGravitySensors;
	}
	
	/** The default sensor will be the first element */
	public List<GyroscopeSensor> getGyroscopeSensors() {
		return mGyroscopeSensors;
	}
	
	/** The default sensor will be the first element */
	public List<LightSensor> getLightSensors() {
		return mLightSensors;
	}
	
	/** The default sensor will be the first element */
	public List<LinearAccelerationSensor> getLinearAccelerationSensors() {
		return mLinearAccelerationSensors;
	}
	
	/** The default sensor will be the first element */
	public List<MagneticFieldSensor> getMagneticFieldSensors() {
		return mMagneticFieldSensors;
	}
	
	/** The default sensor will be the first element */
	public List<OrientationSensor> getOrientationSensors() {
		return mOrientationSensors;
	}
	
	/** The default sensor will be the first element */
	public List<PressureSensor> getPressureSensors() {
		return mPressureSensors;
	}
	
	/** The default sensor will be the first element */
	public List<ProximitySensor> getProximitySensors() {
		return mProximitySensors;
	}
	
	/** The default sensor will be the first element */
	public List<RelativeHumiditySensor> getRelativeHumiditySensors() {
		return mRelativeHumiditySensors;
	}
	
	/** The default sensor will be the first element */
	public List<RotationVectorSensor> getRotationVectorSensors() {
		return mRotationVectorSensors;
	}
	
	/** The default sensor will be the first element */
	public List<TemperatureSensor> getTemperatureSensors() {
		return mTemperatureSensors;
	}
	
	public float[] getOrientationInWorldCoordinateSystem() {
		if (mAccelerometerSensors.size() < 1 || mMagneticFieldSensors.size() < 1) return null;
		
		float[] gravs = mAccelerometerSensors.get(0).getLastValues(), 
				geomags = mMagneticFieldSensors.get(0).getLastValues();
		if (gravs == null || geomags == null) return null;
		
		float[] rotationMatrix = new float[9], 
				orientation = new float[3];
		
		if (SensorManager.getRotationMatrix(rotationMatrix, null, gravs, geomags)) {
			SensorManager.getOrientation(rotationMatrix, orientation);
		}
		return orientation;
	}
	
	/** Calculates the dew point in degrees Celsius */
	public float getDewPoint() {
		if (mRelativeHumiditySensors.size() < 1 || mAmbientTemperatureSensors.size() < 1) return 0;
		float rh = mRelativeHumiditySensors.get(0).getRelativeHumidity();;
		float t = mAmbientTemperatureSensors.get(0).getAmbientTemperature();		
		double h = Math.log(rh / 100.0) + (17.62 * t) / (243.12 + t);
		return (float) (243.12 * h / (17.62 - h));
	}
	
	/** Calculates the absolute humidity in g/m^3 */
	public float getAbsoluteHumidity() {
		if (mRelativeHumiditySensors.size() < 1 || mAmbientTemperatureSensors.size() < 1) return 0;
		float rh = mRelativeHumiditySensors.get(0).getRelativeHumidity();;
		float t = mAmbientTemperatureSensors.get(0).getAmbientTemperature();		
		return (float) (216.7 * (rh / 100.0 * 6.112 * Math.exp(17.62 * t / (243.12 + t)) / (273.15 + t)));
	}

	
	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		if (!super.startListening(onlyIfCallbackSet)) return false;
		for (SensorWrapper sw : mSensors) {
			if (onlyIfCallbackSet && sw.getCallback() == null) {
				continue;
			}
			sw.startListening(onlyIfCallbackSet);
		}
		return setListening(true);
	}
	
	@Override
	public boolean stopListening() {
		if (!super.stopListening()) return false;
		for (SensorWrapper sw : mSensors) {
			sw.stopListening();
		}
		return !setListening(false);
	}
	
	@Override
	public void setCallback(ListeningElement.Callback callback) {
		super.setCallback(callback);
		for (SensorWrapper sw : mSensors) {
			sw.setCallback((Callback) callback);
		}
	}
	
//	/** Pauses all sensors */
//	public void onPause() {
//		for (SensorWrapper sw : mSensors) {
//			sw.onPause();
//		}
//	}
//	
//	/** Resumes listening on sensors that were stopped with onPause() */
//	public void onResume() {
//		for (SensorWrapper sw : mSensors) {
//			sw.onResume();
//		}
//	}
	
	public int getSensorIndex(SensorWrapper sensorWrapper) {
		for (int i = 0; i < mSensors.length; ++i) {
			if (mSensors[i] == sensorWrapper) return i;
		}
		return -1;
	}

	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> subcontents;
		int i = 0;
		for (AccelerometerSensor s : mAccelerometerSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("AccelerometerSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (AmbientTemperatureSensor s : mAmbientTemperatureSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("AmbientTemperatureSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (GravitySensor s : mGravitySensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("GravitySensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (GyroscopeSensor s : mGyroscopeSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("GyroscopeSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (LightSensor s : mLightSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("LightSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (LinearAccelerationSensor s : mLinearAccelerationSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("LinearAccelerationSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (MagneticFieldSensor s : mMagneticFieldSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("MagneticFieldSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (OrientationSensor s : mOrientationSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("OrientationSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (PressureSensor s : mPressureSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("PressureSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (ProximitySensor s : mProximitySensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("ProximitySensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (RelativeHumiditySensor s : mRelativeHumiditySensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("RelativeHumiditySensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (RotationVectorSensor s : mRotationVectorSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("RotationVectorSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		i = 0;
		for (TemperatureSensor s : mTemperatureSensors) {
			subcontents = s.getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {					
				contents.put("TemperatureSensor " + i + " " + e.getKey(), e.getValue());
			}			
			++i;
		}
		
		float[] coords = getOrientationInWorldCoordinateSystem();
		if (coords != null) {
			i = 0;
			for (float v : coords) {
				contents.put("Orientation in World Coordinate System " + i, String.valueOf(v));
				++i;
			}
		}
		
		contents.put("Dew Point (C)", String.valueOf(getDewPoint()));
		contents.put("Absolute Humidity (g/m^3)", String.valueOf(getAbsoluteHumidity()));		
		
		return contents;
	}
}
