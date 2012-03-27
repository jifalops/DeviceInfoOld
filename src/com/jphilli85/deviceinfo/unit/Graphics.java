package com.jphilli85.deviceinfo.unit;

import java.util.HashMap;
import java.util.Map;

import com.jphilli85.deviceinfo.ShellHelper;

public class Graphics extends Unit {
	
	public String getOpenglVersion() {  return ShellHelper.getProp("ro.opengles.version"); }
	
	@Override
	public Map<String, String> getContents() {
		Map<String, String> contents = new HashMap<String, String>();
		
		
		
		return contents;
	}
}
