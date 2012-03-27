package com.jphilli85.deviceinfo.unit;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;

import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.ShellHelper;

public class Display extends Unit {
	
	// Use a shorter name
	private final int API = Build.VERSION.SDK_INT;
	
	private android.view.Display mDisplay;
    private DisplayMetrics mDisplayMetrics;
	
	public Display(Context context) {
		mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        mDisplay.getMetrics(mDisplayMetrics);     
	}
	
	public android.view.Display getDisplay() {
		return mDisplay;
	}
	
	public DisplayMetrics getDisplayMetrics() {
		return mDisplayMetrics;
	}
	
	/** Get the logical density */
	public float getDensity() {
		return mDisplayMetrics.density;
	}
	
	public int getDensityDpi() {
		return mDisplayMetrics.densityDpi;
	}
	
	public int getHeight() {
		return mDisplayMetrics.heightPixels;
	}
	
	public float getScaledDensity() {
		return mDisplayMetrics.scaledDensity;
	}
	
	public int getWidth() {
		return mDisplayMetrics.widthPixels;
	}
	
	public float getXDpi() {
		return mDisplayMetrics.xdpi;
	}
	
	public float getYDpi() {
		return mDisplayMetrics.ydpi;
	}
	
	// TODO put strings in resources to support multiple languages
	public String getDensityDpiString() {
		int dpi = mDisplayMetrics.densityDpi;
		if (dpi == DisplayMetrics.DENSITY_LOW) return "DENSITY_LOW";
		else if (dpi == DisplayMetrics.DENSITY_MEDIUM) return "DENSITY_MEDIUM";
		else if (dpi == DisplayMetrics.DENSITY_HIGH) return "DENSITY_HIGH";
		else if (API >= 9 && dpi == DisplayMetrics.DENSITY_XHIGH) return "DENSITY_XHIGH";
		else if (API >= 13 && dpi == DisplayMetrics.DENSITY_TV) return "DENSITY_TV";
		else return null;		
	}
	
	
	public float getWidthInches() {
		return mDisplayMetrics.widthPixels / mDisplayMetrics.xdpi;
	}
	
	public float getHeightInches() {
		return mDisplayMetrics.heightPixels / mDisplayMetrics.ydpi;
	}
	
	public double getDiagonal() {
		return Math.sqrt(Math.pow(mDisplayMetrics.widthPixels, 2) 
				+ Math.pow(mDisplayMetrics.heightPixels, 2));
	}
	
	public double getDiagonalInches() {
        return Math.sqrt(Math.pow(getWidthInches(), 2) 
        		+ Math.pow(getHeightInches(), 2));
    }
	
	public float getRefreshRate() { 
		return mDisplay.getRefreshRate(); 
	}

    public int getRotation() { 
        if (API < 8) return mDisplay.getOrientation();
        else return mDisplay.getRotation();
    }
    
    // TODO check with api7
    public int getRotationDegrees() {
    	return getRotation() * 90;
    }
    
    public int getPixelFormat() {
    	return mDisplay.getPixelFormat();
    }

    // TODO ui facing strings, put in graphics
    public String getPixelFormatString() {
    	int format = mDisplay.getPixelFormat();
    	
        if (API < 8) {
            if (format == PixelFormat.JPEG) return "JPEG";
            if (format == PixelFormat.YCbCr_420_SP) return "YCbCr_420_SP";
            if (format == PixelFormat.YCbCr_422_I) return "YCbCr_422_I";
            if (format == PixelFormat.YCbCr_422_SP) return "YCbCr_422_SP";            
        }
        else {
            if (format == ImageFormat.JPEG) return "JPEG";
            if (format == ImageFormat.NV21) return "NV21";
            if (format == ImageFormat.YUY2) return "YUY2"; 
            if (format == ImageFormat.NV16) return "NV16";                          
        }        
        if (format == PixelFormat.L_8) return "L_8";
        if (format == PixelFormat.LA_88) return "LA_88";
        if (format == PixelFormat.OPAQUE) return "OPAQUE";
        if (format == PixelFormat.RGB_332) return "RGB_332";
        if (format == PixelFormat.RGB_565) return "RGB_565";
        if (format == PixelFormat.RGB_888) return "RGB_888";
        if (format == PixelFormat.RGBA_4444) return "RGBA_4444";
        if (format == PixelFormat.RGBA_5551) return "RGBA_5551";
        if (format == PixelFormat.RGBA_8888) return "RGBA_8888";
        if (format == PixelFormat.RGBX_8888) return "RGBX_8888";
        if (format == PixelFormat.TRANSLUCENT) return "TRANSLUCENT";
        if (format == PixelFormat.TRANSPARENT) return "TRANSPARENT";
        if (format == PixelFormat.UNKNOWN) return "Unknown";
        return null;
    }    
    
    // TODO touch test
    public int getMaxSimultaneousTouch() {
    	String s = ShellHelper.getProp("ro.product.max_num_touch");
    	if (s == null || !s.matches("[0-9]{2}")) return -1;
    	return Integer.valueOf(s);
    }
    
    public boolean isTouchScreen(Context context) {
        Configuration config = context.getResources().getConfiguration();            
        return (config.touchscreen == Configuration.TOUCHSCREEN_FINGER ||
                config.touchscreen == Configuration.TOUCHSCREEN_STYLUS);            
    }
    
    public float getFontScale(Context context) {
    	return context.getResources().getConfiguration().fontScale;
    }
    
    // TODO ui facing strings
    public String getOrientationString(Context context) {    	
    	switch (context.getResources().getConfiguration().orientation) {
    	case Configuration.ORIENTATION_PORTRAIT: return "ORIENTATION_PORTRAIT";
    	case Configuration.ORIENTATION_LANDSCAPE: return "ORIENTATION_LANDSCAPE";
    	case Configuration.ORIENTATION_SQUARE: return "ORIENTATION_SQUARE";
    	case Configuration.ORIENTATION_UNDEFINED: return "ORIENTATION_UNDEFINED";
    	default: return null;
    	}
    }
    
    public int getScreenHeightDp(Context context) {
    	if (API >= 13) 
    		return context.getResources().getConfiguration().screenHeightDp;
    	else return 0;
    }
    
    public int getScreenWidthDp(Context context) {
    	if (API >= 13) 
    		return context.getResources().getConfiguration().screenWidthDp;
    	else return 0;
    }
    
    //TODO ui facing strings
    public String getScreenSizeString(Context context) {
    	int mask = context.getResources().getConfiguration().screenLayout;
    	if ((mask & Configuration.SCREENLAYOUT_SIZE_SMALL) > 0) return "SMALL"; 
    	else if ((mask & Configuration.SCREENLAYOUT_SIZE_NORMAL) > 0) return "NORMAL";
    	else if ((mask & Configuration.SCREENLAYOUT_SIZE_LARGE) > 0) return "LARGE";
    	else if ((API >= 9) && ((mask & Configuration.SCREENLAYOUT_SIZE_XLARGE) > 0)) return "XLARGE";
    	else if ((mask & Configuration.SCREENLAYOUT_SIZE_UNDEFINED) > 0) return "UNDEFINED";
    	else return null;    	
    }

    public boolean isScreenLong(Context context) {
    	int mask = context.getResources().getConfiguration().screenLayout;
    	return ((mask & Configuration.SCREENLAYOUT_LONG_YES) > 0);
    }
    
    public int getSmallestScreenWidthDp(Context context) {
    	if (API >= 13) 
    		return context.getResources().getConfiguration().smallestScreenWidthDp;
    	else return 0;
    }
    
    @Override
	public Map<String, String> getContents() {
		Map<String, String> contents = new HashMap<String, String>();
		Context context = DeviceInfo.sAppContext;
		
		contents.put("Density DPI String", getDensityDpiString());
		contents.put("Density DPI", String.valueOf(getDensityDpi()));
		contents.put("X DPI", String.valueOf(getXDpi()));
		contents.put("Y DPI", String.valueOf(getYDpi()));
		contents.put("Density", String.valueOf(getDensity()));
		contents.put("Scaled Density", String.valueOf(getScaledDensity()));
		contents.put("Font Scale", String.valueOf(getFontScale(context)));
		contents.put("Width (pixel)", String.valueOf(getWidth()));
		contents.put("Height (pixel)", String.valueOf(getHeight()));
		contents.put("Diagonal (pixel)", String.valueOf(getDiagonal()));
		contents.put("Width (inch)", String.valueOf(getWidthInches()));
		contents.put("Height (inch)", String.valueOf(getHeightInches()));
		contents.put("Diagonal (inch)", String.valueOf(getDiagonalInches()));
		contents.put("Refresh Rate", String.valueOf(getRefreshRate()));
		contents.put("Rotation (degrees)", String.valueOf(getRotationDegrees()));
		contents.put("Pixel Format", getPixelFormatString());
		contents.put("Is Touch Screen", String.valueOf(isTouchScreen(context)));
		contents.put("Max Simultaneous Touch", String.valueOf(getMaxSimultaneousTouch()));
		contents.put("Screen Size String", getScreenSizeString(context));
		contents.put("Is Screen Long", String.valueOf(isScreenLong(context)));
		contents.put("Orientation String", getOrientationString(context));
		contents.put("Screen Height DP", String.valueOf(getScreenHeightDp(context)));
		contents.put("Screen Width DP", String.valueOf(getScreenWidthDp(context)));
		contents.put("Smallest Screen Width DP", String.valueOf(getSmallestScreenWidthDp(context)));
		
		return contents;
	}
}
