package com.jphilli85.deviceinfo.unit;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jphilli85.deviceinfo.ShellHelper;

public class Ram extends Unit {
	private LinkedHashMap<String, String> mMeminfo;
	
	public Ram() {
		mMeminfo = new LinkedHashMap<String, String>();
		if (!updateMeminfo()) {
			throw new RuntimeException("Error reading from '/proc/meminfo'."); 
		}			
	}
	
	/** Get the current meminfo from /proc */
	public boolean updateMeminfo() {
        List<String> meminfo = ShellHelper.getProc("meminfo");
        if (meminfo == null || meminfo.isEmpty()) return false;        
        String[] parts = null;
        mMeminfo.clear();
        for (String s : meminfo) {
        	parts = s.split(":");
        	if (parts.length != 2) continue;
        	mMeminfo.put(parts[0], parts[1]);
        }
        return !mMeminfo.isEmpty();
    }
	
	public Map<String, String> getMeminfo() {
		return mMeminfo;
	}
	
	public String getMeminfo(String key) {
        if (key == null || !mMeminfo.containsKey(key)) return null;
        return mMeminfo.get(key);        
    }
	
	public String getTotal() {
        return getMeminfo("MemTotal");     
    }
	
	public String getFree() {
        return getMeminfo("MemFree");     
    }
	
	
	@Override
	public LinkedHashMap<String, String> getContents() {
//		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();			
		return mMeminfo;
//		return contents;
	}
}
