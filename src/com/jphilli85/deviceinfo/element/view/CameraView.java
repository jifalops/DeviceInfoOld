package com.jphilli85.deviceinfo.element.view;

import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Camera;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Camera.CameraWrapper;


public class CameraView extends ElementView {
	private Camera mCamera;
	
	public CameraView() {
		this(DeviceInfo.getContext());
	}
	
	protected CameraView(Context context) {
		super(context);	
	}

	@Override
	public Element getElement() {
		return mCamera;
	}

	@Override
	protected void initialize(Context context) {
		mCamera = new Camera(context);
		
		TableSection table = new TableSection();		
		table.add("Number of Cameras", String.valueOf(mCamera.getNumCameras()));
		add(table);
		
		Section section;
		
		List<CameraWrapper> cameras = mCamera.getCameras();
		LinkedHashMap<String, String> params;
		for (int i = 0; i < cameras.size(); ++i) {
			section = new Section("Camera " + (i + 1));
			table = new TableSection();
			if (API >= 9) {
				table.add("Direction", cameras.get(i).getCameraDirection());
				table.add("Orientation (Degrees)", String.valueOf(cameras.get(i).getCameraOrientation()));
			}
			
			params = cameras.get(i).getCameraParametersMap();
			if (params != null) {		
				for (String key : params.keySet()) {
					table.add(key, params.get(key));					
				}
			}
			
			section.add(table);	
			add(section);
		}		
	}
}
