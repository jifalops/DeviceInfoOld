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


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A convenience class that gathers information about the device that the application 
 * is running on. <br>
 * This class also checks that the requested information exists at the current API level
 * running on the device.<br>
 */
public class DeviceInfo {
    // The API level of the device.
    private final int API;     
    private static final String UNKNOWN = "unknown";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    
    private final TelephonyManager mTelephonyManager;
    private final ServiceState mServiceState;
    private final SensorManager mSensorManager;    
    private final Display mDisplay;
    private DisplayMetrics mDisplayMetrics;
    private ContentResolver mContentResolver;    
    
    public DeviceInfo(Context context) {  
    	API = Build.VERSION.SDK_INT;
        mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        mDisplay.getMetrics(mDisplayMetrics);       
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);          
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mServiceState = new ServiceState();    
        mContentResolver = context.getContentResolver();
    }

    private String gets(String s) { 
    	return (s == null || s.length() == 0) ? UNKNOWN : s; 
	}
  
    // Android's ID for tracking devices. Does not work reliably.
    public String getAndroidId() { return gets(Secure.getString(mContentResolver, Secure.ANDROID_ID)); }
    
    
    // Disk Partitions
    public String getSystemSize() { 
        StatFs stat = new StatFs("/system");
        return String.valueOf((long)stat.getBlockSize() * (long)stat.getBlockCount()); 
    } 
    public String getSystemSize(String scale, int decimalPlaces) { 
    	StatFs stat = new StatFs("/system");
    	return Library.round(
    			Library.scaleData(
    				(double)stat.getBlockSize() * (double)stat.getBlockCount(), 
    				"B", scale), decimalPlaces) + " " + scale;
    }
    public String getDataSize() { 
        StatFs stat = new StatFs("/data");
        return String.valueOf((long)stat.getBlockSize() * (long)stat.getBlockCount()); 
    } 
    public String getDataSize(String scale, int decimalPlaces) { 
        StatFs stat = new StatFs("/data");
        return Library.round(
                Library.scaleData(
                    (double)stat.getBlockSize() * (double)stat.getBlockCount(), 
                    "B", scale), decimalPlaces) + " " + scale;
    }
    
    
    
    //
    // Telephony properties
    //
    
    // Telephony Manager 
    public String getDeviceId() {           return gets(mTelephonyManager.getDeviceId()); }
    public String getSubscriberId() {       return gets(mTelephonyManager.getSubscriberId()); }
    public String getSimSerial() {          return gets(mTelephonyManager.getSimSerialNumber()); }
    public String getSoftwareVersion() {    return gets(mTelephonyManager.getDeviceSoftwareVersion()); }
    public String getPhoneNumber() {        return gets(mTelephonyManager.getLine1Number()); }
    public String getCountryCode() {        return gets(mTelephonyManager.getNetworkCountryIso()); }
    public String getNetworkOperatorNum() { return gets(mTelephonyManager.getNetworkOperator()); }
    public String getNetworkOperator() {    return gets(mTelephonyManager.getNetworkOperatorName()); } 
    public String getSimOperatorInt() {     return gets(mTelephonyManager.getSimOperator()); }
    public String getSimOperator() {        return gets(mTelephonyManager.getSimOperatorName()); }     
    //public String hasIccCard() {        	return (API >= 5 && mTelephonyManager.hasIccCard()) ? mTrue : mFalse; }
    public String isNetworkRoaming() {      return mTelephonyManager.isNetworkRoaming() ? TRUE : FALSE; }
   
    public String getCallStateInt() { return String.valueOf(mTelephonyManager.getCallState()); }
    public String getCallState() { 
        int x = mTelephonyManager.getCallState();
        if (x == TelephonyManager.CALL_STATE_IDLE)      return "Idle";
        if (x == TelephonyManager.CALL_STATE_OFFHOOK)   return "Off Hook";
        if (x == TelephonyManager.CALL_STATE_RINGING)   return "Ringing";
        return UNKNOWN;
    }
    public String getDataActivityInt() { return String.valueOf(mTelephonyManager.getDataActivity()); }
    public String getDataActivity() { 
        int x = mTelephonyManager.getDataActivity();
        if (API >= 4 && x == TelephonyManager.DATA_ACTIVITY_DORMANT)   return "Dormant";
        if (x == TelephonyManager.DATA_ACTIVITY_IN)                     return "In";
        if (x == TelephonyManager.DATA_ACTIVITY_INOUT)                  return "In+Out";
        if (x == TelephonyManager.DATA_ACTIVITY_NONE)                   return "None";
        if (x == TelephonyManager.DATA_ACTIVITY_OUT)                    return "Out";
        return UNKNOWN;
    }
    public String getDataStateInt() { return String.valueOf(mTelephonyManager.getDataState()); }
    public String getDataState() { 
        int x = mTelephonyManager.getDataState();
        if (x == TelephonyManager.DATA_CONNECTED)       return "Connected";
        if (x == TelephonyManager.DATA_CONNECTING)      return "Connecting";
        if (x == TelephonyManager.DATA_DISCONNECTED)    return "Disconnected";
        if (x == TelephonyManager.DATA_SUSPENDED)       return "Suspended";       
        return UNKNOWN;
    }
    public String getNetworkTypeInt() { return String.valueOf(mTelephonyManager.getNetworkType()); }
    public String getNetworkType() {                                                                   
        int x = mTelephonyManager.getNetworkType();
        if (API >= 4 && x == TelephonyManager.NETWORK_TYPE_1xRTT)   return "1xRTT";
        if (API >= 4 && x == TelephonyManager.NETWORK_TYPE_CDMA)    return "CDMA";
        if (x == TelephonyManager.NETWORK_TYPE_EDGE)                return "EDGE";
        //if (API >= 11 && x == TelephonyManager.NETWORK_TYPE_EHRPD)  return "eHRPD";
        if (API >= 4 && x == TelephonyManager.NETWORK_TYPE_EVDO_0)  return "EVDO-0";
        if (API >= 4 && x == TelephonyManager.NETWORK_TYPE_EVDO_A)  return "EVDO-A";
        //if (API >= 9 && x == TelephonyManager.NETWORK_TYPE_EVDO_B)  return "EVDO-B";
        if (x == TelephonyManager.NETWORK_TYPE_GPRS)                return "GPRS";
//        if (API >= 5 && x == TelephonyManager.NETWORK_TYPE_HSDPA)   return "HSDPA";
//        if (API >= 5 && x == TelephonyManager.NETWORK_TYPE_HSPA)    return "HSPA";
//        if (API >= 5 && x == TelephonyManager.NETWORK_TYPE_HSUPA)   return "HSUPA";
//        if (API >= 8 && x == TelephonyManager.NETWORK_TYPE_IDEN)    return "iDen";
//        if (API >= 11 && x == TelephonyManager.NETWORK_TYPE_LTE)    return "LTE";
        if (x == TelephonyManager.NETWORK_TYPE_UMTS)                return "UMTS";
        if (x == TelephonyManager.NETWORK_TYPE_UNKNOWN)             return "Unknown";
        return UNKNOWN;
    }
    public String getPhoneTypeInt() { return String.valueOf(mTelephonyManager.getPhoneType()); }
    public String getPhoneType() { 
        int x = mTelephonyManager.getPhoneType();
        if (API >= 4 && x == TelephonyManager.PHONE_TYPE_CDMA)  return "CDMA";
        if (x == TelephonyManager.PHONE_TYPE_GSM)               return "GSM";
        if (x == TelephonyManager.PHONE_TYPE_NONE)              return "None";
        //if (API >= 11 && x == TelephonyManager.PHONE_TYPE_SIP)  return "SIP";     
        return UNKNOWN;
    }
    public String getSimStateInt() { return String.valueOf(mTelephonyManager.getSimState()); }
    public String getSimState() { 
        int x = mTelephonyManager.getSimState();
        if (x == TelephonyManager.SIM_STATE_ABSENT)         return "Absent";
        if (x == TelephonyManager.SIM_STATE_NETWORK_LOCKED) return "Network Locked";
        if (x == TelephonyManager.SIM_STATE_PIN_REQUIRED)   return "PIN Required";
        if (x == TelephonyManager.SIM_STATE_PUK_REQUIRED)   return "PUK Required";    
        if (x == TelephonyManager.SIM_STATE_READY)          return "Ready";
        if (x == TelephonyManager.SIM_STATE_UNKNOWN)        return "Unknown";
        return UNKNOWN;
    }   
    
    // Service State
    public String getServiceMode() {            return mServiceState.getIsManualSelection() ? "Manual" : "Automatic"; }
    public String getServiceOperatorNum() {     return gets(mServiceState.getOperatorNumeric()); }
    public String getServiceOperator() {        return gets(mServiceState.getOperatorAlphaLong()); }
    public String getServiceOperatorShort() {   return gets(mServiceState.getOperatorAlphaShort()); }
    public String isServiceRoaming() {          return mServiceState.getRoaming() ? TRUE : FALSE; }
 
    public String getServiceStateInt() { return String.valueOf(mServiceState.getState()); }
    public String getServiceState() {
        int x = mServiceState.getState();
        if (x == ServiceState.STATE_EMERGENCY_ONLY) return "Emergency Only";
        if (x == ServiceState.STATE_IN_SERVICE)     return "In Service";
        if (x == ServiceState.STATE_OUT_OF_SERVICE) return "Out of Service";
        if (x == ServiceState.STATE_POWER_OFF)      return "Power Off";  
        return UNKNOWN;
    }
    
    // Signal Strength (need to use listeners)
    //public int getCdmaDbm() {  }
    
    
    
    //
    // android.os.Build properties. The Build.VERSION_CODES are not included.
    //
    
    public String getBuildBoard() {                 return gets(Build.BOARD); }
    //public String getBuildBootloader() {            if (API < 8) return EMPTY; return gets(Build.BOOTLOADER); }
    public String getBuildBrand() {                 return gets(Build.BRAND); }
    public String getBuildCpuAbi() {                return API < 4 ? UNKNOWN : gets(Build.CPU_ABI); }
    //public String getBuildCpuAbi2() {               return API < 8 ? EMPTY : gets(Build.CPU_ABI2); }
    public String getBuildDevice() {                return gets(Build.DEVICE); }
    public String getBuildDisplay() {               return API < 3 ? UNKNOWN : gets(Build.DISPLAY); }
    public String getBuildFingerprint() {           return gets(Build.FINGERPRINT); }
    //public String getBuildHardware() {              return API < 8 ? EMPTY : gets(Build.HARDWARE); }
    public String getBuildHost() {                  return gets(Build.HOST); }
    public String getBuildId() {                    return gets(Build.ID); }
    public String getBuildManufacturer() {          return API < 4 ? UNKNOWN : gets(Build.MANUFACTURER); }
    public String getBuildModel() {                 return gets(Build.MODEL); }
    public String getBuildProduct() {               return gets(Build.PRODUCT); }
    //public String getBuildRadio() {                 return API < 8 ? EMPTY : gets(Build.RADIO); }
    //public String getBuildSerial() {                return API < 9 ? EMPTY : gets(Build.SERIAL); }
    public String getBuildTags() {                  return gets(Build.TAGS); }
    public String getBuildTime() {                  return String.valueOf(Build.TIME); }
    public String getBuildType() {                  return gets(Build.TYPE); }
    public String getBuildUnknown() {               return gets(Build.UNKNOWN); }
    public String getBuildUser() {                  return gets(Build.USER); }
    public String getBuildVersionCodename() {       return gets(Build.VERSION.CODENAME); }
    public String getBuildVersionIncremental() {    return gets(Build.VERSION.INCREMENTAL); }
    public String getBuildVersionRelease() {        return gets(Build.VERSION.RELEASE); }
    public String getBuildVersionSdk() {            
    	if (API < 4) return gets(Build.VERSION.SDK);
    	return String.valueOf(Build.VERSION.SDK_INT);
    }


    //
    // Shell `getprop` properties
    //
    
    // Function to shorten property retrieval
    public String getProp(String prop) {
        if (prop == null || prop.length() == 0) return UNKNOWN;        
       String s = UNKNOWN;         
        try { s = Library.shellExec("getprop " + prop).get(0).trim(); }
        catch (IOException e) {}
        catch (SecurityException e) {}         
        if (s == "[]") return UNKNOWN;
        return s;           
    }
   
    public String getPropBuildPlatform() {  return getProp("ro.build.platform"); }
    public String getPropModelInternal() {  return getProp("ro.product.model.internal"); }
    public String getPropSerial() {         return getProp("ro.serialno"); }
    public String getPropBaseband() {       return getProp("gsm.version.baseband"); }
    public String getPropBaseband2() {      return getProp("ro.baseband"); }
    public String getPropCarrier() {        return getProp("ro.carrier"); }
    public String getPropBuildId() {        return getProp("ro.build.id"); }
    public String getPropDate() {           return getProp("ro.build.date"); }
    public String getPropName() {           return getProp("ro.product.name"); }
    public String getPropDescription() {    return getProp("ro.build.description"); }
    public String getPropRil() {            return getProp("gsm.version.ril-impl"); }
    public String getPropRilBarcode() {     return getProp("ro.ril.barcode"); }
    public String getPropOperator() {       return getProp("gsm.sim.operator.alpha"); } //ro.cdma.home.operator.alpha
    public String getPropOperatorId() {     return getProp("gsm.sim.operator.numeric"); } //ro.cdma.home.operator.numeric
    public String getPropCountryCode() {    return getProp("gsm.sim.operator.iso-country"); } //ro.product.locale.region persist.sys.country gsm.operator.iso-country
    public String getPropDefaultNetwork() { return getProp("ro.telephony.default_network"); }
    public String getPropNetworkType() {    return getProp("gsm.network.type"); } 
    public String getPropTimeZone() {       return getProp("persist.sys.timezone"); }
    public String getPropOpenglVersion() {  return getProp("ro.opengles.version"); }
    public String getPropLanguageCode() {   return getProp("persist.sys.language"); } //ro.product.locale.language
    public String getPropMultiTouch() {     return getProp("ro.product.multi_touch_enabled"); }
    public String getPropMaxNumTouch() {    return getProp("ro.product.max_num_touch"); }
    public String getPropHardware() {       return getProp("ro.hardware"); }
    public String getPropBootloader() {     return getProp("ro.bootloader"); }
    public String getPropCpuAbi2() {     return getProp("ro.product.cpu.abi2"); }

    //
    // Info from /proc
    //
    
    // Functions to shorten proc retrieval
    private List<String> getProc(String proc) {
        List<String> list = new ArrayList<String>();
        if (proc == null || proc.length() == 0) return list;               
        try { list = Library.shellExec("cat /proc/" + proc); }
        catch (IOException e) {}
        catch (SecurityException e) {}
        return list;
    }   
    public String getProcCpuField(String field) {
        if (field == null || field.length() == 0) return UNKNOWN;
        List<String> list = getProc("cpuinfo");
        for (String s : list) {
            String[] parts = s.split(":", 2);
            if (parts[0].trim().equals(field)) { return parts[1].trim(); }           
        }
        return UNKNOWN;
    }
    public String getProcMemField(String field) {
        if (field == null || field.length() == 0) return UNKNOWN;
        List<String> list = getProc("meminfo");
        for (String s : list) {
            String[] parts = s.split(":", 2);
            if (parts[0].trim().equals(field)) { return parts[1].trim(); }           
        }
        return UNKNOWN;
    }
    
    public String getProcPhoneId() { 
    	List<String> list = getProc("phoneid");
    	return list.size() == 0 ? UNKNOWN : list.get(0); 
	}
    
    // CPU
    public List<String> getProcCpuInfo() {      return getProc("cpuinfo"); } 
    
    public String getProcCpuDescription() {     return getProcCpuField("Processor"); }
    public String getProcCpuBogoMips() {        return getProcCpuField("BogoMIPS"); }
    public String getProcCpuFeatures() {        return getProcCpuField("Features"); }
    public String getProcCpuImplementer() {     return getProcCpuField("CPU implementer"); }
    public String getProcCpuArchitecture() {    return getProcCpuField("CPU architecture"); }
    public String getProcCpuVariant() {         return getProcCpuField("CPU variant"); }
    public String getProcCpuPart() {            return getProcCpuField("CPU part"); }
    public String getProcCpuRevision() {        return getProcCpuField("CPU revision"); }

    // Memory
    public List<String> getProcMemInfo() { return getProc("meminfo"); }
    
    public String getProcMemTotal() { return getProcMemField("MemTotal"); }
    public String getProcMemTotal(String scale) {
        if (scale.length() == 0) return UNKNOWN;      
        String[] parts = getProcMemTotal().split("\\s", 2);
        double value = 0.0;
        try { value = Double.valueOf(parts[0]); }
        catch (NumberFormatException e) {}
        return String.valueOf(Library.scaleData(value, parts[0], scale));
    }

    
    // Version
    public String getProcVersion() { return getProc("version").get(0); }
    public String getProcVersionKernel() { 
        String[] parts = getProc("version").get(0).split("\\s");
        return parts[2];
    }
    //public String getProcVersionDate() { 
        //String[] parts = getProc("version").get(0).split("PREEMPT");
        //return parts[1].trim();
    //}
    
    
    
    //
    // Sensors
    //
    
    public String getSensorType(int type) {
        if (type == Sensor.TYPE_ACCELEROMETER)                      return "Accelerometer";
        //if (API >= 9 && type == Sensor.TYPE_GRAVITY)               return "Gravity";
        if (type == Sensor.TYPE_GYROSCOPE)                          return "Gyroscope";
        if (type == Sensor.TYPE_LIGHT)                              return "Light";
        //if (API >= 9 && type == Sensor.TYPE_LINEAR_ACCELERATION)   return "Linear Acceleration";
        if (type == Sensor.TYPE_MAGNETIC_FIELD)                     return "Magnetic Field";
        if (type == Sensor.TYPE_ORIENTATION)                        return "Orientation";
        if (type == Sensor.TYPE_PRESSURE)                           return "Pressure";
        if (type == Sensor.TYPE_PROXIMITY)                          return "Proximity";
        //if (API >= 9 && type == Sensor.TYPE_ROTATION_VECTOR)       return "Rotation Vector";
        if (type == Sensor.TYPE_TEMPERATURE)                        return "Temperature";
        else return UNKNOWN;
    }
    
    public String getSensorDefault(Sensor s) { return mSensorManager.getDefaultSensor(s.getType()) == s ? TRUE : FALSE; }
    public String getSensorName(Sensor s) { return s.getName(); }
    public String getSensorVendor(Sensor s) { return s.getVendor(); }
    public String getSensorVersion(Sensor s) { return String.valueOf(s.getVersion()); }
    public String getSensorPower(Sensor s) { return String.valueOf(s.getPower()); }
    public String getSensorResolution(Sensor s) { return String.valueOf(s.getResolution()); }
    public String getSensorMaxRange(Sensor s) { return String.valueOf(s.getMaximumRange()); }
    //public String getSensorMinDelay(Sensor s) { return API >= 9 ? String.valueOf(s.getMinDelay()) : EMPTY; }
    
    /** Depreciated */
    public List<String> getSensorInfo() { 
        final String NL = "\n";        
        StringBuilder builder;  
        List<String> result = new ArrayList<String>();
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        String def; 
        for (Sensor s : sensors) {
            builder = new StringBuilder();                                   
            if (mSensorManager.getDefaultSensor(s.getType()) == s) def = "Yes";
            else def = "No";            
            builder.append("Type: ").append(getSensorType(s.getType())).append(NL)
                .append("Default: ").append(def).append(NL)
                .append("Name: ").append(s.getName()).append(NL)
                .append("Vendor: ").append(s.getVendor()).append(NL)
                .append("Version: ").append(s.getVersion()).append(NL)
                .append("Power: ").append(s.getPower()).append(NL)
                .append("Resolution: ").append(s.getResolution()).append(NL)
                .append("Max Range: ").append(s.getMaximumRange()).append(NL);
            //if (API >= 9) builder.append("Min Delay: ").append(s.getMinDelay()).append(NL);
            result.add(builder.toString());
        }
        return result;
    }
    
    
    //
    // Display
    //
          
    public String getDisplayWidthInches() {
    	NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(2); 
        return numFormat.format(mDisplay.getWidth() / mDisplayMetrics.xdpi);
    }
    public String getDisplayHeightInches() {
    	NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(2);
        return numFormat.format(mDisplay.getHeight() / mDisplayMetrics.ydpi);
    }
    public String getDisplayDiagonalInches() {
    	NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(2);
        
        return numFormat.format(
        		Math.sqrt(
        		Math.pow(mDisplay.getWidth() / mDisplayMetrics.xdpi, 2) +
        		Math.pow(mDisplay.getHeight() / mDisplayMetrics.ydpi, 2)));
    }
    public String getDisplayWidth() { return String.valueOf(mDisplay.getWidth()); }
    public String getDisplayHeight() { return String.valueOf(mDisplay.getHeight()); }
    public String getDisplayDpiX() { return String.valueOf(mDisplayMetrics.xdpi); }
    public String getDisplayDpiY() { return String.valueOf(mDisplayMetrics.ydpi); }
    public String getDisplayRefreshRate() { return String.valueOf(mDisplay.getRefreshRate()); }
    public String getDisplayLogicalDensity() { return String.valueOf(mDisplayMetrics.density); }
    public String getDisplayRotation() { 
        //if (API < 8) 
        return String.valueOf(mDisplay.getOrientation());
        //return String.valueOf(mDisplay.getRotation());
    }
    public String getDisplayDpi() {
        if (API < 4) return String.valueOf((int) (160 * mDisplayMetrics.density));
        return String.valueOf(mDisplayMetrics.densityDpi); // doesn't get xhdpi?
    }
    public String getDisplayDensity() {
        if (API < 4) {
            if ((int) (160 * mDisplayMetrics.density) == 120) return "Low";
            if ((int) (160 * mDisplayMetrics.density) == 160) return "Medium";
            if ((int) (160 * mDisplayMetrics.density) == 240) return "High";
        }
        else {
            if (mDisplayMetrics.densityDpi == DisplayMetrics.DENSITY_HIGH) return "High";
            if (mDisplayMetrics.densityDpi == DisplayMetrics.DENSITY_LOW) return "Low";
            if (mDisplayMetrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM) return "Medium";
            //if ((API >= 9) && (mDisplayMetrics.densityDpi == DisplayMetrics.DENSITY_XHIGH)) return "Extra High";
        }
        return UNKNOWN;
    }
    public String getPixelFormat() {
        if (API < 8) {
            if (mDisplay.getPixelFormat() == PixelFormat.JPEG) return "JPEG";
            if (mDisplay.getPixelFormat() == PixelFormat.YCbCr_420_SP) return "YCbCr_420_SP";
            //if ((API >= 5) && (mDisplay.getPixelFormat() == PixelFormat.YCbCr_422_I)) return "YCbCr_422_I";
            if (mDisplay.getPixelFormat() == PixelFormat.YCbCr_422_SP) return "YCbCr_422_SP";            
        }
        else {
//            if (mDisplay.getPixelFormat() == ImageFormat.JPEG) return "JPEG";
//            if (mDisplay.getPixelFormat() == ImageFormat.NV21) return "NV21";
//            if (mDisplay.getPixelFormat() == ImageFormat.YUY2) return "YUY2"; 
//            if (mDisplay.getPixelFormat() == ImageFormat.NV16) return "NV16";                          
        }        
        if (mDisplay.getPixelFormat() == PixelFormat.L_8) return "L_8";
        if (mDisplay.getPixelFormat() == PixelFormat.LA_88) return "LA_88";
        if (mDisplay.getPixelFormat() == PixelFormat.OPAQUE) return "Opaque";
        if (mDisplay.getPixelFormat() == PixelFormat.RGB_332) return "RGB 332";
        if (mDisplay.getPixelFormat() == PixelFormat.RGB_565) return "RGB 565";
        if (mDisplay.getPixelFormat() == PixelFormat.RGB_888) return "RGB 888";
        if (mDisplay.getPixelFormat() == PixelFormat.RGBA_4444) return "RGBA 4444";
        if (mDisplay.getPixelFormat() == PixelFormat.RGBA_5551) return "RGBA 5551";
        if (mDisplay.getPixelFormat() == PixelFormat.RGBA_8888) return "RGBA 8888";
        if (mDisplay.getPixelFormat() == PixelFormat.RGBX_8888) return "RGBX 8888";
        if (mDisplay.getPixelFormat() == PixelFormat.TRANSLUCENT) return "Translucent";
        if (mDisplay.getPixelFormat() == PixelFormat.TRANSPARENT) return "Transparent";
        if (mDisplay.getPixelFormat() == PixelFormat.UNKNOWN) return "Unknown";
        return UNKNOWN;
    }    
}