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

import com.jphilli85.deviceinfo.ContentsMapper;

// TODO add microphone
public class Sensors implements ContentsMapper {
	private static final String LOG_TAG = Sensors.class.getSimpleName();
	private static final int API = Build.VERSION.SDK_INT;
	
	public static final String TYPE_ACCELEROMETER = "TYPE_ACCELEROMETER";
	public static final String TYPE_AMBIENT_TEMPERATURE = "TYPE_AMBIENT_TEMPERATURE";
	public static final String TYPE_GRAVITY = "TYPE_GRAVITY";
	public static final String TYPE_GYROSCOPE = "TYPE_GYROSCOPE";
	public static final String TYPE_LIGHT = "TYPE_LIGHT";
	public static final String TYPE_LINEAR_ACCELERATION = "TYPE_LINEAR_ACCELERATION";
	public static final String TYPE_MAGNETIC_FIELD = "TYPE_MAGNETIC_FIELD";
	public static final String TYPE_ORIENTATION = "TYPE_ORIENTATION";
	public static final String TYPE_PRESSURE = "TYPE_PRESSURE";
	public static final String TYPE_PROXIMITY = "TYPE_PROXIMITY";
	public static final String TYPE_RELATIVE_HUMIDITY = "TYPE_RELATIVE_HUMIDITY";
	public static final String TYPE_ROTATION_VECTOR = "TYPE_ROTATION_VECTOR";
	public static final String TYPE_TEMPERATURE = "TYPE_TEMPERATURE";
	
	public static final String CATEGORY_MOTION = "CATEGORY_MOTION";
	public static final String CATEGORY_POSITION = "CATEGORY_POSITION";
	public static final String CATEGORY_ENVIRONMENT = "CATEGORY_ENVIRONMENT";
	
	public static final String SENSOR_STATUS_ACCURACY_HIGH = "SENSOR_STATUS_ACCURACY_HIGH";
	public static final String SENSOR_STATUS_ACCURACY_LOW = "SENSOR_STATUS_ACCURACY_LOW";
	public static final String SENSOR_STATUS_ACCURACY_MEDIUM = "SENSOR_STATUS_ACCURACY_MEDIUM";
	public static final String SENSOR_STATUS_UNRELIABLE = "SENSOR_STATUS_UNRELIABLE";
	
	public interface Callback {
		/** Corresponds to SensorEventListener.onAccuracyChanged() */
		void onAccuracyChanged(SensorWrapper sensorWrapper);
		/** Corresponds to SensorEventListener.onSensorChanged() */
		void onSensorChanged(SensorWrapper sensorWrapper);		
	}
	
	private final SensorManager mSensorManager;
//	private final List<SensorWrapper> mSensors;
	private final Callback mCallback;	
	
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

	// TODO singleton?
	public Sensors(Context context, Callback callback) {
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mCallback = callback;
//		mSensors = new ArrayList<SensorWrapper>();		
		
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
		
		
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		// Add default sensors first.
		for (Sensor s : sensors) {
			if (s == mSensorManager.getDefaultSensor(s.getType())) 
				addSensor(s, true); 				
		}
		// Then other sensors.
		for (Sensor s : sensors) {
			if (s != mSensorManager.getDefaultSensor(s.getType())) 
				addSensor(s, false);
		}
	}
	
	private void addSensor(Sensor sensor, boolean isDefault) {
		int type = sensor.getType();
		
		if (type == Sensor.TYPE_ACCELEROMETER) 
			 mAccelerometerSensors.add(new AccelerometerSensor(sensor, isDefault));
        else if (API >= 14 && type == Sensor.TYPE_AMBIENT_TEMPERATURE)
        	mAmbientTemperatureSensors.add(new AmbientTemperatureSensor(sensor, isDefault));
        else if (API >= 9 && type == Sensor.TYPE_GRAVITY) 
        	mGravitySensors.add(new GravitySensor(sensor, isDefault));
        else if (type == Sensor.TYPE_GYROSCOPE) 
        	mGyroscopeSensors.add(new GyroscopeSensor(sensor, isDefault));
        else if (type == Sensor.TYPE_LIGHT) 
        	mLightSensors.add(new LightSensor(sensor, isDefault));
        else if (API >= 9 && type == Sensor.TYPE_LINEAR_ACCELERATION) 
        	mLinearAccelerationSensors.add(new LinearAccelerationSensor(sensor, isDefault));
        else if (type == Sensor.TYPE_MAGNETIC_FIELD)
        	mMagneticFieldSensors.add(new MagneticFieldSensor(sensor, isDefault));
        else if (type == Sensor.TYPE_ORIENTATION) 
        	mOrientationSensors.add(new OrientationSensor(sensor, isDefault));
        else if (type == Sensor.TYPE_PRESSURE) 
        	mPressureSensors.add(new PressureSensor(sensor, isDefault));
        else if (type == Sensor.TYPE_PROXIMITY) 
        	mProximitySensors.add(new ProximitySensor(sensor, isDefault));
        else if (API >= 14 && type == Sensor.TYPE_RELATIVE_HUMIDITY)
        	mRelativeHumiditySensors.add(new RelativeHumiditySensor(sensor, isDefault));
        else if (API >= 9 && type == Sensor.TYPE_ROTATION_VECTOR) 
        	mRotationVectorSensors.add(new RotationVectorSensor(sensor, isDefault));
        else if (type == Sensor.TYPE_TEMPERATURE)
        	mTemperatureSensors.add(new TemperatureSensor(sensor, isDefault));		
	}

	public abstract class SensorWrapper implements ContentsMapper, SensorEventListener {		
		private final Sensor mSensor;	
		private final boolean mIsDefault;		
		private final String mSensorTypeString;
		private final String mCategory;
		private final String mUnit;
		
		private boolean mIsListening;
		
		private int mLastAccuracyStatus;
		private int mLastAccuracy;
		private long mLastTimestamp;
		protected float[] mLastValues;
		
		
		
		private SensorWrapper(Sensor sensor, boolean isDefault,
				String type, String category, String unit) {
			mSensor = sensor;
			mIsDefault = isDefault;
			mSensorTypeString = type;
			mCategory = category;
			mUnit = unit;
		}

		public Sensor getSensor() {
			return mSensor;
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
		
		// TODO ui facing strings
		public String getLastAccuracyStatusString() {
			switch(mLastAccuracyStatus) {
			case SensorManager.SENSOR_STATUS_ACCURACY_HIGH: return SENSOR_STATUS_ACCURACY_HIGH;
			case SensorManager.SENSOR_STATUS_ACCURACY_LOW: return SENSOR_STATUS_ACCURACY_LOW;
			case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM: return SENSOR_STATUS_ACCURACY_MEDIUM;
			case SensorManager.SENSOR_STATUS_UNRELIABLE: return SENSOR_STATUS_UNRELIABLE;			
			}
			return null;
		}
		
		/** The SensorEvent's timestamp in ns */
		public long getLastTimestamp() {
			return mLastTimestamp;
		}
		
		public int getLastAccuracy() {
			return mLastAccuracy;
		}
		
		public float[] getLastValues() {
			return mLastValues;
		}
		
		public String getCategory() {
			return mCategory;
		}
		
		public boolean isListening() {
			return mIsListening;
		}
		
		public void startListening() {
			if (mIsListening) return;		
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
			mIsListening = true;
		}
		
		public void stopListening() {
			if (!mIsListening) return;
			mSensorManager.unregisterListener(this);
			mIsListening = false;
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			mLastAccuracyStatus = accuracy;
			if (mCallback != null) mCallback.onAccuracyChanged(this);
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			mLastTimestamp = event.timestamp;
			mLastAccuracy = event.accuracy;
			mLastValues = event.values.clone();
			if (mCallback != null) mCallback.onSensorChanged(this);
		}
		
		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			
			contents.put("Type", getTypeString());
			contents.put("Category", getCategory());
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
			contents.put("Last Event Timestmp (ns)", String.valueOf(getLastTimestamp()));
			contents.put("Last Event Accuracy", String.valueOf(getLastAccuracy()));
			contents.put("Last Event Accuracy Status", getLastAccuracyStatusString());
			
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
		public static final String UNIT = "m/s²";
		
		private AccelerometerSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_ACCELEROMETER, CATEGORY_MOTION, UNIT);			
		}
		
		/** Acceleration minus Gx on the x-axis in m/s² */
		public float getAccelerationX() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		/** Acceleration minus Gy on the y-axis in m/s² */
		public float getAccelerationY() {
			return mLastValues == null ? 0 : mLastValues[1];
		}
		
		/** Acceleration minus Gz on the z-axis in m/s² */
		public float getAccelerationZ() {
			return mLastValues == null ? 0 : mLastValues[2];
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X Acceleration (" + UNIT + ")", String.valueOf(mLastValues[0]));
			contents.put("Y Acceleration (" + UNIT + ")", String.valueOf(mLastValues[1]));
			contents.put("Z Acceleration (" + UNIT + ")", String.valueOf(mLastValues[2]));
			return contents;
		}
	}
	
	public class AmbientTemperatureSensor extends SensorWrapper {
		public static final String UNIT = "°C";
		
		private AmbientTemperatureSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_AMBIENT_TEMPERATURE, CATEGORY_ENVIRONMENT, UNIT);
		}
		
		/** Ambient (room) temperature in °C */
		public float getAmbientTemperature() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Ambient temperature (" + UNIT + ")", String.valueOf(mLastValues[0]));
			return contents;
		}
	}
	
	public class GravitySensor extends SensorWrapper {
		public static final String UNIT = "m/s²";
		
		private GravitySensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_GRAVITY, CATEGORY_MOTION, UNIT);
		}
		
		/** Gravity on the x-axis in m/s² */
		public float getGravityX() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		/** Gravity on the y-axis in m/s² */
		public float getGravityY() {
			return mLastValues == null ? 0 : mLastValues[1];
		}
		
		/** Gravity on the z-axis in m/s² */
		public float getGravityZ() {
			return mLastValues == null ? 0 : mLastValues[2];
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X Gravity (" + UNIT + ")", String.valueOf(mLastValues[0]));
			contents.put("Y Gravity (" + UNIT + ")", String.valueOf(mLastValues[1]));
			contents.put("Z Gravity (" + UNIT + ")", String.valueOf(mLastValues[2]));
			return contents;
		}
	}
	
	public class GyroscopeSensor extends SensorWrapper {
		public static final String UNIT = "rad/s";
		
		private GyroscopeSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_GYROSCOPE, CATEGORY_MOTION, UNIT);
		}
		
		/** Angular speed around the x-axis in rad/s */
		public float getAngularSpeedX() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		/** Angular speed around the y-axis in rad/s */
		public float getAngularSpeedY() {
			return mLastValues == null ? 0 : mLastValues[1];
		}
		
		/** Angular speed around the z-axis in rad/s */
		public float getAngularSpeedZ() {
			return mLastValues == null ? 0 : mLastValues[2];
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X AngularSpeed (" + UNIT + ")", String.valueOf(mLastValues[0]));
			contents.put("Y AngularSpeed (" + UNIT + ")", String.valueOf(mLastValues[1]));
			contents.put("Z AngularSpeed (" + UNIT + ")", String.valueOf(mLastValues[2]));
			return contents;
		}
	}
	
	public class LightSensor extends SensorWrapper {
		public static final String UNIT = "lx";
		
		private LightSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_LIGHT, CATEGORY_ENVIRONMENT, UNIT);
		}
		
		/** Ambient light level in lux */
		public float getLightLevel() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Ambient light level (" + UNIT + ")", String.valueOf(mLastValues[0]));
			return contents;
		}
	}
	
	public class LinearAccelerationSensor extends SensorWrapper {
		public static final String UNIT = "m/s²";
		
		private LinearAccelerationSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_LINEAR_ACCELERATION, CATEGORY_MOTION, UNIT);
		}
		
		/** Linear acceleration on the x-axis in m/s² */
		public float getLinearAccelerationX() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		/** Linear acceleration on the y-axis in m/s² */
		public float getLinearAccelerationY() {
			return mLastValues == null ? 0 : mLastValues[1];
		}
		
		/** Linear acceleration on the z-axis in m/s² */
		public float getLinearAccelerationZ() {
			return mLastValues == null ? 0 : mLastValues[2];
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X LinearAcceleration (" + UNIT + ")", String.valueOf(mLastValues[0]));
			contents.put("Y LinearAcceleration (" + UNIT + ")", String.valueOf(mLastValues[1]));
			contents.put("Z LinearAcceleration (" + UNIT + ")", String.valueOf(mLastValues[2]));
			return contents;
		}
	}
	
	public class MagneticFieldSensor extends SensorWrapper {
		public static final String UNIT = "μT";
		
		private MagneticFieldSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_MAGNETIC_FIELD, CATEGORY_POSITION, UNIT);
		}
		
		/** Ambient magnetic field on the x-axis in μT */
		public float getMagneticFieldX() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		/** Ambient magnetic field on the y-axis in μT */
		public float getMagneticFieldY() {
			return mLastValues == null ? 0 : mLastValues[1];
		}
		
		/** Ambient magnetic field on the z-axis in μT */
		public float getMagneticFieldZ() {
			return mLastValues == null ? 0 : mLastValues[2];
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("X MagneticField (" + UNIT + ")", String.valueOf(mLastValues[0]));
			contents.put("Y MagneticField (" + UNIT + ")", String.valueOf(mLastValues[1]));
			contents.put("Z MagneticField (" + UNIT + ")", String.valueOf(mLastValues[2]));
			return contents;
		}
	}
	
	public class OrientationSensor extends SensorWrapper {
		public static final String UNIT = "°";
		
		private OrientationSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_ORIENTATION, CATEGORY_POSITION, UNIT);
		}
		
		/** 
		 * Angle between the magnetic north direction and the y-axis, 
		 * around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West 
		 */
		public float getAzimuth() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		/** 
		 * Rotation around x-axis (-180 to 180), with positive 
		 * values when the z-axis moves toward the y-axis.
		 */
		public float getPitch() {
			return mLastValues == null ? 0 : mLastValues[1];
		}
		
		/**  
		 * Rotation around y-axis (-90 to 90), with positive 
		 * values when the x-axis moves toward the z-axis. 
		 */
		public float getRoll() {
			return mLastValues == null ? 0 : mLastValues[2];
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Azimuth (" + UNIT + ")", String.valueOf(mLastValues[0]));
			contents.put("Pitch (" + UNIT + ")", String.valueOf(mLastValues[1]));
			contents.put("Roll (" + UNIT + ")", String.valueOf(mLastValues[2]));
			return contents;
		}
	}

	public class PressureSensor extends SensorWrapper {
		public static final String UNIT = "hPa";
		
		private PressureSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_PRESSURE, CATEGORY_ENVIRONMENT, UNIT);
		}
		
		/** Atmospheric pressure in hPa (millibar) */
		public float getPressure() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Atmospheric pressure (" + UNIT + ")", String.valueOf(mLastValues[0]));
			return contents;
		}
	}
	
	public class ProximitySensor extends SensorWrapper {
		public static final String UNIT = "cm";
		
		private ProximitySensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_PROXIMITY, CATEGORY_POSITION, UNIT);
		}
		
		/** 
		 * Proximity sensor distance in cm.
		 * Note: Some proximity sensors only support a binary near or far 
		 * measurement. In this case, the sensor should report its maximum 
		 * range value in the far state and a lesser value in the near state.  
		 */
		public float getProximity() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Proximity (" + UNIT + ") (may only indicate 'near' or 'far')", String.valueOf(mLastValues[0]));
			return contents;
		}
	}

	public class RelativeHumiditySensor extends SensorWrapper {
		public static final String UNIT = "%";
		
		private RelativeHumiditySensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_RELATIVE_HUMIDITY, CATEGORY_ENVIRONMENT, UNIT);
		}
		
		/** Relative ambient air humidity in % */
		public float getRelativeHumidity() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Relative humidity (" + UNIT + ")", String.valueOf(mLastValues[0]));
			return contents;
		}
	}
	
	// TODO Math!
	public class RotationVectorSensor extends SensorWrapper {
		public static final String UNIT = "";
		
		private RotationVectorSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_ROTATION_VECTOR, CATEGORY_MOTION, UNIT);
		}
		
		/** x*sin(θ/2) (unitless) */
		public float getRotationVectorX() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		/** y*sin(θ/2) (unitless) */
		public float getRotationVectorY() {
			return mLastValues == null ? 0 : mLastValues[1];
		}
		
		/** z*sin(θ/2) (unitless) */
		public float getRotationVectorZ() {
			return mLastValues == null ? 0 : mLastValues[2];
		}	
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("RotationVectorX (unitless)", String.valueOf(mLastValues[0]));
			contents.put("RotationVectorY (unitless)", String.valueOf(mLastValues[1]));
			contents.put("RotationVectorZ (unitless)", String.valueOf(mLastValues[2]));
			return contents;
		}
	}
	
	public class TemperatureSensor extends SensorWrapper {
		public static final String UNIT = "°C";
		
		private TemperatureSensor(Sensor sensor, boolean isDefault) {
			super (sensor, isDefault, TYPE_TEMPERATURE, CATEGORY_ENVIRONMENT, UNIT);
		}
		
		/** Temperature in °C */
		public float getTemperature() {
			return mLastValues == null ? 0 : mLastValues[0];
		}
		
		@Override
		protected LinkedHashMap<String, String> getValuesContents() {
			if (mLastValues != null) return null;
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			contents.put("Temperature (" + UNIT + ")", String.valueOf(mLastValues[0]));
			return contents;
		}
	}
	
	public SensorManager getSensorManager() {
		return mSensorManager;
	}
	
	public Callback getCallback() {
		return mCallback;
	}
	
	public List<SensorWrapper> getAllSensors() {
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
		return list;
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
		float rh = 0, t = 0;
		float[] values = mRelativeHumiditySensors.get(0).getLastValues();
		if (values != null) rh = values[0];
		values = mAmbientTemperatureSensors.get(0).getLastValues();
		if (values != null) t = values[0];
		
		double h = Math.log(rh / 100.0) + (17.62 * t) / (243.12 + t);
		return (float) (243.12 * h / (17.62 - h));
	}
	
	/** Calculates the absolute humidity in g/m^3 */
	public float getAbsoluteHumidity() {
		if (mRelativeHumiditySensors.size() < 1 || mAmbientTemperatureSensors.size() < 1) return 0;
		float rh = 0, t = 0;
		float[] values = mRelativeHumiditySensors.get(0).getLastValues();
		if (values != null) rh = values[0];
		values = mAmbientTemperatureSensors.get(0).getLastValues();
		if (values != null) t = values[0];
		
		return (float) (216.7 * (rh / 100.0 * 6.112 * Math.exp(17.62 * t / (243.12 + t)) / (273.15 + t)));
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
