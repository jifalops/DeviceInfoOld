package com.jphilli85.deviceinfo.element.view;

import android.content.Context;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Display;
import com.jphilli85.deviceinfo.element.Element;


public class DisplayView extends ElementView {
	private final Display mDisplay;
	
	public DisplayView() {
		this(DeviceInfo.getContext());
	}
	protected DisplayView(Context context) {
		super(context);
		mDisplay = new Display(context);
		
		TableSection table = new TableSection();
		
		table.add("Density", mDisplay.getDensityDpiString() 
				+ " (" + String.valueOf(mDisplay.getDensityDpi()) + " DPI)");		
		table.add("Exact X DPI", String.valueOf(mDisplay.getXDpi()));
		table.add("Exact Y DPI", String.valueOf(mDisplay.getYDpi()));
		table.add("Logical Density", String.valueOf(mDisplay.getLogicalDensity()));
		table.add("Scaled Density", String.valueOf(mDisplay.getScaledDensity()));
		table.add("Font Scale", String.valueOf(mDisplay.getFontScale(context)));
		table.add("Width (pixel)", String.valueOf(mDisplay.getWidth()));
		table.add("Height (pixel)", String.valueOf(mDisplay.getHeight()));
		table.add("Diagonal (pixel)", String.valueOf(mDisplay.getDiagonal()));
		table.add("Width (inch)", String.valueOf(mDisplay.getWidthInches()));
		table.add("Height (inch)", String.valueOf(mDisplay.getHeightInches()));
		table.add("Diagonal (inch)", String.valueOf(mDisplay.getDiagonalInches()));
		table.add("Refresh Rate", String.valueOf(mDisplay.getRefreshRate()));
		table.add("Rotation (degrees)", String.valueOf(mDisplay.getRotationDegrees()));
		table.add("Pixel Format", mDisplay.getPixelFormatString());
		table.add("Is Touch Screen", String.valueOf(mDisplay.isTouchScreen(context)));
		table.add("Max Simultaneous Touch", String.valueOf(mDisplay.getMaxSimultaneousTouch()));
		table.add("Screen Size", mDisplay.getScreenSizeString(context));
		table.add("Is Screen Long", String.valueOf(mDisplay.isScreenLong(context)));
		table.add("Orientation", mDisplay.getOrientationString(context));
		table.add("Screen Height DP", String.valueOf(mDisplay.getScreenHeightDp(context)));
		table.add("Screen Width DP", String.valueOf(mDisplay.getScreenWidthDp(context)));
		table.add("Smallest Screen Width DP", String.valueOf(mDisplay.getSmallestScreenWidthDp(context)));
		
		add(table);
	}

	@Override
	public Element getElement() {
		return mDisplay;
	}
}
