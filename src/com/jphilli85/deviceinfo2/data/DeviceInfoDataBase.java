
package com.jphilli85.deviceinfo2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo2.DeviceInfoActivity;

public class DeviceInfoDataBase extends SQLiteOpenHelper {
    private static final String LOG_TAG = "DeviceInfoDataBase";
    
    private static final String DB_NAME = "info_element";
    private static final int DB_VERSION = 1;

    /** 
     * Subgroup table 
     * 
     * Specifies the various categories of information that the
     * app can show (Battery, GPS, CPU, Display, Software, etc.).
     */
    public static final String TABLE_SUBGROUP = "subgroup";
    public static final String ID = "_id";
    /** The name is used to determine what data to show.*/ 
    public static final String COL_NAME = "name";   
    /** The label shown to the user. */
    public static final String COL_LABEL = "label";
    /** Toggle display of this group or subgroup */    
    public static final String COL_HIDDEN = "hidden";
    
    /** 
     * Group table 
     * 
     * The top-level grouping of the other subgroups. These
     * will be the list of views a user can choose from.
     */    
    public static final String TABLE_GROUP = "group";
    // Reuse ID   
    // Reuse COL_LABEL
    /** 
     * The order to list groups or subgroups within a group.
     * Lowest number is listed first (highest). The order of 
     * two groups with the same index can vary. 
     */ 
    public static final String COL_INDEX = "index";
    // Reuse COL_HIDDEN
    
    /**
     * Subgroup-Group table
     * 
     * Defines the mapping of a group to the subgroups it contains.
     */ 
    public static final String TABLE_SUBGROUP_GROUP = "subgroup_group";
    // Reuse ID
    public static final String COL_GROUP_ID = "group_id";
    public static final String COL_SUBGROUP_ID = "subgroup_id";
    // Reuse COL_INDEX

    
    /*
     * Create tables
     */
    
    private static final String CREATE_TABLE_SUBGROUP = "CREATE TABLE " + TABLE_SUBGROUP 
    		+ " (" + ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ COL_NAME + " text UNIQUE NOT NULL, " 
    		+ COL_LABEL + " text UNIQUE NOT NULL, "
    		+ COL_HIDDEN + " integer NOT NULL default 0);";
    
    private static final String CREATE_TABLE_GROUP = "CREATE TABLE " + TABLE_GROUP 
    		+ " (" + ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ COL_LABEL + " text UNIQUE NOT NULL, " 
    		+ COL_INDEX + " integer NOT NULL default 0, "
    		+ COL_HIDDEN + " integer NOT NULL default 0);";
    
    private static final String CREATE_TABLE_SUBGROUP_GROUP = "CREATE TABLE " + TABLE_SUBGROUP_GROUP 
    		+ " (" + ID + " integer PRIMARY KEY AUTOINCREMENT, " 
    		+ COL_GROUP_ID + " integer NOT NULL, "
    		+ COL_SUBGROUP_ID + " integer NOT NULL, " 
    		+ COL_INDEX + " integer NOT NULL default 0);";
    
    
    /*
     * The actual subgroups
     */
    public static final String SUBGROUP_OVERVIEW = "SUBGROUP_OVERVIEW";
    public static final String SUBGROUP_CPU = "SUBGROUP_CPU";
    public static final String SUBGROUP_RAM = "SUBGROUP_RAM";
    public static final String SUBGROUP_STORAGE = "SUBGROUP_STORAGE";
    public static final String SUBGROUP_DISPLAY = "SUBGROUP_DISPLAY";
    public static final String SUBGROUP_GRAPHICS = "SUBGROUP_GRAPHICS";
    public static final String SUBGROUP_CAMERA = "SUBGROUP_CAMERA";
    public static final String SUBGROUP_AUDIO = "SUBGROUP_AUDIO";    
    public static final String SUBGROUP_BATTERY = "SUBGROUP_BATTERY";
    public static final String SUBGROUP_SENSORS = "SUBGROUP_SENSORS";
    public static final String SUBGROUP_GPS = "SUBGROUP_GPS";
    public static final String SUBGROUP_MOBILE = "SUBGROUP_MOBILE";
    public static final String SUBGROUP_WIFI = "SUBGROUP_WIFI";
    public static final String SUBGROUP_BLUETOOTH = "SUBGROUP_BLUETOOTH";
    public static final String SUBGROUP_PLATFORM = "SUBGROUP_SOFTWARE";
    public static final String SUBGROUP_PROPERTIES = "SUBGROUP_PROPERTIES";
    public static final String SUBGROUP_AVAILABLE_KEYS = "SUBGROUP_AVAILABLE_KEYS";
    public static final String SUBGROUP_LOGCAT = "SUBGROUP_LOGCAT";
    public static final String SUBGROUP_IDENTIFIERS = "SUBGROUP_IDENTIFIERS";
    

    public DeviceInfoDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
    	db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBGROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBGROUP_GROUP);
        
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
        values.put(COL_NAME, SUBGROUP_OVERVIEW);
        values.put(COL_LABEL, getString(R.string.subgroup_label_overview));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // CPU [2]
        values.clear();
        values.put(COL_NAME, SUBGROUP_CPU);
        values.put(COL_LABEL, getString(R.string.subgroup_label_cpu));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // RAM [3]
        values.clear();
        values.put(COL_NAME, SUBGROUP_RAM);
        values.put(COL_LABEL, getString(R.string.subgroup_label_ram));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Storage [4]
        values.clear();
        values.put(COL_NAME, SUBGROUP_STORAGE);
        values.put(COL_LABEL, getString(R.string.subgroup_label_storage));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Display [5]
        values.clear();
        values.put(COL_NAME, SUBGROUP_DISPLAY);
        values.put(COL_LABEL, getString(R.string.subgroup_label_display));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Graphics [6]
        values.clear();
        values.put(COL_NAME, SUBGROUP_GRAPHICS);
        values.put(COL_LABEL, getString(R.string.subgroup_label_graphics));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Camera [7]
        values.clear();
        values.put(COL_NAME, SUBGROUP_CAMERA);
        values.put(COL_LABEL, getString(R.string.subgroup_label_camera));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Audio [8]
        values.clear();
        values.put(COL_NAME, SUBGROUP_AUDIO);
        values.put(COL_LABEL, getString(R.string.subgroup_label_audio));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Battery [9]
        values.clear();
        values.put(COL_NAME, SUBGROUP_BATTERY);
        values.put(COL_LABEL, getString(R.string.subgroup_label_battery));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Sensors [10]
        values.clear();
        values.put(COL_NAME, SUBGROUP_SENSORS);
        values.put(COL_LABEL, getString(R.string.subgroup_label_sensors));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // GPS [11]
        values.clear();
        values.put(COL_NAME, SUBGROUP_GPS);
        values.put(COL_LABEL, getString(R.string.subgroup_label_gps));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Mobile [12]
        values.clear();
        values.put(COL_NAME, SUBGROUP_MOBILE);
        values.put(COL_LABEL, getString(R.string.subgroup_label_mobile));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // WiFi [13]
        values.clear();
        values.put(COL_NAME, SUBGROUP_WIFI);
        values.put(COL_LABEL, getString(R.string.subgroup_label_wifi));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Bluetooth [14]
        values.clear();
        values.put(COL_NAME, SUBGROUP_BLUETOOTH);
        values.put(COL_LABEL, getString(R.string.subgroup_label_bluetooth));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Platform [15]
        values.clear();
        values.put(COL_NAME, SUBGROUP_PLATFORM);
        values.put(COL_LABEL, getString(R.string.subgroup_label_platform));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Properties [16]
        values.clear();
        values.put(COL_NAME, SUBGROUP_PROPERTIES);
        values.put(COL_LABEL, getString(R.string.subgroup_label_properties));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Available Keys [17]
        values.clear();
        values.put(COL_NAME, SUBGROUP_AVAILABLE_KEYS);
        values.put(COL_LABEL, getString(R.string.subgroup_label_available_keys));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Logcat [18]
        values.clear();
        values.put(COL_NAME, SUBGROUP_LOGCAT);
        values.put(COL_LABEL, getString(R.string.subgroup_label_logcat));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        // Identifiers [19]
        values.clear();
        values.put(COL_NAME, SUBGROUP_IDENTIFIERS);
        values.put(COL_LABEL, getString(R.string.subgroup_label_identifiers));     
        db.insert(TABLE_SUBGROUP, null, values);
        
        //
        // TABLE_GROUP
        //
        
        // Overview [1]
        values.clear();
        values.put(COL_LABEL, getString(R.string.group_label_overview));
        values.put(COL_INDEX, -1);
        db.insert(TABLE_GROUP, null, values);
        
        // CPU [2]
        values.clear();
        values.put(COL_LABEL, getString(R.string.group_label_cpu));
        values.put(COL_INDEX, 0);
        db.insert(TABLE_GROUP, null, values);
        
        // Memory [3]
        values.clear();
        values.put(COL_LABEL, getString(R.string.group_label_memory));
        values.put(COL_INDEX, 1);
        db.insert(TABLE_GROUP, null, values);
        
        // Visual & Audio [4]
        values.clear();
        values.put(COL_LABEL, getString(R.string.group_label_visual_audio));
        values.put(COL_INDEX, 2);
        db.insert(TABLE_GROUP, null, values);
        
        // Battery & Sensors [5]
        values.clear();
        values.put(COL_LABEL, getString(R.string.group_label_battery_sensors));
        values.put(COL_INDEX, 3);
        db.insert(TABLE_GROUP, null, values);
        
        // Connections [6]
        values.clear();
        values.put(COL_LABEL, getString(R.string.group_label_connections));
        values.put(COL_INDEX, 4);
        db.insert(TABLE_GROUP, null, values);
        
        // System [7]
        values.clear();
        values.put(COL_LABEL, getString(R.string.group_label_system));
        values.put(COL_INDEX, 5);
        db.insert(TABLE_GROUP, null, values);
        
        //
        // TABLE_SUBGROUP_GROUP
        //
        
        // Overview subgroups 

        // Overview 
        values.clear();
        values.put(COL_GROUP_ID, 1);
        values.put(COL_SUBGROUP_ID, 1);
        values.put(COL_INDEX, -1);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // CPU subgroups 

        // CPU 
        values.clear();
        values.put(COL_GROUP_ID, 2);
        values.put(COL_SUBGROUP_ID, 2);
        values.put(COL_INDEX, 0);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Memory subgroups 

        // RAM 
        values.clear();
        values.put(COL_GROUP_ID, 3);
        values.put(COL_SUBGROUP_ID, 3);
        values.put(COL_INDEX, 0);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
    	// Storage 
        values.clear();
        values.put(COL_GROUP_ID, 3);
        values.put(COL_SUBGROUP_ID, 4);
        values.put(COL_INDEX, 1);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Visual & Audio subgroups 

        // Display
        values.clear();
        values.put(COL_GROUP_ID, 4);
        values.put(COL_SUBGROUP_ID, 5);
        values.put(COL_INDEX, 0);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Graphics
        values.clear();
        values.put(COL_GROUP_ID, 4);
        values.put(COL_SUBGROUP_ID, 6);
        values.put(COL_INDEX, 1);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Camera
        values.clear();
        values.put(COL_GROUP_ID, 4);
        values.put(COL_SUBGROUP_ID, 7);
        values.put(COL_INDEX, 2);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Audio
        values.clear();
        values.put(COL_GROUP_ID, 4);
        values.put(COL_SUBGROUP_ID, 8);
        values.put(COL_INDEX, 3);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Battery & Sensors subgroups 

        // Battery
        values.clear();
        values.put(COL_GROUP_ID, 5);
        values.put(COL_SUBGROUP_ID, 9);
        values.put(COL_INDEX, 0);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Sensors
        values.clear();
        values.put(COL_GROUP_ID, 5);
        values.put(COL_SUBGROUP_ID, 10);
        values.put(COL_INDEX, 2);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // GPS
        values.clear();
        values.put(COL_GROUP_ID, 5);
        values.put(COL_SUBGROUP_ID, 11);
        values.put(COL_INDEX, 1);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Connections subgroups 

        // Mobile
        values.clear();
        values.put(COL_GROUP_ID, 6);
        values.put(COL_SUBGROUP_ID, 12);
        values.put(COL_INDEX, 0);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // WiFi
        values.clear();
        values.put(COL_GROUP_ID, 6);
        values.put(COL_SUBGROUP_ID, 13);
        values.put(COL_INDEX, 1);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Bluetooth
        values.clear();
        values.put(COL_GROUP_ID, 6);
        values.put(COL_SUBGROUP_ID, 14);
        values.put(COL_INDEX, 2);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // System subgroups 

        // Platform
        values.clear();
        values.put(COL_GROUP_ID, 7);
        values.put(COL_SUBGROUP_ID, 15);
        values.put(COL_INDEX, 0);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Properties
        values.clear();
        values.put(COL_GROUP_ID, 7);
        values.put(COL_SUBGROUP_ID, 16);
        values.put(COL_INDEX, 1);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Available Keys
        values.clear();
        values.put(COL_GROUP_ID, 7);
        values.put(COL_SUBGROUP_ID, 17);
        values.put(COL_INDEX, 3);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Logcat
        values.clear();
        values.put(COL_GROUP_ID, 7);
        values.put(COL_SUBGROUP_ID, 18);
        values.put(COL_INDEX, 4);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
        
        // Identifiers
        values.clear();
        values.put(COL_GROUP_ID, 7);
        values.put(COL_SUBGROUP_ID, 19);
        values.put(COL_INDEX, 2);
        db.insert(TABLE_SUBGROUP_GROUP, null, values);
    }


    private String getString(int resId) { 
    	return DeviceInfoActivity.sAppContext.getString(resId);
    }
}
