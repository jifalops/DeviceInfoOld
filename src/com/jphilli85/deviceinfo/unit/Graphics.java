package com.jphilli85.deviceinfo.unit;

import java.util.LinkedHashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.os.Build;

public class Graphics extends Unit implements GLSurfaceView.Renderer {

	public interface OnGLSurfaceViewCreatedListener {
		void onGLSurfaceViewCreated();
	}
	
	private GL10 mGL;
	private OnGLSurfaceViewCreatedListener mListener;
	
	private String mRenderer;
	private String mVersion;
	private String mVendor;
	private String mMaxTextureSize;
	private String mMaxTextureUnits;
	private String mMaxTextureStackDepth;
	private String mExtensions;
	
	public Graphics(GLSurfaceView surfaceView) {
		mListener = null;
		surfaceView.setEGLContextClientVersion(2);
		surfaceView.setRenderer(this);
		surfaceView.onResume();
	}
	
	public void setOnGLSurfaceViewCreatedListener(OnGLSurfaceViewCreatedListener l) {
		mListener = l;
	}

	public float getOpenglVersion() {
		return Build.VERSION.SDK_INT >= 8 ? 2.0f : 1.1f;
	}
	
	
	public String getRenderer() {
		return mRenderer;
	}
	
	public String getVersion() {
		return mVersion;
	}
	
	public String getVendor() {
		return mVendor;
	}
	
	public String getMaxTextureSize() {
		return mMaxTextureSize;
	}
	
	public String getMaxTextureUnits() {
		return mMaxTextureUnits;
	}
	
	public String getMaxTextureStackDepth() {
		return mMaxTextureStackDepth;
	}
	
	public String getExtensions() {
		return mExtensions;
	}
	
	
	
	@Override
	public void onDrawFrame(GL10 gl) {

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		mRenderer = gl.glGetString(GL10.GL_RENDERER);
		mVersion = gl.glGetString(GL10.GL_VERSION);
		mVendor = gl.glGetString(GL10.GL_VENDOR);
		mMaxTextureSize = gl.glGetString(GL10.GL_MAX_TEXTURE_SIZE);
		mMaxTextureUnits = gl.glGetString(GL10.GL_MAX_TEXTURE_UNITS);
		mMaxTextureStackDepth = gl.glGetString(GL10.GL_MAX_TEXTURE_STACK_DEPTH);
		mExtensions = gl.glGetString(GL10.GL_EXTENSIONS);
		
		if (mListener != null) mListener.onGLSurfaceViewCreated();
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		contents.put("OpenGL Version", String.valueOf(getOpenglVersion()));
		contents.put("OpenGL Renderer", getRenderer());
		contents.put("OpenGL Version", getVersion());
		contents.put("OpenGL Vendor", getVendor());
		contents.put("OpenGL MaxTextureSize", getMaxTextureSize());
		contents.put("OpenGL MaxTextureUnits", getMaxTextureUnits());
		contents.put("OpenGL MaxTextureStackDepth", getMaxTextureStackDepth());
		contents.put("OpenGL Extensions", getExtensions());
		
		return contents;
	}
}
