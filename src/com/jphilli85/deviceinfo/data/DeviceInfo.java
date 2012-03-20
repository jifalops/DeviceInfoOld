package com.jphilli85.deviceinfo.data;

import android.content.ContentResolver;
import android.net.Uri;

public final class DeviceInfo {
	private DeviceInfo() { /* Not instantiable */ }
	
	public static final String AUTHORITY = "com.jphilli85.deviceinfo";
	public static final String DATABASE_NAME = "deviceinfo.db";
	public static final int DATABASE_VERSION = 1;
	
	/** 
     * Group table 
     * 
     * The top-level grouping of the other subgroups. These
     * will be the list of views a user can choose from.
     */    
	public static final class Group {		
		private Group() { /* Not instantiable */ }
		
		public static final String TABLE_NAME = "groups";
	    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	  
	    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + TABLE_NAME;
	    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + TABLE_NAME;
	    
	    public static final String COL_ID = "_id";
	    /** The name can be used to determine what data to show.*/ 
	    public static final String COL_NAME = "name";  
	    /** The label shown to the user. */
	    public static final String COL_LABEL = "label";
	    /** 
	     * The order to list groups or subgroups within a group.
	     * Lowest number is listed first (highest). The order of 
	     * two groups with the same index can vary. 
	     */ 
	    public static final String COL_INDEX = "index_";
	    /** Toggle display of this group or subgroup */    
	    public static final String COL_HIDDEN = "hidden";
	    
	    /*
	     * Group names
	     */
	    public static final String GROUP_OVERVIEW = "GROUP_OVERVIEW";
	    public static final String GROUP_CPU = "GROUP_CPU";
	    public static final String GROUP_MEMORY = "GROUP_MEMORY";
	    public static final String GROUP_VISUAL_AUDIO = "GROUP_VISUAL_AUDIO";
	    public static final String GROUP_BATTERY_SENSORS = "GROUP_BATTERY_SENSORS";
	    public static final String GROUP_CONNECTIONS = "GROUP_CONNECTIONS";
	    public static final String GROUP_SYSTEM = "GROUP_SYSTEM";
	}
	
	/** 
     * Subgroup table 
     * 
     * Specifies the various categories of information that the
     * app can show (Battery, GPS, CPU, Display, Software, etc.).
     */
	public static final class Subgroup {		
		private Subgroup() { /* Not instantiable */ }
		
		public static final String TABLE_NAME = "subgroup";
	    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	  
	    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + TABLE_NAME;
	    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + TABLE_NAME;
	    
	    public static final String COL_ID = "_id";
	    /** The name can be used to determine what data to show.*/ 
	    public static final String COL_NAME = "name";   
	    /** The label shown to the user. */
	    public static final String COL_LABEL = "label";
	    /** Toggle display of this group or subgroup */    
	    public static final String COL_HIDDEN = "hidden";
	    
	    /*
	     * Subgroup names
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
	}
	
	/**
     * Subgroup-Group table
     * 
     * Defines the mapping of a group to the subgroups it contains.
     */ 
	public static final class SubgroupGroup {
		private SubgroupGroup() { /* Not instantiable */ }
		
		public static final String TABLE_NAME = "subgroup_group";
	    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	  
	    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + TABLE_NAME;
	    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + TABLE_NAME;

	    public static final String COL_ID = "_id";
	    public static final String COL_GROUP_ID = "group_id";
	    public static final String COL_SUBGROUP_ID = "subgroup_id";
	    /** 
	     * The order to list groups or subgroups within a group.
	     * Lowest number is listed first (highest). The order of 
	     * two groups with the same index can vary. 
	     */ 
	    public static final String COL_INDEX = "index_";
	}
}
