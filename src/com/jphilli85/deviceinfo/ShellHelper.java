package com.jphilli85.deviceinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class ShellHelper {
	private static final String LOG_TAG = ShellHelper.class.getSimpleName();
	
	/** Non instantiable, even from within. */
	private ShellHelper() { throw new AssertionError(); }
	
	/**
     * Send a command to the shell.
     * @param command The command execute. 
     * @return A list of strings containing each line of the output.
     * @see java.lang.Runtime.getRuntime().exec()
     * @see java.io.BufferedReader.readLine()
     * @throws IOException if the requested command cannot be executed 
     *                      or if some other IOException occurs.
     */
    public static List<String> exec(String command) 
    		throws IOException, SecurityException {
        return exec(command, true);
    }
    
    /**   
     * @param trimOutput
     * 		trim whitespace from the beginning and end of
     * 	    each line of the command output. Defaults to true.
     */
    public static List<String> exec(String command, boolean trimOutput) 
    		throws IOException, SecurityException {
        List<String> list = new ArrayList<String>();
        BufferedReader stdInput = null;
        try { 
            Process p = Runtime.getRuntime().exec(command);
            stdInput = new BufferedReader(new 
                InputStreamReader(p.getInputStream()));                                                
            String s;
            while ((s = stdInput.readLine()) != null) {
            	if (trimOutput)	{
            		s = s.trim();
            		if (!s.isEmpty()) list.add(s);
            	}
            	else list.add(s);
            }
    	}
        catch (IOException e) {
        	Log.e(LOG_TAG, "IOException occurred while executing '" + command + "'.");
        	throw e;
        }
        catch (SecurityException e) {
        	Log.e(LOG_TAG, "SecurityException occurred while executing '" + command + "'.");
        	throw e;
        }
        finally {
        	stdInput.close();
        }
        return list;
    }
    
    public static List<String> cat(String filename) {    	
    	File f = new File(filename); 
    	if (!f.exists() || f.isDirectory()) return null;
		List<String> list = null;                       
        try { list = exec("cat " + f.getAbsolutePath()); }
        catch (IOException ignored) {}
        catch (SecurityException ignored) {}
        return list;
    }
    
    
    public static List<String> getProc(String proc) {
    	return cat("/proc/" + proc);
    }  
    
    
    public static Map<String, String> getProp() {  
    	List<String> list = null; 
        try { list = exec("getprop"); }
        catch (IOException ignored) {}
        catch (SecurityException ignored) {}
        if (list == null || list.isEmpty()) return null;
        Map<String, String> props = new HashMap<String, String>();
        String[] parts = null;
        for (String s : list) {
        	// Each property has the format [name]: [value]
        	// Remove first and last bracket.        	
        	s = s.trim().substring(1, s.length() - 1);
        	parts = s.split("]: [");
        	if (parts == null || parts.length != 2) {
        		Log.d(LOG_TAG, "getprop property does not have exactly 2 parts.");
        		continue;
        	}
        	props.put(parts[0], parts[1]);
        }
        return props;
    }
    
    public static String getProp(String prop) {
        if (prop == null || prop.isEmpty()) return null;        
        String s = null;         
        try { s = exec("getprop " + prop).get(0).trim(); }
        catch (IOException ignored) {}
        catch (SecurityException ignored) {}
        return s;           
    }

    
}
