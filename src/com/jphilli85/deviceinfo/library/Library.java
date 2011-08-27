/*
 * Copyright (C) 2011 Jacob Phillips
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jphilli85.deviceinfo.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public final class Library { 	
	/** Non-instantiable */
	private Library() {
		throw new AssertionError();
	}
	
	/**
	 * Convert a value in device independent pixels (dp) to raw pixels.
	 * @param context The context or view to scale to.
	 * @param dp The value to convert.
	 * @return The associated pixel value.
	 */
	public static int dpToPx(Context context, int dp) {		
	    float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (dp * scale + 0.5f);
	}
	
	
    /**
     * Upper case the first letter of a string.
     * @param str The string to operate on.
     * @return The new resulting string.
     * @throws NullPointerException if the string is <tt>null</tt>.
     */
    public static String ucFirst(String str) {
        if (str == null) throw new NullPointerException("The string cannot be null.");
        if (str.length() == 0) return str;
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
    
    /**
     * Upper case the first letter of each word of a string.
     * @param str The string to operate on.
     * @return The new resulting string.
     * @throws NullPointerException if the string is <tt>null</tt>.
     */
    public static String ucWords(String str) {
        if (str == null) throw new NullPointerException("The string cannot be null.");
        if (str.length() == 0) return str;        
        final StringBuilder result = new StringBuilder(str.length());        
        String[] words = str.split("\\s");       
        for(int i = 0; i < words.length; ++i) {       
            if(i>0) result.append(" ");
            result.append(ucFirst(words[i]));
        }
        return result.toString();
    }
    
    /**
     * Send a command to the shell.
     * @param command The command execute. 
     * @return A list of strings containing each line of the output.
     * @see java.lang.Runtime.getRuntime().exec()
     * @see java.io.BufferedReader.readLine()
     * @throws IOException if the requested command cannot be executed 
     *                      or if some other IOException occurs.
     */
    public static List<String> shellExec(String command) 
    		throws IOException, SecurityException {
        List<String> list = new ArrayList<String>();
        BufferedReader stdInput = null;
        try { 
            Process p = Runtime.getRuntime().exec(command);
            stdInput = new BufferedReader(new 
                InputStreamReader(p.getInputStream()));                                                
            String s;
            while ((s = stdInput.readLine()) != null) list.add(s);            
        } catch (IOException e) { 
        	stdInput.close();
        	throw e; 
    	}
        catch (SecurityException e) { 
        	stdInput.close();
        	throw e; 
    	}
        stdInput.close();
      
        return list;
    }
    
    
    public static String round(double number, int decimalPlaces) {
    	return round(number, decimalPlaces, false);
    }
    public static String round(double number, int decimalPlaces, boolean groupDigits) {
    	NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(decimalPlaces); 
        numFormat.setGroupingUsed(groupDigits);
        return numFormat.format(number);
    }
    
    /**
     * Scale bits or bytes. Accepted values are as follows:<br>
     * b=bits, B=bytes, k=kibi, K=kilo, m=mibi, M=mega, g=gibi, G=giga<br>
     * Lower case letters (except 'b') mean the multiplier is based on 1024 instead of 1000.<br>
     * For zero or one length strings, the lowest possible multiplier will be used.
     * For example, "B" means raw number of bytes and "k" means kibi bits.
     * @param value value to scale.
     * @param oldScale current scale.
     * @param newScale desired scale.
     * @return A value converted to the requested scale
     */
    public static double scaleData(double value, String oldScale, String newScale) {        
        final double FAILED = -1;
        if (oldScale.length() > 2 || newScale.length() > 2) return FAILED;
        if (oldScale.equals(newScale)) return value;
        
        int oldMultiplier = 0;
        int oldBase = 0;
        int newMultiplier = 0;
        int newBase = 0;    
        //double valueInBits = 0;
        
        // Set values for what was given
        if (oldScale.length() == 2) {
            if (oldScale.startsWith("k")) oldMultiplier = 1024;
            else if (oldScale.startsWith("K")) oldMultiplier = 1000;
            else if (oldScale.startsWith("m")) oldMultiplier = 1024 * 1024;
            else if (oldScale.startsWith("M")) oldMultiplier = 1000 * 1000;
            else if (oldScale.startsWith("g")) oldMultiplier = 1024 * 1024 * 1024;
            else if (oldScale.startsWith("G")) oldMultiplier = 1000 * 1000 * 1000;
            else return FAILED;
            
            if (oldScale.endsWith("b")) oldBase = 1;
            else if (oldScale.endsWith("B")) oldBase = 8;
            else return FAILED;
        }
        else if (oldScale.length() == 1) {
            if (oldScale.startsWith("k")) { oldMultiplier = 1024; oldBase = 1; }
            else if (oldScale.startsWith("K")) { oldMultiplier = 1000; oldBase = 1; }
            else if (oldScale.startsWith("m")) { oldMultiplier = 1024 * 1024; oldBase = 1; }
            else if (oldScale.startsWith("M")) { oldMultiplier = 1000 * 1000; oldBase = 1; }
            else if (oldScale.startsWith("g")) { oldMultiplier = 1024 * 1024 * 1024; oldBase = 1; }
            else if (oldScale.startsWith("G")) { oldMultiplier = 1000 * 1000 * 1000; oldBase = 1; }
            else if (oldScale.startsWith("b")) { oldMultiplier = 1; oldBase = 1; }
            else if (oldScale.startsWith("B")) { oldMultiplier = 1; oldBase = 8; }
            else return FAILED;
        }
        else { oldMultiplier = 1; oldBase = 1; }
        
        if (newScale.length() == 2) {
            if (newScale.startsWith("k")) newMultiplier = 1024;
            else if (newScale.startsWith("K")) newMultiplier = 1000;
            else if (newScale.startsWith("m")) newMultiplier = 1024 * 1024;
            else if (newScale.startsWith("M")) newMultiplier = 1000 * 1000;
            else if (newScale.startsWith("g")) newMultiplier = 1024 * 1024 * 1024;
            else if (newScale.startsWith("G")) newMultiplier = 1000 * 1000 * 1000;
            else return FAILED;
            
            if (newScale.endsWith("b")) newBase = 1;
            else if (newScale.endsWith("B")) newBase = 8;
            else return FAILED;
        }
        else if (newScale.length() == 1) {
            if (newScale.startsWith("k")) { newMultiplier = 1024; newBase = 1; }
            else if (newScale.startsWith("K")) { newMultiplier = 1000; newBase = 1; }
            else if (newScale.startsWith("m")) { newMultiplier = 1024 * 1024; newBase = 1; }
            else if (newScale.startsWith("M")) { newMultiplier = 1000 * 1000; newBase = 1; }
            else if (newScale.startsWith("g")) { newMultiplier = 1024 * 1024 * 1024; newBase = 1; }
            else if (newScale.startsWith("G")) { newMultiplier = 1000 * 1000 * 1000; newBase = 1; }
            else if (newScale.startsWith("b")) { newMultiplier = 1; newBase = 1; }
            else if (newScale.startsWith("B")) { newMultiplier = 1; newBase = 8; }
            else return FAILED;
        }
        else { newMultiplier = 1; newBase = 1; }       
        
        // Compute bits and then divide by the given scaling factors
        return value * oldBase * oldMultiplier / newBase / newMultiplier;
    }   
}