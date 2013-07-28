package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.hardware.Sensor;
import android.widget.TextView;

import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Sensors;
import com.jphilli85.deviceinfo.element.Sensors.SensorWrapper;

public class SensorsView extends ListeningElementView implements Sensors.Callback, PlayableSection.Callback {	
	private Sensors mSensors;
	
	private TextView[] 		
		mAccuracy, mValue0,
		mValue1, mValue2,
		mValue3;
	
	private TextView
		mWorldX, mWorldY,
		mWorldZ, 
		mDewPoint, mHumidity;
	
	private PlayableSection[] mPlayables;
		
	public SensorsView(Context context) {
		super(context);
		
	}
	
	private TableSection getValuesTable(SensorWrapper sw, int index) {
		TableSection table = new TableSection(getContext()) ;
		if (sw == null) return table;
		
		table.add("Accuracy", mAccuracy[index]);
		
		switch (sw.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			table.add("X Acceleration (" + sw.getUnit() + ")", mValue0[index]);
			table.add("Y Acceleration (" + sw.getUnit() + ")", mValue1[index]);
			table.add("Z Acceleration (" + sw.getUnit() + ")", mValue2[index]);		
			return table;	
		case Sensor.TYPE_GYROSCOPE:
			table.add("X Angular Speed (" + sw.getUnit() + ")", mValue0[index]);
			table.add("Y Angular Speed (" + sw.getUnit() + ")", mValue1[index]);
			table.add("Z Angular Speed (" + sw.getUnit() + ")", mValue2[index]);		
			return table;
		case Sensor.TYPE_LIGHT:
			table.add("Ambient light level (" + sw.getUnit() + ")", mValue0[index]);		
			return table;	
		case Sensor.TYPE_MAGNETIC_FIELD:
			table.add("X Magnetic Field (" + sw.getUnit() + ")", mValue0[index]);
			table.add("Y Magnetic Field (" + sw.getUnit() + ")", mValue1[index]);
			table.add("Z Magnetic Field (" + sw.getUnit() + ")", mValue2[index]);		
			return table;
		case Sensor.TYPE_ORIENTATION:
			table.add("Azimuth (" + sw.getUnit() + ")", mValue0[index]);
			table.add("Pitch (" + sw.getUnit() + ")", mValue1[index]);
			table.add("Roll (" + sw.getUnit() + ")", mValue2[index]);	
			return table;
		case Sensor.TYPE_PRESSURE:
			table.add("Atmospheric Pressure (" + sw.getUnit() + ")", mValue0[index]);		
			return table;
		case Sensor.TYPE_PROXIMITY:
			table.add("Proximity (" + sw.getUnit() + ")", mValue0[index]);		
			return table;
		case Sensor.TYPE_TEMPERATURE:
			table.add("Temperature (" + sw.getUnit() + ")", mValue0[index]);		
			return table;
		}
		
		if (API >= 9) {
			switch (sw.getType()) {
			case Sensor.TYPE_GRAVITY:
				table.add("X Gravity (" + sw.getUnit() + ")", mValue0[index]);
				table.add("Y Gravity (" + sw.getUnit() + ")", mValue1[index]);
				table.add("Z Gravity (" + sw.getUnit() + ")", mValue2[index]);			
				return table;
			case Sensor.TYPE_LINEAR_ACCELERATION:
				table.add("X Linear Acceleration (" + sw.getUnit() + ")", mValue0[index]);
				table.add("Y Linear Acceleration (" + sw.getUnit() + ")", mValue1[index]);
				table.add("Z Linear Acceleration (" + sw.getUnit() + ")", mValue2[index]);			
				return table;
			case Sensor.TYPE_ROTATION_VECTOR:
				table.add("X Rotation Vector (unitless)", mValue0[index]);
				table.add("Y Rotation Vector (unitless)", mValue1[index]);
				table.add("Z Rotation Vector (unitless)", mValue2[index]);
				table.add("Extra Rotation Vector (unitless)", mValue3[index]);
				return table;
			}			
		}
		
		if (API >= 14) {
			switch (sw.getType()) {
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
				table.add("Ambient Temp (" + sw.getUnit() + ")", mValue0[index]);		
				return table;
			case Sensor.TYPE_RELATIVE_HUMIDITY:
				table.add("Relative humidity (" + sw.getUnit() + ")", mValue0[index]);			
				return table;
			}
		}
		
		return table;
	}
	
	private Subsection getSensorSubsection(final SensorWrapper sw, int index) {
		Subsection subsection = new Subsection(getContext(), sw.getTypeString());
		Subsection subsection2 = new Subsection(getContext(), "Properties");
		TableSection table = new TableSection(getContext()) ;
		
		table.add("Name", sw.getName());
		table.add("Vendor", sw.getVendor());
		table.add("Version", String.valueOf(sw.getVersion()));
		table.add("Default", String.valueOf(sw.isDefaultForType()));
		table.add("Power (mA)", String.valueOf(sw.getPower()));
		table.add("Resolution (" + sw.getUnit() + ")", String.valueOf(sw.getResolution()));
		table.add("Max Range (" + sw.getUnit() + ")", String.valueOf(sw.getMaximumRange()));
		table.add("Min Delay (us)", String.valueOf(sw.getMinDelay()));
		
		subsection2.add(table);
		subsection.add(subsection2);
		
		mPlayables[index] = new Subsection(getContext(), "Values");	
		
		 
		
		mPlayables[index].setCallback(this);
		
		mPlayables[index].add(getValuesTable(sw, index));
		subsection.add(mPlayables[index]);
		
		return subsection;
	}

	@Override
	public Element getElement() {		
		return mSensors;
	}

	@Override
	protected void initialize(Context context) {
		mSensors = new Sensors(context);		
		mSensors.setCallback(this);
		
		SensorWrapper[] sensors = mSensors.getAllSensors();
		int count = sensors.length;
		
		mPlayables = new PlayableSection[count];
		
		mAccuracy = new TextView[count];
		mValue0 = new TextView[count];
		mValue1 = new TextView[count];
		mValue2 = new TextView[count];
		mValue3 = new TextView[count];
		
		Section section;
		Subsection subsection;
		TableSection table;
		
		section = new Section(getContext(), "Aggregate Data");
		boolean agg = false;
		if (mSensors.getAccelerometerSensors().size() > 0
				&& mSensors.getMagneticFieldSensors().size() > 0) {
			agg = true;
			table = new TableSection(getContext()) ;
			mWorldX = table.getValueTextView();
			mWorldY = table.getValueTextView();
			mWorldZ = table.getValueTextView();
			
			
			subsection = new Subsection(getContext(), "Orientation (World Coordinates)");			
			table.add("X", mWorldX);
			table.add("Y", mWorldY);
			table.add("Z", mWorldZ);
			
			subsection.add(table);
			section.add(subsection);
		}
		if (mSensors.getRelativeHumiditySensors().size() > 0
				&& mSensors.getAmbientTemperatureSensors().size() > 0) {
			agg = true;
			table = new TableSection(getContext()) ;
			
			mDewPoint = table.getValueTextView();
			mHumidity = table.getValueTextView();
			
			table.add("Dew Point (C)", mDewPoint);
			table.add("Absolute Humidity (g/m^3)", mHumidity);
			section.add(table);
		}
		if (!agg) {
			table = new TableSection(getContext()) ;
			table.add(null, "No aggregate data from sensor combinations");
			section.add(table);
		}
		add(section);
		
		section = new Section(getContext(), "Motion Sensors");
		table = new TableSection(getContext()) ;
		for (int i = 0; i < count; ++i) {			
			mAccuracy[i] = table.getValueTextView();
			mValue0[i] = table.getValueTextView();
			mValue1[i] = table.getValueTextView();
			mValue2[i] = table.getValueTextView();
			mValue3[i] = table.getValueTextView();
			
			if (sensors[i].getCategory() == Sensors.CATEGORY_MOTION) {							
				section.add(getSensorSubsection(sensors[i], i));
			}
		}			
		add(section);
		
		section = new Section(getContext(), "Position Sensors");		
		for (int i = 0; i < count; ++i) {		
			if (sensors[i].getCategory() == Sensors.CATEGORY_POSITION) {
				section.add(getSensorSubsection(sensors[i], i));
			}
		}
		add(section);
		
		section = new Section(getContext(), "Environment Sensors");		
		for (int i = 0; i < count; ++i) {		
			if (sensors[i].getCategory() == Sensors.CATEGORY_ENVIRONMENT) {
				section.add(getSensorSubsection(sensors[i], i));
			}
		}
		add(section);
	}

	@Override
	public void onAccuracyChanged(SensorWrapper sensorWrapper) {
		int index =	mSensors.getSensorIndex(sensorWrapper);
		if (index < 0) return;
		mAccuracy[index].setText(sensorWrapper.getAccuracyStatusString());
	}

	@Override
	public void onSensorChanged(SensorWrapper sensorWrapper) {
		int index =	mSensors.getSensorIndex(sensorWrapper);
		if (index < 0) return;
		float[] values = sensorWrapper.getLastValues();
		if (values == null) return;
        mAccuracy[index].setText(sensorWrapper.getAccuracyString());
        switch (values.length) {
		case 4: mValue3[index].setText(String.valueOf(values[3]));
		case 3: mValue2[index].setText(String.valueOf(values[2]));
		case 2: mValue1[index].setText(String.valueOf(values[1]));
		case 1: mValue0[index].setText(String.valueOf(values[0]));
		}
		
		switch (sensorWrapper.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
		case Sensor.TYPE_MAGNETIC_FIELD:
			if (mWorldX == null) break;
			float[] coords = mSensors.getOrientationInWorldCoordinateSystem();
			if (coords == null || coords.length != 3) break;
			mWorldX.setText(String.valueOf(coords[0]));
			mWorldY.setText(String.valueOf(coords[1]));
			mWorldZ.setText(String.valueOf(coords[2]));
			break;
		}
		if (API >= 14) {
			switch (sensorWrapper.getType()) {
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
			case Sensor.TYPE_RELATIVE_HUMIDITY:
				if (mDewPoint == null) break;
				mDewPoint.setText(String.valueOf(mSensors.getDewPoint()));
				mHumidity.setText(String.valueOf(mSensors.getAbsoluteHumidity()));
			}
		}
	}
	
	@Override
	public void onPlay(PlayableSection section) {		
		super.onPlay(section);
		if (section == mHeader) {
			for (PlayableSection ps : mPlayables) {
				ps.setPauseIcon();
			}
		}
		else {
			section.setPauseIcon();
			mHeader.setPauseIcon();
		}
	}
	
	@Override
	public void onPause(PlayableSection section) {
		super.onPause(section);
		if (section == mHeader) {
			for (PlayableSection ps : mPlayables) {
				ps.setPlayIcon();
			}
		}
		else {
			section.setPlayIcon();
			for (PlayableSection ps : mPlayables) {
				if (ps.isPlaying()) return;
			}
			mHeader.setPlayIcon();
		}
	}
}