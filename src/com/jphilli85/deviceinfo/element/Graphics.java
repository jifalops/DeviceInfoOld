package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.jphilli85.deviceinfo.app.DeviceInfo;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;

// TODO use multiple egl contexts (1 gives different results than 2,
// even if still using GLES20 methods.

// TODO more values & limits
public class Graphics extends ListeningElement implements GLSurfaceView.Renderer {
	public static final float OPENGLES_VERSION_10 = 1.0f;
	public static final float OPENGLES_VERSION_11 = 1.1f;
	public static final float OPENGLES_VERSION_20 = 2.0f;

	public interface Callback extends ListeningElement.Callback {
		/** Corresponds to GLSurfaceView.Renderer.onSurfaceCreated(); */
		void onSurfaceCreated(GL10 gl, EGLConfig config);
		/** Corresponds to GLSurfaceView.Renderer.onSurfaceChanged(); */
		void onSurfaceChanged(GL10 gl, int width, int height);
		/** Corresponds to GLSurfaceView.Renderer.onDrawFrame(); */
		void onDrawFrame(GL10 gl);
	}
	
	private final float mOpenGlesVersion;
	private OpenGles mOpenGles;
	private final GLSurfaceView mGlSurfaceView;
	
	public Graphics(GLSurfaceView glSurfaceView) {
		mGlSurfaceView = glSurfaceView;
		String ver = openGlesVersion(DeviceInfo.getContext());
		mOpenGlesVersion = ver == null ? 0.0f : Float.valueOf(ver);

		if (Build.VERSION.SDK_INT >= 8) {
			glSurfaceView.setEGLContextClientVersion((int) mOpenGlesVersion);
		}

		glSurfaceView.setRenderer(this);
		glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	/** 
	 * Gets the highest supported OpenGL version as a string 
	 * representation of a floating point number.
	 */
	public static String openGlesVersion(Context context) {
		return ((ActivityManager) context
			.getSystemService(Context.ACTIVITY_SERVICE))
			.getDeviceConfigurationInfo().getGlEsVersion();
	}
	
	public OpenGles getOpenGles() {
		return mOpenGles;
	}
	
	public GLSurfaceView getGlSurfaceView() {
		return mGlSurfaceView;
	}
	
	/** 
	 * Get the highest supported OpenGL ES version.
	 * This will be available immediately after instantiation
	 * whereas getOpenGles() will only be ready after the GLSurfaceView
	 * has been created.	 
	 */
	public float getOpenGlesVersion() {
		return mOpenGlesVersion;
	}
	
	
	public void onPause() {
		mGlSurfaceView.onPause();
	}
	
	public void onResume() {
		mGlSurfaceView.onResume();
	}
	
	
	private abstract class OpenGles implements ContentsMapper {
		protected final float mOpenGlesVersion;
		
		protected String mRenderer;
		protected String mVersion;
		protected String mVendor;
		protected int mMaxTextureSize;
				
		protected String[] mExtensions;
		
		public OpenGles() {
			mOpenGlesVersion = Graphics.this.mOpenGlesVersion;
		}
		
		public float getOpenGlesVersion() {
			return mOpenGlesVersion;
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
		
		public int getMaxTextureSize() {
			return mMaxTextureSize;
		}	

		public String[] getExtensions() {
			return mExtensions;
		}
		
		protected int getInt(int glConst) {
			int[] placeholder = {0};		
			if (mOpenGlesVersion == OPENGLES_VERSION_10) 
				GLES10.glGetIntegerv(glConst, placeholder, 0);
			else if (mOpenGlesVersion == OPENGLES_VERSION_11) 
				GLES11.glGetIntegerv(glConst, placeholder, 0);
			else if (mOpenGlesVersion == OPENGLES_VERSION_20) 
				GLES20.glGetIntegerv(glConst, placeholder, 0);
			return placeholder[0];
		}
		
		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			
			contents.put("Version", String.valueOf(getOpenGlesVersion()));
			contents.put("Renderer", getRenderer());
			contents.put("Version", getVersion());
			contents.put("Vendor", getVendor());
			contents.put("MaxTextureSize", String.valueOf(getMaxTextureSize()));	
			String[] extensions = getExtensions();
			if (extensions != null) {
				for (int i = 0; i < extensions.length; ++i) {
					contents.put("Extension " + i, extensions[i]);
				}
			}
			return contents;
		}
	}
	
	private class OpenGles10 extends OpenGles {
		protected int mMaxTextureUnits;
		protected int mMaxTextureStackDepth;
		
		public OpenGles10() {
			super();
			mRenderer = GLES10.glGetString(GLES10.GL_RENDERER);
			mVersion = GLES10.glGetString(GLES10.GL_VERSION);
			mVendor = GLES10.glGetString(GLES10.GL_VENDOR);
			mMaxTextureSize = getInt(GLES10.GL_MAX_TEXTURE_SIZE);				
			mExtensions = GLES10.glGetString(GLES10.GL_EXTENSIONS).split(" ");
				
			mMaxTextureUnits = getInt(GLES10.GL_MAX_TEXTURE_UNITS);		
			mMaxTextureStackDepth = getInt(GLES10.GL_MAX_TEXTURE_STACK_DEPTH);
		}
		
		public int getMaxTextureUnits() {
			return mMaxTextureUnits;
		}
		
		public int getMaxTextureStackDepth() {
			return mMaxTextureStackDepth;
		}

		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = super.getContents();
			contents.put("MaxTextureUnits", String.valueOf(getMaxTextureUnits()));
			contents.put("MaxTextureStackDepth", String.valueOf(getMaxTextureStackDepth()));
			return contents;
		}
	}
	
	private class OpenGles11 extends OpenGles10 {
		public OpenGles11() {
			super();
		}		
	}
	
	private class OpenGles20 extends OpenGles {
		protected int mMaxTextureImageUnits;
		protected int mMaxRenderBufferSize;

		public OpenGles20() {
			super();
			mRenderer = GLES20.glGetString(GLES20.GL_RENDERER);
			mVersion = GLES20.glGetString(GLES20.GL_VERSION);
			mVendor = GLES20.glGetString(GLES20.GL_VENDOR);
			mMaxTextureSize = getInt(GLES20.GL_MAX_TEXTURE_SIZE);							
			mExtensions = GLES20.glGetString(GLES20.GL_EXTENSIONS).split(" ");
			
			mMaxTextureImageUnits = getInt(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS);
			mMaxRenderBufferSize = getInt(GLES20.GL_MAX_RENDERBUFFER_SIZE);
		}
		
		public int getMaxTextureImageUnits() {
			return mMaxTextureImageUnits;
		}
		
		public int getMaxRenderBufferSize() {
			return mMaxRenderBufferSize;
		}

		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = super.getContents();
			contents.put("MaxTextureImageUnits", String.valueOf(getMaxTextureImageUnits()));
			contents.put("MaxRenderBufferSize", String.valueOf(getMaxRenderBufferSize()));
			return contents;
		}
	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		
		if (mOpenGles == null) {
			if (mOpenGlesVersion == OPENGLES_VERSION_10) mOpenGles = new OpenGles10();
			else if (mOpenGlesVersion == OPENGLES_VERSION_11) mOpenGles = new OpenGles11();
			else if (mOpenGlesVersion == OPENGLES_VERSION_20) mOpenGles = new OpenGles20();
		}
		if ((Callback) getCallback() != null) ((Callback) getCallback()).onSurfaceCreated(gl, config);
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		if ((Callback) getCallback() != null) ((Callback) getCallback()).onSurfaceChanged(gl, width, height);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		
		if ((Callback) getCallback() != null) ((Callback) getCallback()).onDrawFrame(gl);
	}

	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = super.getContents();
		contents.put("OpenGL ES Version", String.valueOf(getOpenGlesVersion()));
		if (mOpenGles != null) contents.putAll(mOpenGles.getContents());		
		return contents;
	}
	
	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		if (!super.startListening(onlyIfCallbackSet)) return false;
		onResume();
		return setListening(true);
	}
	
	@Override
	public boolean stopListening() {
		if (!super.stopListening()) return false;
		onPause();
		return !setListening(false);
	}
}
