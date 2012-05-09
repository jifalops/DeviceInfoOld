package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;

import com.jphilli85.deviceinfo.ShellHelper;

public class Properties extends Element {

	@Override
	public LinkedHashMap<String, String> getContents() {
//		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		return ShellHelper.getProp();
//		return contents;
	}
 
}
