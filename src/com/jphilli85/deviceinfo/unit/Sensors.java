package com.jphilli85.deviceinfo.unit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

public class Sensors extends Unit implements SensorEventListener {
	private static final String LOG_TAG = Sensors.class.getSimpleName();
	private static final int API = Build.VERSION.SDK_INT;
	
	public static final String CATEGORY_MOTION = "CATEGORY_MOTION";
	public static final String CATEGORY_POSITION = "CATEGORY_POSITION";
	public static final String CATEGORY_ENVIRONMENT = "CATEGORY_ENVIRONMENT";
	
	private final SensorManager mSensorManager;
	private final List<SensorWrapper> mSensors;
	private final SensorEventCallback mEventCallback;
	
	// Sensor types
	private boolean mHasAccelerometer;
	private boolean mHasAmbientTemperature;
	private boolean mHasGravity;
	private boolean mHasGyroscope;
	private boolean mHasLight;
	private boolean mHasLinearAcceleration;
	private boolean mHasMagneticField;
	private boolean mHasOrientation;
	private boolean mHasPressure;
	private boolean mHasProximity;
	private boolean mHasRelativeHumidity;
	private boolean mHasRotationVector;
	private boolean mHasTemperature;	
	
	public interface SensorEventCallback {
		void onSensorChanged(SensorWrapper sensorWrapper);
		void onAccuracyChanged(SensorWrapper sensorWrapper);
	}
	
	public Sensors(Context context, SensorEventCallback eventCallback) {
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mEventCallback = eventCallback;
		mSensors = new ArrayList<SensorWrapper>();
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		for (Sensor s : sensors) mSensors.add(new SensorWrapper(s));
	}

	private class SensorWrapper implements ContentsMapper {
		private final Sensor mSensor;		
		private String mSensorTypeString;
		private final boolean mIsDefault;
		
		private int mLastAccuracy;
		private long mLastTimestamp;
		private float[] mLastValues;
		
		private int mLastAccuracyStatus;
		
		private String mCategory;
		
		public SensorWrapper(Sensor sensor) {
			mSensor = sensor;	
			int type = sensor.getType();
			mIsDefault = sensor == mSensorManager.getDefaultSensor(type);
			setMembersFromType(type);						
		}
		
		// TODO ui facing strings
		private void setMembersFromType(int type) {
	        if (type == Sensor.TYPE_ACCELEROMETER) {
	        	mHasAccelerometer = true;
	        	mCategory = CATEGORY_MOTION;
	        	mSensorTypeString = "TYPE_ACCELEROMETER";
	        }
	        else if (API >= 14 && type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
	        	mHasAmbientTemperature = true;
	        	mCategory = CATEGORY_ENVIRONMENT;
	        	mSensorTypeString = "TYPE_AMBIENT_TEMPERATURE";
	        }
	        else if (API >= 9 && type == Sensor.TYPE_GRAVITY) {
	        	mHasGravity = true;
	        	mCategory = CATEGORY_MOTION;
	        	mSensorTypeString = "TYPE_GRAVITY";
	        }
	        else if (type == Sensor.TYPE_GYROSCOPE) {
	        	mHasGyroscope = true;
	        	mCategory = CATEGORY_MOTION;
	        	mSensorTypeString = "TYPE_GYROSCOPE";
	        }
	        else if (type == Sensor.TYPE_LIGHT) {
	        	mHasLight = true;
	        	mCategory = CATEGORY_ENVIRONMENT;
	        	mSensorTypeString = "TYPE_LIGHT";
	        }
	        else if (API >= 9 && type == Sensor.TYPE_LINEAR_ACCELERATION) {
	        	mHasLinearAcceleration = true;
	        	mCategory = CATEGORY_MOTION;
	        	mSensorTypeString = "TYPE_LINEAR_ACCELERATION";
	        }
	        else if (type == Sensor.TYPE_MAGNETIC_FIELD) {
	        	mHasMagneticField = true;
	        	mCategory = CATEGORY_POSITION;
	        	mSensorTypeString = "TYPE_MAGNETIC_FIELD";
	        }
	        else if (type == Sensor.TYPE_ORIENTATION) {
	        	mHasOrientation = true;
	        	mCategory = CATEGORY_POSITION;
	        	mSensorTypeString = "TYPE_ORIENTATION";
	        }
	        else if (type == Sensor.TYPE_PRESSURE) {
	        	mHasPressure = true;
	        	mCategory = CATEGORY_ENVIRONMENT;
	        	mSensorTypeString = "TYPE_PRESSURE";
	        }
	        else if (type == Sensor.TYPE_PROXIMITY) {
	        	mHasProximity = true;
	        	mCategory = CATEGORY_POSITION;
	        	mSensorTypeString = "TYPE_PROXIMITY";
	        }
	        else if (API >= 14 && type == Sensor.TYPE_RELATIVE_HUMIDITY) {
	        	mHasRelativeHumidity = true;
	        	mCategory = CATEGORY_ENVIRONMENT;
	        	mSensorTypeString = "TYPE_RELATIVE_HUMIDITY";
	        }
	        else if (API >= 9 && type == Sensor.TYPE_ROTATION_VECTOR) {
	        	mHasRotationVector = true;
	        	mCategory = CATEGORY_MOTION;
	        	mSensorTypeString = "TYPE_ROTATION_VECTOR";
	        }
	        else if (type == Sensor.TYPE_TEMPERATURE) {
	        	mHasTemperature = true;
	        	mCategory = CATEGORY_ENVIRONMENT;
	        	mSensorTypeString = "TYPE_TEMPERATURE";
	        }
	    }
		
		public Sensor getSensor() {
			return mSensor;
		}
		
		public String getTypeString() {
			return mSensorTypeString;
		}
		
		public boolean isDefaultForType() {
			return mIsDefault;
		}
		
		// TODO Units!
		
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
			
		private void setLastEventInfo(long timestamp, int accuracy, float[] values) {
			mLastTimestamp = timestamp;
			mLastAccuracy = accuracy;
			mLastValues = values;
		}
		
		private void setLastAccuracyStatus(int accuracy) {
			mLastAccuracyStatus = accuracy;
		}
		
		public int getLastAccuracyStatus() {
			return mLastAccuracyStatus;
		}
		
		public String getLastAccuracyStatusString() {
			switch(mLastAccuracyStatus) {
			case SensorManager.SENSOR_STATUS_ACCURACY_HIGH: return "SENSOR_STATUS_ACCURACY_HIGH";
			case SensorManager.SENSOR_STATUS_ACCURACY_LOW: return "SENSOR_STATUS_ACCURACY_LOW";
			case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM: return "SENSOR_STATUS_ACCURACY_MEDIUM";
			case SensorManager.SENSOR_STATUS_UNRELIABLE: return "SENSOR_STATUS_UNRELIABLE";
			default: return null;
			}
		}
		
		/** The SensorEvent's timestamp in ns */
		public long getLastEventTimestamp() {
			return mLastTimestamp;
		}
		
		public int getLastEventAccuracy() {
			return mLastAccuracy;
		}
		
		public float[] getLastEventValues() {
			return mLastValues;
		}
		
		public String getCategory() {
			return mCategory;
		}
		
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			
			contents.put("Type", getTypeString());
			contents.put("Category", getCategory());
			contents.put("Default for Type", String.valueOf(isDefaultForType()));
			contents.put("Name", getName());
			contents.put("Vendor", getVendor());
			contents.put("Version", String.valueOf(getVersion()));
			contents.put("Power (mA)", String.valueOf(getPower()));
			contents.put("Resolution", String.valueOf(getResolution()));
			contents.put("Max Range", String.valueOf(getMaximumRange()));
			contents.put("Min Delay (us)", String.valueOf(getMinDelay()));
			contents.put("Last Event Timestmp (ns)", String.valueOf(getLastEventTimestamp()));
			contents.put("Last Event Accuracy", String.valueOf(getLastEventAccuracy()));
			contents.put("Last Event Accuracy Status", getLastAccuracyStatusString());
			
			if (mLastValues != null) {
				int i = 0;
				for (float v : getLastEventValues()) {
					contents.put("Last Event Values " + i, String.valueOf(v));
					++i;
				}
			}
			
			return contents;
		}
	}
	
	public boolean hasAccelerometerSensor() {
		return mHasAccelerometer;
	}
	
	public boolean hasAmbientTemperatureSensor() {
		return mHasAmbientTemperature;
	}
	
	public boolean hasGravitySensor() {
		return mHasGravity;
	}
	
	public boolean hasGyroscopeSensor() {
		return mHasGyroscope;
	}
	
	public boolean hasLightSensor() {
		return mHasLight;
	}
	
	public boolean hasLinearAccelerationSensor() {
		return mHasLinearAcceleration;
	}
	
	public boolean hasMagneticFieldSensor() {
		return mHasMagneticField;
	}
	
	public boolean hasOrientationSensor() {
		return mHasOrientation;
	}
	
	public boolean hasPressureSensor() {
		return mHasPressure;
	}
	
	public boolean hasProximitySensor() {
		return mHasProximity;
	}
	
	public boolean hasRelativeHumiditySensor() {
		return mHasRelativeHumidity;
	}
	
	public boolean hasRotationVectorSensor() {
		return mHasRotationVector;
	}
	
	public boolean hasTemperatureSensor() {
		return mHasTemperature;
	}
	
	public void startListening() {
		for (SensorWrapper sw : mSensors) {
			mSensorManager.registerListener(this, sw.getSensor(), 
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}
	
	public void stopListening() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		for (SensorWrapper sw : mSensors) {
			if (sw.getSensor() == sensor) {
				sw.setLastAccuracyStatus(accuracy);				
				if (mEventCallback != null) mEventCallback.onAccuracyChanged(sw);
				break;
			}
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		for (SensorWrapper sw : mSensors) {
			if (sw.getSensor() == event.sensor) {
				sw.setLastEventInfo(event.timestamp, event.accuracy, event.values.clone());
				if (mEventCallback != null) mEventCallback.onSensorChanged(sw);
				break;
			}
		}		
	}
	
	public float[] getOrientationInWorldCoordinateSystem() {
		if (!hasAccelerometerSensor() || !hasMagneticFieldSensor()) return null;
		float[] gravs = new float[3], geomags = new float[3];
		for (SensorWrapper sw : mSensors) {
			if (sw.getType() == Sensor.TYPE_ACCELEROMETER) {
				gravs = sw.getLastEventValues();
			}
			else if (sw.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				geomags = sw.getLastEventValues();
			}
		}
		if (gravs == null || geomags == null) return null;
		
		float[] rotationMatrix = new float[9], orientation = new float[3];
		if (SensorManager.getRotationMatrix(rotationMatrix, null, gravs, geomags)) {
			SensorManager.getOrientation(rotationMatrix, orientation);
		}
		return orientation;
	}
	
	/** Calculates the dew point in degrees Celsius */
	public float getDewPoint() {
		if (!hasRelativeHumiditySensor() || !hasAmbientTemperatureSensor()) return 0;
		float rh = 0, t = 0;
		float[] values;
		for (SensorWrapper sw : mSensors) {
			if (sw.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
				values = sw.getLastEventValues();
				if (values != null) rh = values[0];
			}
			else if (sw.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
				values = sw.getLastEventValues();
				if (values != null) t = values[0];
			}
		}
		
		double h = Math.log(rh / 100.0) + (17.62 * t) / (243.12 + t);
		return (float) (243.12 * h / (17.62 - h));
	}
	
	/** Calculates the absolute humidity in g/m^3 */
	public float getAbsoluteHumidity() {
		if (!hasRelativeHumiditySensor() || !hasAmbientTemperatureSensor()) return 0;
		float rh = 0, t = 0;
		float[] values;
		for (SensorWrapper sw : mSensors) {
			if (sw.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
				values = sw.getLastEventValues();
				if (values != null) rh = values[0];
			}
			else if (sw.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
				values = sw.getLastEventValues();
				if (values != null) t = values[0];
			}
		}
		
		return (float) (216.7 * (rh / 100.0 * 6.112 * Math.exp(17.62 * t / (243.12 + t)) / (273.15 + t)));
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> subcontents;
		int i = 0;
		for (SensorWrapper sw : mSensors) {
			subcontents = sw.getContents();
			for (String s : subcontents.keySet()) {			
				contents.put("Sensor " + i + " " + s, subcontents.get(s));
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
		contents.put("hasAccelerometerSensor", String.valueOf(hasAccelerometerSensor()));
		contents.put("hasAmbientTemperatureSensor", String.valueOf(hasAmbientTemperatureSensor()));
		contents.put("hasGravitySensor", String.valueOf(hasGravitySensor()));
		contents.put("hasGyroscopeSensor", String.valueOf(hasGyroscopeSensor()));
		contents.put("hasLightSensor", String.valueOf(hasLightSensor()));
		contents.put("hasLinearAccelerationSensor", String.valueOf(hasLinearAccelerationSensor()));
		contents.put("hasMagneticFieldSensor", String.valueOf(hasMagneticFieldSensor()));
		contents.put("hasOrientationSensor", String.valueOf(hasOrientationSensor()));
		contents.put("hasPressureSensor", String.valueOf(hasPressureSensor()));
		contents.put("hasProximitySensor", String.valueOf(hasProximitySensor()));
		contents.put("hasRelativeHumiditySensor", String.valueOf(hasRelativeHumiditySensor()));
		contents.put("hasRotationVectorSensor", String.valueOf(hasRotationVectorSensor()));
		contents.put("hasTemperatureSensor", String.valueOf(hasTemperatureSensor()));
		
		return contents;
	}
}
