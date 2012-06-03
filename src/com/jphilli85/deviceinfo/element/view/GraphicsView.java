package com.jphilli85.deviceinfo.element.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup.LayoutParams;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Graphics;


public class GraphicsView extends ElementView implements Graphics.Callback {
	private Graphics mGraphics;
	private final GLSurfaceView mGlSurfaceView;
	
	public GraphicsView() {
		this(DeviceInfo.getContext());
	}
	
	protected GraphicsView(Context context) {
		super(context);
		mGlSurfaceView = new GLSurfaceView(context);
		mGlSurfaceView.setLayoutParams(new LayoutParams(1, 1));
		
	}

	@Override
	public Element getElement() {
		return mGraphics;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {
			
			@Override
			public void run() {
				showElementContents();			
				mGraphics.stopListening();
			}
		});
//		Log.d("GraphicsView", "onSurfaceCreated: Main thread? " + DeviceInfo.isOnMainThread());
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
//		Log.d("GraphicsView", "onSurfaceChanged: Main thread? " + DeviceInfo.isOnMainThread());
	}

	@Override
	public void onDrawFrame(GL10 gl) {
//		Log.d("GraphicsView", "onDrawFrame: Main thread? " + DeviceInfo.isOnMainThread());
	}

	@Override
	protected void initialize(Context context) {
		mGraphics = new Graphics(mGlSurfaceView);		
		mGraphics.setCallback(this);
		
		add(mGlSurfaceView);
		mGraphics.startListening();
	}
}
