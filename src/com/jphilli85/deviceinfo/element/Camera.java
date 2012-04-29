package com.jphilli85.deviceinfo.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.jphilli85.deviceinfo.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.util.Log;

public class Camera extends Element {
	private static final String LOG_TAG = Camera.class.getSimpleName();
	private static final int API = Build.VERSION.SDK_INT;
	
	public final String FACING_FRONT;
	public final String FACING_BACK;
	
	private final List<CameraWrapper> mCameras;
	private final int mNumCameras;
	
	public Camera(Context context) {
		FACING_FRONT = context.getString(R.string.camera_facing_front);
		FACING_BACK = context.getString(R.string.camera_facing_back);
		
		mCameras = new ArrayList<CameraWrapper>();
		
		if (API >= 9) {
			mNumCameras = android.hardware.Camera.getNumberOfCameras();
			CameraInfo info = new CameraInfo();
			CameraWrapper cw;
			for (int i = 0; i < mNumCameras; ++i) {
				android.hardware.Camera.getCameraInfo(i, info);
				cw = new CameraWrapper(getCamera(i));
				cw.setCameraInfo(info);
				mCameras.add(cw);	
			}
		}
		else if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			/* 
			 * PackageManager.FEATURE_CAMERA_FRONT wasn't introduced until API 9, 
			 * as well as Camera.getNumberOfCameras(). I'm going to assume that there 
			 * are no devices with multiple cameras on the back before API 9.
			 * If there are, this class will only see the first.
			 */ 
			mNumCameras = 1;
			mCameras.add(new CameraWrapper(getCamera()));
		}
		else {
			mNumCameras = 0;
		}
		
		// Important!
		releaseCameras();
	}
	
	/** Camera facing front or back. */
	public String getCameraDirection(int facing) {
		switch (facing) {
		case CameraInfo.CAMERA_FACING_BACK: return FACING_BACK;
		case CameraInfo.CAMERA_FACING_FRONT: return FACING_FRONT;
		default: return null;
		}
	}
	

	private android.hardware.Camera getCamera(int id) {
		android.hardware.Camera c = null;
	    try {
	    	if (API >= 9) c = android.hardware.Camera.open(id);
	    	else c = getCamera();
	    }
	    catch (Exception ignored){
	    	Log.e(LOG_TAG, "Error opening camera " + id);
	    }
	    return c;
	}
	
	private android.hardware.Camera getCamera() {
		android.hardware.Camera c = null;
	    try { c = android.hardware.Camera.open(); }
	    catch (Exception ignored) {
	    	Log.e(LOG_TAG, "Error opening camera.");
	    }
	    return c;
	}
	
	public class CameraWrapper {
		private android.hardware.Camera mCamera;
		private CameraInfo mCameraInfo;
		private Parameters mParameters;
		private LinkedHashMap<String, String> mParametersMap;
		
		public CameraWrapper(android.hardware.Camera camera) {
			if (camera == null) return;
			
			mCamera = camera;
			mParameters = camera.getParameters();
			mParametersMap = parseParameters(mParameters, true);
		}
				
		private LinkedHashMap<String, String> parseParameters(Parameters cameraParams, boolean replaceHyphen) {
			if (cameraParams == null) return null;
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			String[] params = cameraParams.flatten().split(";");
			String[] pair = null;
			String key = null;
			for (String s : params) {
				pair = s.split("=", 2);				
				if (pair.length != 2) {
					Log.d(LOG_TAG, "Parsing camera parameters resulted in an array with length " + pair.length);
				}
				key = pair[0].trim();
				if (replaceHyphen) key = key.replace("-", " ");
				map.put(key, pair[1].trim());
			}			
			return map;
		}
		
		private void setCameraInfo(CameraInfo info) {
			mCameraInfo = info;
		}
		
		public android.hardware.Camera getCamera() {
			return mCamera;
		}
		
		public CameraInfo getCameraInfo() {
			if (API >= 9) return mCameraInfo;
			else return null;
		}
		
		public Parameters getCameraParameters() {
			return mParameters;
		}
		
		public LinkedHashMap<String, String> getCameraParametersMap() {
			return mParametersMap;
		}
		
		public String getCameraDirection() {
			if (API < 9 || mCameraInfo == null) return null;
			return Camera.this.getCameraDirection(mCameraInfo.facing);
		}
		
		/**  		 
		 * @return Orientation in degrees (0, 90, 180, 270),
		 * or -1 if not supported or an error occurred
		 */
		public int getCameraOrientation() {
			if (API < 9 || mCameraInfo == null) return -1;
			return mCameraInfo.orientation;
		}
	}

	private void releaseCameras() {
		for (CameraWrapper cw : mCameras) {
			android.hardware.Camera c = cw.getCamera();
			if (c != null) c.release();
		}
	}
	
	public int getNumCameras() {
		return mNumCameras;
	}
	
	public List<CameraWrapper> getCameras() {
		return mCameras;
	}
	

	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		contents.put("Number of Cameras", String.valueOf(mNumCameras));
		
		List<CameraWrapper> cameras = getCameras();
		LinkedHashMap<String, String> params;
		int paramIndex = 0;
		for (int i = 0; i < cameras.size(); ++i) {
			if (API >= 9) {
				contents.put("Camera " + i + " Direction", cameras.get(i).getCameraDirection());
				contents.put("Camera " + i + " Orientation (Degrees)", String.valueOf(cameras.get(i).getCameraOrientation()));
			}
			
			params = cameras.get(i).getCameraParametersMap();
			if (params != null) {		
				for (String key : params.keySet()) {
					contents.put("Camera " + i + " Parameter " + paramIndex, key + " = " + params.get(key));
					++paramIndex;
				}
			}
		}
		
		return contents;
	}

}
