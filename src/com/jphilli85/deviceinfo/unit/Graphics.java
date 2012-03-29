package com.jphilli85.deviceinfo.unit;

import java.util.LinkedHashMap;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import android.os.Build;

public class Graphics extends Unit {

	private GL10 mGL;
	
	public Graphics() {
		
		EGL10 egl = (EGL10) EGLContext.getEGL();
		EGLDisplay disp = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
		egl.eglInitialize(disp, new int[] {1, 0});
		int[] configSpec = {EGL10.EGL_NONE};
		EGLConfig[] config = new EGLConfig[1];
		int num_configs[] = new int[1];
		egl.eglChooseConfig(disp, configSpec, config, 1, num_configs);		
		
		EGLContext eglContext = egl.eglCreateContext(
				disp, 
				config[0], 
				EGL10.EGL_NO_CONTEXT, 
				null);
		mGL = (GL10) eglContext.getGL();
		
		EGLSurface draw = new EGLSurface() {};
		EGLSurface read = new EGLSurface() {};
		
		//egl.eglMakeCurrent(disp, draw, read, eglContext);
	}

	public float getOpenglVersion() {
		return Build.VERSION.SDK_INT >= 8 ? 2.0f : 1.1f;
	}
	
	
	public String getRenderer() {
		return mGL.glGetString(GL10.GL_RENDERER);
	}
	
	public String getVersion() {
		return mGL.glGetString(GL10.GL_VERSION);
	}
	
	public String getVendor() {
		return mGL.glGetString(GL10.GL_VENDOR);
	}
	
	public String getMaxTextureSize() {
		return mGL.glGetString(GL10.GL_MAX_TEXTURE_SIZE);
	}
	
	public String getMaxTextureUnits() {
		return mGL.glGetString(GL10.GL_MAX_TEXTURE_UNITS);
	}
	
	public String getMaxTextureStackDepth() {
		return mGL.glGetString(GL10.GL_MAX_TEXTURE_STACK_DEPTH);
	}
	
	public String getExtensions() {
		return mGL.glGetString(GL10.GL_EXTENSIONS);
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
