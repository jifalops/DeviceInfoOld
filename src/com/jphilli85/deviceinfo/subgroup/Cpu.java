package com.jphilli85.deviceinfo.subgroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cpu extends Subgroup {
	private Map<String, String> mCpuinfo;
	//TODO multiple cpus, live info
	public Cpu() {
		mCpuinfo = new HashMap<String, String>();
		if (!updateCpuinfo()) {
			throw new RuntimeException("Error reading from '/proc/cpuinfo'."); 
		}			
	}
	
	/** Get the current cpuinfo from /proc */
	public boolean updateCpuinfo() {
        List<String> cpuinfo = getProc("cpuinfo");
        if (cpuinfo == null || cpuinfo.isEmpty()) return false;        
        String[] parts = null;
        mCpuinfo.clear();
        for (String s : cpuinfo) {
        	parts = s.split(":");
        	if (parts.length != 2) continue;
        	mCpuinfo.put(parts[0], parts[1]);
        }
        return !mCpuinfo.isEmpty();
    }
	
	public Map<String, String> getCpuinfo() {
		return mCpuinfo;
	}
	
	public String getCpuinfo(String key) {
        if (key == null || !mCpuinfo.containsKey(key)) return null;
        return mCpuinfo.get(key);        
    }
}
