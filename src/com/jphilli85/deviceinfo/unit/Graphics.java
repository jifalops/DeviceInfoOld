package com.jphilli85.deviceinfo.unit;

import java.util.LinkedHashMap;

import com.jphilli85.deviceinfo.ShellHelper;

public class Graphics extends Unit {
	
	public String getOpenglVersion() {  return ShellHelper.getProp("ro.opengles.version"); }
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		
		
		return contents;
	}
}
