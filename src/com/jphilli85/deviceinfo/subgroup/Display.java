package com.jphilli85.deviceinfo.subgroup;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Display extends Subgroup {
	private android.view.Display mDisplay;
    private DisplayMetrics mDisplayMetrics;
	
	public Display(Context context) {
		mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        mDisplay.getMetrics(mDisplayMetrics);     
	}
}
