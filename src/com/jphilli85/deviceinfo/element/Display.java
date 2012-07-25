package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.ShellHelper;
import com.jphilli85.deviceinfo.app.DeviceInfo;

public class Display extends Element {
	private static final int API = Build.VERSION.SDK_INT;
	
	private android.view.Display mDisplay;
    private DisplayMetrics mDisplayMetrics;
    
    // DisplayMetrics constants
    public final String DENSITY_LOW;
    public final String DENSITY_MEDIUM;
    public final String DENSITY_HIGH;
    public final String DENSITY_XHIGH;
    public final String DENSITY_TV;    
	// ImageFormat constants
	public final String FORMAT_JPEG;
	public final String FORMAT_NV21;
	public final String FORMAT_YUY2;
	public final String FORMAT_NV16;	
	// PixelFormat constants
	public final String FORMAT_YCBCR_420_SP;
	public final String FORMAT_YCBCR_422_I;
	public final String FORMAT_YCBCR_422_SP;
	public final String FORMAT_L_8;
	public final String FORMAT_LA_88;
	public final String FORMAT_OPAQUE;
	public final String FORMAT_RGB_332;
	public final String FORMAT_RGB_565;
	public final String FORMAT_RGB_888;
	public final String FORMAT_RGBA_4444;
	public final String FORMAT_RGBA_5551;
	public final String FORMAT_RGBA_8888;
	public final String FORMAT_RGBX_8888;
	public final String FORMAT_TRANSLUCENT;
	public final String FORMAT_TRANSPARENT;
	public final String UNKNOWN;	
	// Configuration constants
	public final String ORIENTATION_PORTRAIT;
	public final String ORIENTATION_LANDSCAPE;
	public final String ORIENTATION_SQUARE;
	public final String UNDEFINED;
	public final String SIZE_SMALL;
	public final String SIZE_NORMAL;
	public final String SIZE_LARGE;
	public final String SIZE_XLARGE;
	
	public Display(Context context) {
		super(context);
		mDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();		
        mDisplayMetrics = new DisplayMetrics();        
        if (mDisplay != null) {
        	mDisplay.getMetrics(mDisplayMetrics);
        }            
        
        // DisplayMetrics constants
        DENSITY_LOW = context.getString(R.string.display_density_low);
        DENSITY_MEDIUM = context.getString(R.string.display_density_medium);
        DENSITY_HIGH = context.getString(R.string.display_density_high);
        DENSITY_XHIGH = context.getString(R.string.display_density_xhigh);
        DENSITY_TV = context.getString(R.string.display_density_tv);    
    	// ImageFormat constants
    	FORMAT_JPEG = context.getString(R.string.display_format_jpeg);
    	FORMAT_NV21 = context.getString(R.string.display_format_nv21);
    	FORMAT_YUY2 = context.getString(R.string.display_format_yuy2);
    	FORMAT_NV16 = context.getString(R.string.display_format_nv16);	
    	// PixelFormat constants
    	FORMAT_YCBCR_420_SP = context.getString(R.string.display_format_ycbcr_420_sp);
    	FORMAT_YCBCR_422_I = context.getString(R.string.display_format_ycbcr_422_i);
    	FORMAT_YCBCR_422_SP = context.getString(R.string.display_format_ycbcr_422_sp);
    	FORMAT_L_8 = context.getString(R.string.display_format_l_8);
    	FORMAT_LA_88 = context.getString(R.string.display_format_la_88);
    	FORMAT_OPAQUE = context.getString(R.string.display_format_opaque);
    	FORMAT_RGB_332 = context.getString(R.string.display_format_rgb_332);
    	FORMAT_RGB_565 = context.getString(R.string.display_format_rgb_565);
    	FORMAT_RGB_888 = context.getString(R.string.display_format_rgb_888);
    	FORMAT_RGBA_4444 = context.getString(R.string.display_format_rgba_4444);
    	FORMAT_RGBA_5551 = context.getString(R.string.display_format_rgba_5551);
    	FORMAT_RGBA_8888 = context.getString(R.string.display_format_rgba_8888);
    	FORMAT_RGBX_8888 = context.getString(R.string.display_format_rgbx_8888);
    	FORMAT_TRANSLUCENT = context.getString(R.string.display_format_translucent);
    	FORMAT_TRANSPARENT = context.getString(R.string.display_format_transparent);
    	UNKNOWN = context.getString(R.string.display_unknown);	
    	// Configuration constants
    	ORIENTATION_PORTRAIT = context.getString(R.string.display_orientation_portrait);
    	ORIENTATION_LANDSCAPE = context.getString(R.string.display_orientation_landscape);
    	ORIENTATION_SQUARE = context.getString(R.string.display_orientation_square);
    	UNDEFINED = context.getString(R.string.display_undefined);
    	SIZE_SMALL = context.getString(R.string.display_size_small);
    	SIZE_NORMAL = context.getString(R.string.display_size_normal);
    	SIZE_LARGE = context.getString(R.string.display_size_large);
    	SIZE_XLARGE = context.getString(R.string.display_size_xlarge);
	}
	
	public android.view.Display getDisplay() {
		return mDisplay;
	}
	
	public DisplayMetrics getDisplayMetrics() {
		return mDisplayMetrics;
	}
	
	/** Get the logical density. Base density (1.0) is 160 dpi. */
	public float getLogicalDensity() {
		return mDisplayMetrics == null ? 0 : mDisplayMetrics.density;
	}
	
	public int getDensityDpi() {
		return mDisplayMetrics == null ? 0 : mDisplayMetrics.densityDpi;
	}
	
	public int getHeight() {
		return mDisplayMetrics == null ? 0 : mDisplayMetrics.heightPixels;
	}
	
	public float getScaledDensity() {
		return mDisplayMetrics == null ? 0 : mDisplayMetrics.scaledDensity;
	}
	
	public int getWidth() {
		return mDisplayMetrics == null ? 0 : mDisplayMetrics.widthPixels;
	}
	
	public float getXDpi() {
		return mDisplayMetrics == null ? 0 : mDisplayMetrics.xdpi;
	}
	
	public float getYDpi() {
		return mDisplayMetrics == null ? 0 : mDisplayMetrics.ydpi;
	}

	public String getDensityDpiString() {
		if (mDisplayMetrics == null) return null;
		int dpi = mDisplayMetrics.densityDpi;
		if (dpi == DisplayMetrics.DENSITY_LOW) return DENSITY_LOW;
		else if (dpi == DisplayMetrics.DENSITY_MEDIUM) return DENSITY_MEDIUM;
		else if (dpi == DisplayMetrics.DENSITY_HIGH) return DENSITY_HIGH;
		else if (API >= 9 && dpi == DisplayMetrics.DENSITY_XHIGH) return DENSITY_XHIGH;
		else if (API >= 13 && dpi == DisplayMetrics.DENSITY_TV) return DENSITY_TV;
		else return null;		
	}
	
	
	public float getWidthInches() {
		return mDisplayMetrics == null ? 0 
				: mDisplayMetrics.widthPixels / mDisplayMetrics.xdpi;
	}
	
	public float getHeightInches() {
		return mDisplayMetrics == null ? 0 
				: mDisplayMetrics.heightPixels / mDisplayMetrics.ydpi;
	}
	
	public float getDiagonal() {
		return mDisplayMetrics == null ? 0 
				: (float) (Math.sqrt(Math.pow(mDisplayMetrics.widthPixels, 2) 
				+ Math.pow(mDisplayMetrics.heightPixels, 2)));
	}
	
	public float getDiagonalInches() {
        return mDisplayMetrics == null ? 0 
        		: (float) (Math.sqrt(Math.pow(getWidthInches(), 2) 
        		+ Math.pow(getHeightInches(), 2)));
    }
	
	public float getRefreshRate() { 
		return mDisplay == null ? 0 : mDisplay.getRefreshRate(); 
	}

    @SuppressLint("NewApi")
	public int getRotation() { 
    	if (mDisplay == null) return 0; 
        if (API < 8) return mDisplay.getOrientation();
        else return mDisplay.getRotation();
    }
    
    public int getRotationDegrees() {
    	return getRotation() * 90;
    }
    
    public int getPixelFormat() {
    	return mDisplay == null ? 0 : mDisplay.getPixelFormat();
    }

    public String getPixelFormatString() {
    	if (mDisplay == null) return null;
    	
    	int format = mDisplay.getPixelFormat();
    	
        if (API < 8) {
            if (format == PixelFormat.JPEG) return FORMAT_JPEG;
            if (format == PixelFormat.YCbCr_420_SP) return FORMAT_YCBCR_420_SP;
            if (format == PixelFormat.YCbCr_422_I) return FORMAT_YCBCR_422_I;
            if (format == PixelFormat.YCbCr_422_SP) return FORMAT_YCBCR_422_SP;            
        }
        else {
            if (format == ImageFormat.JPEG) return FORMAT_JPEG;
            if (format == ImageFormat.NV21) return FORMAT_NV21;
            if (format == ImageFormat.YUY2) return FORMAT_YUY2; 
            if (format == ImageFormat.NV16) return FORMAT_NV16;                          
        }        
        if (format == PixelFormat.L_8) return FORMAT_L_8;
        if (format == PixelFormat.LA_88) return FORMAT_LA_88;
        if (format == PixelFormat.OPAQUE) return FORMAT_OPAQUE;
        if (format == PixelFormat.RGB_332) return FORMAT_RGB_332;
        if (format == PixelFormat.RGB_565) return FORMAT_RGB_565;
        if (format == PixelFormat.RGB_888) return FORMAT_RGB_888;
        if (format == PixelFormat.RGBA_4444) return FORMAT_RGBA_4444;
        if (format == PixelFormat.RGBA_5551) return FORMAT_RGBA_5551;
        if (format == PixelFormat.RGBA_8888) return FORMAT_RGBA_8888;
        if (format == PixelFormat.RGBX_8888) return FORMAT_RGBX_8888;
        if (format == PixelFormat.TRANSLUCENT) return FORMAT_TRANSLUCENT;
        if (format == PixelFormat.TRANSPARENT) return FORMAT_TRANSPARENT;
        if (format == PixelFormat.UNKNOWN) return UNKNOWN;
        return null;
    }    
    
    // TODO touch test
    public int getMaxSimultaneousTouch() {
    	String s = ShellHelper.getProp("ro.product.max_num_touch");
    	if (s == null || !s.matches("[0-9]{1,4}")) return 0;
    	int value = 0;
    	try { value = Integer.valueOf(s); }
    	catch (NumberFormatException ignored) {}    	
    	return value;
    }
    
    public boolean isTouchScreen(Context context) {
        Configuration config = context.getResources().getConfiguration(); 
        return (config.touchscreen == Configuration.TOUCHSCREEN_FINGER ||
                config.touchscreen == Configuration.TOUCHSCREEN_STYLUS);            
    }
    
    public float getFontScale(Context context) {
    	return context.getResources().getConfiguration().fontScale;
    }

    public String getOrientationString(Context context) {    	
    	switch (context.getResources().getConfiguration().orientation) {
    	case Configuration.ORIENTATION_PORTRAIT: return ORIENTATION_PORTRAIT;
    	case Configuration.ORIENTATION_LANDSCAPE: return ORIENTATION_LANDSCAPE;
    	case Configuration.ORIENTATION_SQUARE: return ORIENTATION_SQUARE;
    	case Configuration.ORIENTATION_UNDEFINED: return UNDEFINED;
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

    public String getScreenSizeString(Context context) {
    	int mask = context.getResources().getConfiguration().screenLayout;
    	if ((mask & Configuration.SCREENLAYOUT_SIZE_SMALL) > 0) return SIZE_SMALL; 
    	else if ((mask & Configuration.SCREENLAYOUT_SIZE_NORMAL) > 0) return SIZE_NORMAL;
    	else if ((mask & Configuration.SCREENLAYOUT_SIZE_LARGE) > 0) return SIZE_LARGE;
    	else if ((API >= 9) && ((mask & Configuration.SCREENLAYOUT_SIZE_XLARGE) > 0)) return SIZE_XLARGE;
    	else if ((mask & Configuration.SCREENLAYOUT_SIZE_UNDEFINED) > 0) return UNDEFINED;
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
	public LinkedHashMap<String, String> getContents() {
    	LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		contents.put("Density DPI String", getDensityDpiString());
		contents.put("Density DPI", String.valueOf(getDensityDpi()));
		contents.put("X DPI", String.valueOf(getXDpi()));
		contents.put("Y DPI", String.valueOf(getYDpi()));
		contents.put("Logical Density", String.valueOf(getLogicalDensity()));
		contents.put("Scaled Density", String.valueOf(getScaledDensity()));
		contents.put("Font Scale", String.valueOf(getFontScale(getContext())));
		contents.put("Width (pixel)", String.valueOf(getWidth()));
		contents.put("Height (pixel)", String.valueOf(getHeight()));
		contents.put("Diagonal (pixel)", String.valueOf(getDiagonal()));
		contents.put("Width (inch)", String.valueOf(getWidthInches()));
		contents.put("Height (inch)", String.valueOf(getHeightInches()));
		contents.put("Diagonal (inch)", String.valueOf(getDiagonalInches()));
		contents.put("Refresh Rate", String.valueOf(getRefreshRate()));
		contents.put("Rotation (degrees)", String.valueOf(getRotationDegrees()));
		contents.put("Pixel Format", getPixelFormatString());
		contents.put("Is Touch Screen", String.valueOf(isTouchScreen(getContext())));
		contents.put("Max Simultaneous Touch", String.valueOf(getMaxSimultaneousTouch()));
		contents.put("Screen Size String", getScreenSizeString(getContext()));
		contents.put("Is Screen Long", String.valueOf(isScreenLong(getContext())));
		contents.put("Orientation String", getOrientationString(getContext()));
		contents.put("Screen Height DP", String.valueOf(getScreenHeightDp(getContext())));
		contents.put("Screen Width DP", String.valueOf(getScreenWidthDp(getContext())));
		contents.put("Smallest Screen Width DP", String.valueOf(getSmallestScreenWidthDp(getContext())));
		
		return contents;
	}
}
