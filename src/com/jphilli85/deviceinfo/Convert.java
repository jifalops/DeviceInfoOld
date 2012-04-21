package com.jphilli85.deviceinfo;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

public class Convert {
	private Convert() { throw new AssertionError(); }
	
	public static class ByteArray {
		private ByteArray() { throw new AssertionError(); }
		
		/** Converts to values to hex */
		public static String[] toStringArray(byte[] values) {
			return toStringArray(values, true, false);
		}
		
		/** Converts to values to decimal */
		public static String[] toStringArray(byte[] values, boolean signed) {
			return toStringArray(values, false, signed);
		}
		
		/** If useHex is true, signed will be ignored. */
		private static String[] toStringArray(byte[] values, boolean useHex, boolean signed) {
			if (values == null || values.length == 0) return null;
			String[] result = new String[values.length];
			int mask = 0xFF;
			if (useHex) {
				for (int i = 0; i < values.length; ++i) {	
					result[i] = Integer.toHexString(values[i] & mask);	
				}
			}
			else if (signed) {
				for (int i = 0; i < values.length; ++i) {			
					result[i] = Byte.toString(values[i]);	
				}
			}
			else {
				for (int i = 0; i < values.length; ++i) {			
					result[i] = Integer.toString(values[i] & mask);	
				}
			}
			return result;
		}
		
		/** Converts a byte array to an integer using up to the first four elements in the array. */
		public static int toInt(byte[] values) {
			if (values == null) return 0;
			int result = 0;
			if (values.length >= 1) result |= (values[0] << 24);
			if (values.length >= 2) result |= ((values[1] & 0xFF) << 16);
			if (values.length >= 3) result |= ((values[2] & 0xFF) << 8);
			if (values.length >= 4) result |= (values[3] & 0xFF);
			return result;
		}
		
		/** 
		 * Convert a byte array to an array of ints in an unsigned fashion 
		 * (0 to 255 instead of -128 to 127).
		 */
		public static int[] toIntArray(byte[] values) {
			return toIntArray(values, false);
		}
		
		/** 
		 * Convert a byte array to an array of ints in a signed or unsigned fashion 
		 * (-128 to 127 or 0 to 255, respectively).
		 */
		public static int[] toIntArray(byte[] values, boolean signed) {
			if (values == null || values.length == 0) return null;
			int[] result = new int[values.length];
			if (signed) {			
				for (int i = 0; i < values.length; ++i) {
					result[i] = values[i];
				}
			}
			else {
				for (int i = 0; i < values.length; ++i) {
					result[i] = values[i] & 0xFF;
				}
			}
			return result;
		}
		
		public static String toHexString(byte[] values) {
			return toHexString(values, null, 1);
		}
		
		public static String toHexString(byte[] values, String separator) {
			return toHexString(values, separator, 1);
		}
		
		/** If there is no separator, groupSize is ignored */
		public static String toHexString(byte[] values, String separator, int groupSize) {			
			if (values == null || values.length == 0) return null;			
			StringBuilder sb = new StringBuilder();
			if (separator == null || separator.length() == 0) {
				for (byte b : values) {
					sb.append(Integer.toHexString(b & 0xFF));
				}
			}
			else {
				int groupCount = 0;
				for (byte b : values) {
					if (groupCount == groupSize) {
						sb.append(separator);
						groupCount = 0;
					}
					sb.append(Integer.toHexString(b & 0xFF));
					++groupCount;
				}
			}
			return sb.toString();
		}
		
		/** Defaults to unsigned bytes */
		public static String toDecString(byte[] values, String separator) {
			return toDecString(values, separator, false, 1);
		}
		
		public static String toDecString(byte[] values, String separator, boolean signed) {
			return toDecString(values, separator, signed, 1);
		}
		
		/** If there is no separator, groupSize is ignored */
		public static String toDecString(byte[] values, String separator, boolean signed, int groupSize) {			
			if (values == null || values.length == 0) return null;			
			StringBuilder sb = new StringBuilder();
			if (signed) {
				if (separator == null || separator.length() == 0) {
					for (byte b : values) {
						sb.append(Integer.toString(b));
					}
				}
				else {
					int groupCount = 0;
					for (byte b : values) {
						if (groupCount == groupSize) {
							sb.append(separator);
							groupCount = 0;
						}
						sb.append(Integer.toString(b));
						++groupCount;
					}
				}
			}
			else {
				if (separator == null || separator.length() == 0) {
					for (byte b : values) {
						sb.append(Integer.toString(b & 0xFF));
					}
				}
				else {
					int groupCount = 0;
					for (byte b : values) {
						if (groupCount == groupSize) {
							sb.append(separator);
							groupCount = 0;
						}
						sb.append(Integer.toString(b & 0xFF));
						++groupCount;
					}
				}
			}
			return sb.toString();
		}
	}
	
	public static class BooleanArray {
		private BooleanArray() { throw new AssertionError(); }
		
		/** Converts to "true" and "false" */
		public static String[] toStringArray(boolean[] values) {
			return toStringArray(values, "true", "false");
		}
		
		public static String[] toStringArray(boolean[] values, String trueString, String falseString) {
			if (values == null || values.length == 0) return null;
			String[] result = new String[values.length];		
			for (int i = 0; i < values.length; ++i) {	
				result[i] = values[i] ? trueString : falseString;	
			}
			return result;
		}
	}
	
	public static class IntArray {
		private IntArray() { throw new AssertionError(); }
	
		/** Converts to values to hex */
		public static String[] toStringArray(int[] values) {
			return toStringArray(values, true, false);
		}
		
		/** Converts to values to decimal */
		public static String[] toStringArray(int[] values, boolean signed) {
			return toStringArray(values, false, signed);
		}
		
		/** If useHex is true, signed will be ignored. */
		private static String[] toStringArray(int[] values, boolean useHex, boolean signed) {
			if (values == null || values.length == 0) return null;
			String[] result = new String[values.length];
			long mask = 0xFFFFFFFFL;
			if (useHex) {
				for (int i = 0; i < values.length; ++i) {	
					result[i] = Long.toHexString(values[i] & mask);	
				}
			}
			else if (signed) {
				for (int i = 0; i < values.length; ++i) {			
					result[i] = Integer.toString(values[i]);	
				}
			}
			else {
				for (int i = 0; i < values.length; ++i) {			
					result[i] = Long.toString(values[i] & mask);	
				}
			}
			return result;
		}
		
		/** 
		 * If expand is true, each element will be expanded to four bytes 
		 * and the resulting array will be four times as long as the one given
		 */
		public static byte[] toByteArray(int[] values, boolean expand) {
			if (values == null || values.length == 0) return null;
			byte[] result;
			if (expand) {				
				result = new byte[values.length * 4];
				byte[] bytes = new byte[4];
				for (int i = 0; i < values.length; ++i) {
					bytes = Int.toByteArray(values[i]);
					result[i * 4] = bytes[0];
					result[i * 4 + 1] = bytes[1];
					result[i * 4 + 2] = bytes[2];
					result[i * 4 + 3] = bytes[3];
				}
			}
			else {
				result = new byte[values.length];
				for (int i = 0; i < values.length; ++i) {	
					result[i] = (byte) values[i];			
				}
			}
			return result;
		}
		
		public static String toHexString(int[] values) {
			if (values == null || values.length == 0) return null;
			return ByteArray.toHexString(toByteArray(values, true));
		}
		
		public static String toHexString(int[] values, String separator) {
			if (values == null || values.length == 0) return null;
			return ByteArray.toHexString(toByteArray(values, true), separator, 4);
		}
		
		public static String toHexString(int[] values, String separator, int groupSize) {
			if (values == null || values.length == 0) return null;
			return ByteArray.toHexString(toByteArray(values, true), separator, groupSize * 4);
		}
		
		/** Defaults to signed integers */
		public static String toDecString(int[] values, String separator) {
			return toDecString(values, separator, true, 1);
		}
		
		public static String toDecString(int[] values, String separator, boolean signed) {
			return toDecString(values, separator, signed, 1);
		}
		
		
		/** If there is no separator, groupSize is ignored */
		public static String toDecString(int[] values, String separator, boolean signed, int groupSize) {			
			if (values == null || values.length == 0) return null;			
			StringBuilder sb = new StringBuilder();
			if (signed) {
				if (separator == null || separator.length() == 0) {
					for (int v : values) {
						sb.append(Integer.toString(v));
					}
				}
				else {
					int groupCount = 0;
					for (int v : values) {
						if (groupCount == groupSize) {
							sb.append(separator);
							groupCount = 0;
						}
						sb.append(Integer.toString(v));
						++groupCount;
					}
				}
			}
			else {
				long mask = 0xFFFFFFFFL;
				if (separator == null || separator.length() == 0) {
					for (int v : values) {
						sb.append(Long.toString(v & mask));
					}
				}
				else {
					int groupCount = 0;
					for (int v : values) {
						if (groupCount == groupSize) {
							sb.append(separator);
							groupCount = 0;
						}
						sb.append(Long.toString(v & mask));
						++groupCount;
					}
				}
			}
			return sb.toString();
		}
	}
	
		
	public static class Int {
		private Int() { throw new AssertionError(); }
		
		public static byte[] toByteArray(int value) {
			return new byte[] { 
				(byte) (value >> 24),
				(byte) (value >> 16),
				(byte) (value >> 8),
				(byte) value
			};
		}
		
		/** Similar to Int.toByteArray(int), but returns an array of ints instead. */
		public static int[] toIntArray(int ip) {
			return new int[] {
				(ip >> 24) & 0xFF,
				(ip >> 16) & 0xFF,
				(ip >> 8) & 0xFF,
				ip & 0xFF
			};
		}
	}
	
	public static class StringArray {
		private StringArray() { throw new AssertionError(); }
		
		/** Converts an array of hex values to an array of bytes. (@see Strings.toByteArray(String)) */
		public static byte[] toByteArray(String[] values) {
			return Strings.toByteArray(TextUtils.join("", values));
		}
		
		/** 
		 * Converts an array of decimal or hex strings to an array of bytes.
		 * If hex is used, this turns into a wrapper for {@code Strings.toByteArray(String hexString)}.
		 */
		public static byte[] toByteArray(String[] values, boolean isHex) {
			if (values == null || values.length == 0) return null;
			if (isHex) return Strings.toByteArray(TextUtils.join("", values));
			
			byte[] result = new byte[values.length];
			for (int i = 0; i < values.length; ++i) {	
				try { result[i] = Byte.valueOf(values[i]); }
				catch (NumberFormatException e) {
					result[i] = 0;
				}
			}
			return result;
		}
		
		/** Converts an array of hex values to an array of ints. (@see Strings.toIntArray(String)) */
		public static int[] toIntArray(String[] values) {
			return Strings.toIntArray(TextUtils.join("", values));
		}
		
		/** 
		 * Converts an array of decimal or hex strings to an array of ints.
		 * If hex is used, this turns into a wrapper for {@code Strings.toIntArray(String hexString)}.
		 */
		public static int[] toIntArray(String[] values, boolean isHex) {
			if (values == null || values.length == 0) return null;
			if (isHex) return Strings.toIntArray(TextUtils.join("", values));			
			
			int[] result = new int[values.length];
			for (int i = 0; i < values.length; ++i) {	
				try { result[i] = Integer.valueOf(values[i]); }
				catch (NumberFormatException e) {
					result[i] = 0;
				}
			}
			return result;
		}
	}
	
	public static class Strings {
		private Strings() { throw new AssertionError(); }
	
		/** Removes all non-hexadecimal characters and then converts to a byte array. */
		public static byte[] toByteArray(String hexString) {
			if (hexString == null || hexString.length() == 0) return null;
			String hex = hexString.replaceAll("[^0-9a-fA-F]", null);		
			if (hex.length() % 2 == 1) hex = "0" + hex;
			int len = hex.length();		
			byte[] result = new byte[len / 2];
			for (int i = 0; i < len; i += 2) {
				result[i/2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	                    + Character.digit(hex.charAt(i+1), 16));
			}
			return result;
		}
		
		/** Removes all non-hexadecimal characters and then converts to an int array. */
		public static int[] toIntArray(String hexString) {
			if (hexString == null || hexString.length() == 0) return null;
			String hex = hexString.replaceAll("[^0-9a-fA-F]", null);		
			if (hex.length() % 2 == 1) hex = "0" + hex;
			int len = hex.length();		
			int[] result = new int[len / 8];
			for (int i = 0; i < len; i += 8) {
				result[i/8] = (Character.digit(hex.charAt(i), 16) << 28)
	                    + Character.digit(hex.charAt(i+1), 16) << 24
	                    + Character.digit(hex.charAt(i+2), 16) << 20
	                    + Character.digit(hex.charAt(i+3), 16) << 16
	                    + Character.digit(hex.charAt(i+4), 16) << 12
	                    + Character.digit(hex.charAt(i+5), 16) << 8
	                    + Character.digit(hex.charAt(i+6), 16) << 4
	                    + Character.digit(hex.charAt(i+7), 16);
			}
			return result;
		}
	}
	
	public static void reverse(byte[] array) {
	      if (array == null) return;
	      int i = 0;
	      int j = array.length - 1;
	      byte tmp;
	      while (j > i) {
	          tmp = array[j];
	          array[j] = array[i];
	          array[i] = tmp;
	          j--;
	          i++;
	      }
	  }
	
	/** This class assumes decimal numbers with a dot (".") delimiter. */
	public static class Ip4 {
		private Ip4() { throw new AssertionError(); }
		
		public static String fromInt(int ip) {
			byte[] bytes = Int.toByteArray(ip);
			reverse(bytes);
			return ByteArray.toDecString(bytes, ".");
		}
		
		
//		public static String getString(int ip) {
//			return getString(toIntArray(ip));
//		}

//		public static int toInt(int[] ip) {
//			return ByteArray.toInt(IntArray.toByteArray(ip, false));
//		}
//
//		public static int toInt(String ip) {					
//			return ByteArray.toInt(toByteArray(ip));
//		}
//		
//		public static byte[] toByteArray(String ip) {
//			if (ip == null || ip.length() == 0) return null;
//			String[] parts = ip.split(".");
//			if (parts.length != 4) return null;
//			return StringArray.toByteArray(parts, false);
//		}
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
