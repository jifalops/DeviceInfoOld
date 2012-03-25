package com.jphilli85.deviceinfo;

import java.text.NumberFormat;

import android.content.Context;

public class Numbers {
	
	/* Non instantiable, even from within. */
	private Numbers() { throw new AssertionError(); }
	
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
     * Scale bits or bytes. The scale can be described as [KkMmGgTt][Bb].
     * Upper case letters (except 'B') use a base 10 multiplier.
     * Lower case letters (except 'b') use a base 2 multiplier.
     * 'B' means the value is in bytes and 'b' means the value is in bits.<br>
     * If the multiplier is omitted, no multiplier is applied.
     * If the base (bit or byte) is omitted, byte will be used.
     * For example, "b" means raw number of bits and "k" means kibi bytes.     
     * @param value value to scale.
     * @param oldScale current scale.
     * @param newScale desired scale.
     * @return A value converted to the requested scale, or -1 if an error was encountered.
     */
    public static double scaleBytes(double value, String oldScale, String newScale) {        
        final double ERROR = -1;
        // Default to using bytes (8) or bits (1)
        final int DEFAULT_BASE = 8;
        
        if (oldScale == null || oldScale.length() > 2 
        		|| newScale == null || newScale.length() > 2) return ERROR;
        if (oldScale.equals(newScale)) return value;
                
        int oldBase = 0;
        long oldMultiplier = 0;        
        int newBase = 0;    
        long newMultiplier = 0;
        
        // Set values for what was given
        if (oldScale.length() == 2) {
            if (oldScale.startsWith("k")) oldMultiplier = 1024;
            else if (oldScale.startsWith("K")) oldMultiplier = 1000;
            else if (oldScale.startsWith("m")) oldMultiplier = 1024 * 1024;
            else if (oldScale.startsWith("M")) oldMultiplier = 1000 * 1000;
            else if (oldScale.startsWith("g")) oldMultiplier = 1024 * 1024 * 1024;
            else if (oldScale.startsWith("G")) oldMultiplier = 1000 * 1000 * 1000;
            else if (oldScale.startsWith("t")) oldMultiplier = 1024 * 1024 * 1024 * 1024;
            else if (oldScale.startsWith("T")) oldMultiplier = 1000 * 1000 * 1000 * 1000;
            else return ERROR;
            
            if (oldScale.endsWith("b")) oldBase = 1;
            else if (oldScale.endsWith("B")) oldBase = 8;
            else return ERROR;
        }
        else if (oldScale.length() == 1) {
            if (oldScale.startsWith("k")) { oldMultiplier = 1024; oldBase = 1; }
            else if (oldScale.startsWith("K")) { oldMultiplier = 1000; oldBase = 1; }
            else if (oldScale.startsWith("m")) { oldMultiplier = 1024 * 1024; oldBase = 1; }
            else if (oldScale.startsWith("M")) { oldMultiplier = 1000 * 1000; oldBase = 1; }
            else if (oldScale.startsWith("g")) { oldMultiplier = 1024 * 1024 * 1024; oldBase = 1; }
            else if (oldScale.startsWith("G")) { oldMultiplier = 1000 * 1000 * 1000; oldBase = 1; }
            else if (oldScale.startsWith("t")) { oldMultiplier = 1024 * 1024 * 1024 * 1024; oldBase = 1; }
            else if (oldScale.startsWith("T")) { oldMultiplier = 1000 * 1000 * 1000 * 1000; oldBase = 1; }
            else if (oldScale.startsWith("b")) { oldMultiplier = 1; oldBase = 1; }
            else if (oldScale.startsWith("B")) { oldMultiplier = 1; oldBase = 8; }
            else return ERROR;
        }
        else { oldMultiplier = 1; oldBase = DEFAULT_BASE; }
        
        if (newScale.length() == 2) {
            if (newScale.startsWith("k")) newMultiplier = 1024;
            else if (newScale.startsWith("K")) newMultiplier = 1000;
            else if (newScale.startsWith("m")) newMultiplier = 1024 * 1024;
            else if (newScale.startsWith("M")) newMultiplier = 1000 * 1000;
            else if (newScale.startsWith("g")) newMultiplier = 1024 * 1024 * 1024;
            else if (newScale.startsWith("G")) newMultiplier = 1000 * 1000 * 1000;
            else if (newScale.startsWith("t")) newMultiplier = 1024 * 1024 * 1024 * 1024;
            else if (newScale.startsWith("T")) newMultiplier = 1000 * 1000 * 1000 * 1000;
            else return ERROR;
            
            if (newScale.endsWith("b")) newBase = 1;
            else if (newScale.endsWith("B")) newBase = 8;
            else return ERROR;
        }
        else if (newScale.length() == 1) {
            if (newScale.startsWith("k")) { newMultiplier = 1024; newBase = 1; }
            else if (newScale.startsWith("K")) { newMultiplier = 1000; newBase = 1; }
            else if (newScale.startsWith("m")) { newMultiplier = 1024 * 1024; newBase = 1; }
            else if (newScale.startsWith("M")) { newMultiplier = 1000 * 1000; newBase = 1; }
            else if (newScale.startsWith("g")) { newMultiplier = 1024 * 1024 * 1024; newBase = 1; }
            else if (newScale.startsWith("G")) { newMultiplier = 1000 * 1000 * 1000; newBase = 1; }
            else if (newScale.startsWith("t")) { newMultiplier = 1024 * 1024 * 1024 * 1024; newBase = 1; }
            else if (newScale.startsWith("T")) { newMultiplier = 1000 * 1000 * 1000 * 1000; newBase = 1; }
            else if (newScale.startsWith("b")) { newMultiplier = 1; newBase = 1; }
            else if (newScale.startsWith("B")) { newMultiplier = 1; newBase = 8; }
            else return ERROR;
        }
        else { newMultiplier = 1; newBase = DEFAULT_BASE; }       
        
        // Compute bits and then divide by the given scaling factors
        return value * oldBase * oldMultiplier / newBase / newMultiplier;
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
	
	public static int pxToDp(Context context, int px) {		
	    float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (px / scale + 0.5f);
	}
}
