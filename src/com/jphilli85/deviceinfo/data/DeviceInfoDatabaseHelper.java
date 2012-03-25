
package com.jphilli85.deviceinfo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.data.DeviceInfoContract.Group;
import com.jphilli85.deviceinfo.data.DeviceInfoContract.Subgroup;
import com.jphilli85.deviceinfo.data.DeviceInfoContract.SubgroupGroup;

public class DeviceInfoDatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "DeviceInfoDataBase";

    /*
     * Create tables
     */
    
    private static final String CREATE_TABLE_SUBGROUP = "CREATE TABLE " + Subgroup.TABLE_NAME 
    		+ " (" + Subgroup.COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ Subgroup.COL_NAME + " text UNIQUE NOT NULL, " 
    		+ Subgroup.COL_LABEL + " text UNIQUE NOT NULL, "
    		+ Subgroup.COL_HIDDEN + " integer NOT NULL default 0);";
    
    private static final String CREATE_TABLE_GROUP = "CREATE TABLE " + Group.TABLE_NAME 
    		+ " (" + Group.COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ Group.COL_NAME + " text UNIQUE NOT NULL, " 
    		+ Group.COL_LABEL + " text UNIQUE NOT NULL, " 
    		+ Group.COL_INDEX + " integer NOT NULL default 0, "
    		+ Group.COL_HIDDEN + " integer NOT NULL default 0);";
    
    private static final String CREATE_TABLE_SUBGROUP_GROUP = "CREATE TABLE " + SubgroupGroup.TABLE_NAME
    		+ " (" + SubgroupGroup.COL_ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ SubgroupGroup.COL_GROUP_ID + " integer NOT NULL, "
    		+ SubgroupGroup.COL_SUBGROUP_ID + " integer NOT NULL, " 
    		+ SubgroupGroup.COL_INDEX + " integer NOT NULL default 0);";
 
    
    public DeviceInfoDatabaseHelper(Context context) {
        super(context, DeviceInfoContract.DATABASE_NAME, null, DeviceInfoContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initialize(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");
        initialize(db);
    }
    
    private void initialize(SQLiteDatabase db) {
    	/*
    	 *  Remove existing data
    	 */
    	db.execSQL("DROP TABLE IF EXISTS " + Subgroup.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Group.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SubgroupGroup.TABLE_NAME);
        
        /*
         * Create tables with current structure
         */
        db.execSQL(CREATE_TABLE_SUBGROUP);
        db.execSQL(CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_SUBGROUP_GROUP);
        
        /*
         * Insert default values into each table        
         */
        
        ContentValues values = new ContentValues();
        
        //
        // TABLE_SUBGROUP
        //
        
        // Overview [1]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_OVERVIEW);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_overview));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // CPU [2]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_CPU);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_cpu));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // RAM [3]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_RAM);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_ram));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Storage [4]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_STORAGE);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_storage));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Display [5]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_DISPLAY);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_display));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Graphics [6]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_GRAPHICS);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_graphics));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Camera [7]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_CAMERA);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_camera));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Audio [8]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_AUDIO);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_audio));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Battery [9]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_BATTERY);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_battery));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Sensors [10]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_SENSORS);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_sensors));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // GPS [11]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_GPS);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_gps));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Mobile [12]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_MOBILE);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_mobile));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // WiFi [13]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_WIFI);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_wifi));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Bluetooth [14]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_BLUETOOTH);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_bluetooth));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Platform [15]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_PLATFORM);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_platform));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Properties [16]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_PROPERTIES);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_properties));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Available Keys [17]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_AVAILABLE_KEYS);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_available_keys));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Logcat [18]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_LOGCAT);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_logcat));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        // Identifiers [19]
        values.clear();
        values.put(Subgroup.COL_NAME, Subgroup.SUBGROUP_IDENTIFIERS);
        values.put(Subgroup.COL_LABEL, getString(R.string.subgroup_label_identifiers));     
        db.insert(Subgroup.TABLE_NAME, null, values);
        
        //
        // TABLE_GROUP
        //
        
        // Overview [1]
        values.clear();
        values.put(Group.COL_NAME, Group.GROUP_OVERVIEW);
        values.put(Group.COL_LABEL, getString(R.string.group_label_overview));
        values.put(Group.COL_INDEX, -1);
        db.insert(Group.TABLE_NAME, null, values);
        
        // CPU [2]
        values.clear();
        values.put(Group.COL_NAME, Group.GROUP_CPU);
        values.put(Group.COL_LABEL, getString(R.string.group_label_cpu));
        values.put(Group.COL_INDEX, 0);
        db.insert(Group.TABLE_NAME, null, values);
        
        // Memory [3]
        values.clear();
        values.put(Group.COL_NAME, Group.GROUP_MEMORY);
        values.put(Group.COL_LABEL, getString(R.string.group_label_memory));
        values.put(Group.COL_INDEX, 1);
        db.insert(Group.TABLE_NAME, null, values);
        
        // Visual & Audio [4]
        values.clear();
        values.put(Group.COL_NAME, Group.GROUP_VISUAL_AUDIO);
        values.put(Group.COL_LABEL, getString(R.string.group_label_visual_audio));
        values.put(Group.COL_INDEX, 2);
//        values.put(Group.COL_HIDDEN, 1);
        db.insert(Group.TABLE_NAME, null, values);
        
        // Battery & Sensors [5]
        values.clear();
        values.put(Group.COL_NAME, Group.GROUP_BATTERY_SENSORS);
        values.put(Group.COL_LABEL, getString(R.string.group_label_battery_sensors));
        values.put(Group.COL_INDEX, 3);
        db.insert(Group.TABLE_NAME, null, values);
        
        // Connections [6]
        values.clear();
        values.put(Group.COL_NAME, Group.GROUP_CONNECTIONS);
        values.put(Group.COL_LABEL, getString(R.string.group_label_connections));
        values.put(Group.COL_INDEX, 4);
        db.insert(Group.TABLE_NAME, null, values);
        
        // System [7]
        values.clear();
        values.put(Group.COL_NAME, Group.GROUP_SYSTEM);
        values.put(Group.COL_LABEL, getString(R.string.group_label_system));
        values.put(Group.COL_INDEX, 5);
        db.insert(Group.TABLE_NAME, null, values);
        
        //
        // TABLE_SUBGROUP_GROUP
        //
        
        // Overview subgroups 

        // Overview 
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 1);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 1);
        values.put(SubgroupGroup.COL_INDEX, -1);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // CPU subgroups 

        // CPU 
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 2);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 2);
        values.put(SubgroupGroup.COL_INDEX, 0);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Memory subgroups 

        // RAM 
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 3);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 3);
        values.put(SubgroupGroup.COL_INDEX, 0);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
    	// Storage 
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 3);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 4);
        values.put(SubgroupGroup.COL_INDEX, 1);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Visual & Audio subgroups 

        // Display
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 4);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 5);
        values.put(SubgroupGroup.COL_INDEX, 0);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Graphics
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 4);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 6);
        values.put(SubgroupGroup.COL_INDEX, 1);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Camera
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 4);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 7);
        values.put(SubgroupGroup.COL_INDEX, 2);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Audio
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 4);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 8);
        values.put(SubgroupGroup.COL_INDEX, 3);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Battery & Sensors subgroups 

        // Battery
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 5);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 9);
        values.put(SubgroupGroup.COL_INDEX, 0);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Sensors
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 5);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 10);
        values.put(SubgroupGroup.COL_INDEX, 2);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // GPS
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 5);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 11);
        values.put(SubgroupGroup.COL_INDEX, 1);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Connections subgroups 

        // Mobile
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 6);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 12);
        values.put(SubgroupGroup.COL_INDEX, 0);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // WiFi
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 6);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 13);
        values.put(SubgroupGroup.COL_INDEX, 1);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Bluetooth
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 6);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 14);
        values.put(SubgroupGroup.COL_INDEX, 2);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // System subgroups 

        // Platform
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 7);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 15);
        values.put(SubgroupGroup.COL_INDEX, 0);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Properties
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 7);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 16);
        values.put(SubgroupGroup.COL_INDEX, 1);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Available Keys
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 7);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 17);
        values.put(SubgroupGroup.COL_INDEX, 3);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Logcat
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 7);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 18);
        values.put(SubgroupGroup.COL_INDEX, 4);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
        
        // Identifiers
        values.clear();
        values.put(SubgroupGroup.COL_GROUP_ID, 7);
        values.put(SubgroupGroup.COL_SUBGROUP_ID, 19);
        values.put(SubgroupGroup.COL_INDEX, 2);
        db.insert(SubgroupGroup.TABLE_NAME, null, values);
    }


    private String getString(int resId) { 
    	return DeviceInfo.sAppContext.getString(resId);
    }
}
