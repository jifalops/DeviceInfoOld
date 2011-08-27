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

package com.jphilli85.deviceinfo;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jphilli85.deviceinfo.library.DeviceInfo;
import com.jphilli85.deviceinfo.library.Library;


/**
 * This is one of the my first Android projects. It is likely filled with inefficient
 * and improper coding styles.
 */
public class DeviceInfoActivity extends Activity {
    private static final String TAG = "DeviceInfoActivity";
    private LinearLayout mLayout;
    private static final int API = Build.VERSION.SDK_INT;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        loadInfo();
    }    
    
    private void loadInfo() {  
    	DeviceInfo di = new DeviceInfo(this);
    	SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    	List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sb;
        
        addCategory("General Information");
       
        addItem("Manufacturer", di.getBuildManufacturer(), true);
        addItem("Model", di.getBuildModel(), true);
        addItem("Model Internal", di.getPropModelInternal());         
        addItem("Hardware", di.getPropHardware());
        addItem("Board Platform", di.getPropBuildPlatform());
        addItem("Board", di.getBuildBoard());
        addItem("Device", di.getBuildDevice());        
        addItem("Brand", di.getBuildBrand(), true);
        addItem("System Partition", di.getSystemSize() + " Bytes");
        addItem("Data Partition", di.getDataSize() + " Bytes");
        addItem("RAM", di.getProcMemTotal());
        addItem("ICC Card", di.hasIccCard()); 
        
        addCategory("Device Identifiers");
        
        addItem("Device ID", di.getDeviceId());
        addItem("Android ID", di.getAndroidId());
        addItem("Phone ID", di.getProcPhoneId()); 
        addItem("SIM Serial", di.getSimSerial());
        addItem("Phone Number", di.getPhoneNumber());
        addItem("Device Serial", di.getPropSerial());
        addItem("Subscriber ID", di.getSubscriberId());
        
        addCategory("Network");
        
        addItem("Operator", di.getNetworkOperator(), true);
        addItem("Operator Numeric", di.getNetworkOperatorNum());
        addItem("Phone Type", di.getPhoneType());
        addItem("Network Type", di.getNetworkType());
        addItem("Baseband", di.getPropBaseband());
        addItem("Service Mode", di.getServiceMode());
        
        addCategory("Software");
        
        addItem("Android Version", di.getBuildVersionRelease());
        addItem("Incremental Version", di.getBuildVersionIncremental());
        addItem("Build Fingerprint", di.getBuildFingerprint());
        addItem("Build Time", di.getBuildTime());
        addItem("Kernel", di.getProcVersionKernel());
        addItem("Bootloader", di.getPropBootloader());
        addItem("RIL Version", di.getPropRil());
        addItem("RIL Barcode", di.getPropRilBarcode());
        addItem("OpenGL Version", di.getPropOpenglVersion());
        addItem("Software Version", di.getSoftwareVersion());
        
        addCategory("Display");
        
        addItem("Diagonal (inches)", di.getDisplayDiagonalInches());
        addItem("Width (inches)", di.getDisplayWidthInches());
        addItem("Height (inches)", di.getDisplayHeightInches());
        addItem("Width (pixels)", di.getDisplayWidth());
        addItem("Height (pixels)", di.getDisplayHeight());
        addItem("Density", di.getDisplayDensity());
        addItem("DPI (dots per inch)", di.getDisplayDpi());
        addItem("Actual DPI X", di.getDisplayDpiX());
        addItem("Actual DPI Y", di.getDisplayDpiY());
        addItem("Max Simultaneous Touch", di.getPropMaxNumTouch());
        addItem("Refresh Rate", di.getDisplayRefreshRate());
        addItem("Pixel/Image Format", di.getPixelFormat());
        
        addCategory("CPU");
        
        addItem("Description", di.getProcCpuDescription());
        addItem("Features", di.getProcCpuFeatures());
        addItem("BogoMIPS", di.getProcCpuBogoMips());
        addItem("ABI", di.getBuildCpuAbi());
        addItem("ABI2", di.getPropCpuAbi2());
        addItem("Architecture", di.getProcCpuArchitecture());
        addItem("Implementer", di.getProcCpuImplementer());
        addItem("Variant", di.getProcCpuVariant());
        addItem("Part", di.getProcCpuPart());
        addItem("Revision", di.getProcCpuRevision());
        
        addCategory("Sensors");     
        
        for (Sensor s : sensors) {
            sb = new StringBuilder();                                             
            sb.append("Name: ").append(di.getSensorName(s)).append("\n")
                .append("Default: ").append(di.getSensorDefault(s)).append("\n")                
                .append("Vendor: ").append(di.getSensorVendor(s)).append("\n")
                .append("Version: ").append(di.getSensorVersion(s)).append("\n")
                .append("Power: ").append(di.getSensorPower(s)).append("\n")
                .append("Resolution: ").append(di.getSensorResolution(s)).append("\n")
                .append("Max Range: ").append(di.getSensorMaxRange(s)).append("\n");
            if (API >= 9) sb.append("Min Delay: ").append(di.getSensorMinDelay(s));
            addItem(di.getSensorType(s.getType()), sb.toString());         
        }       
    }
  
    // could show a category with no items
    private void addCategory(String name) {
        mLayout.addView(new CategoryTextView(this, name), 
        		new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        mLayout.addView(new SeparatorTextView(this), 
        		new LayoutParams(LayoutParams.FILL_PARENT, Library.dpToPx(this, 1)));
    }
    
    private void addItem(String name, String value) { addItem(name, value, false); }
    private void addItem(String name, String value, boolean ucWords) {
        if (value != null && value.length() > 0) {
            mLayout.addView(new KeyTextView(this, name));
            if (ucWords) value = Library.ucWords(value);
            mLayout.addView(new ValueTextView(this, value));
        }
    }
    
   private static class CategoryTextView extends TextView {        
        public CategoryTextView(Context context, String text) {
            super(context);
            int pad = Library.dpToPx(context, 5);          
            setTextAppearance(context, android.R.style.TextAppearance_Large);
            setBackgroundColor(0x33cccccc);
            setPadding(pad, pad, pad, pad);
            setGravity(Gravity.CENTER_VERTICAL);  
            setText(text);
        }
    }
    
    private static class KeyTextView extends TextView {        
        public KeyTextView(Context context, String text) {
            super(context);
            int indent = Library.dpToPx(context, 5);
            int pad = Library.dpToPx(context, 2);
            setTextAppearance(context, android.R.style.TextAppearance_Medium);
            setPadding(indent, pad, pad, pad);
            setText(text);
        }
    }
    
    private static class ValueTextView extends TextView {      
        public ValueTextView(Context context, String text) {
            super(context);
            int indent = Library.dpToPx(context, 20);
            int pad = Library.dpToPx(context, 2);
            setPadding(indent, pad, pad, pad);
            setText(text);
        }
    }
    
    private static class SeparatorTextView extends TextView {      
        public SeparatorTextView(Context context) {
            super(context);
            setBackgroundColor(Color.LTGRAY);
        }
    }
}