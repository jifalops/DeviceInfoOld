package com.jphilli85.deviceinfo.subgroup;

import java.io.IOException;
import java.util.List;

import com.jphilli85.deviceinfo.library.Library;

public abstract class Subgroup {
	protected List<String> getProc(String proc) {
		if (proc == null || proc.isEmpty()) return null;
		List<String> list = null;                       
        try { list = Library.shellExec("cat /proc/" + proc); }
        catch (IOException e) {}
        catch (SecurityException e) {}
        return list;
    }  
}
