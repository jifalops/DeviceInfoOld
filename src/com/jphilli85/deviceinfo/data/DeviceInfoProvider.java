package com.jphilli85.deviceinfo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.jphilli85.deviceinfo.data.DeviceInfo.*;

public class DeviceInfoProvider extends ContentProvider {
	private static final String LOG_TAG = "DeviceInfoProvider";
	
    private DeviceInfoDatabaseHelper mDB;

    /** Get all the groups */
    public static final int GROUP = 1;
    /** Get a group's subgroups using its ID */
    public static final int GROUP_ID = 2;
    /** Get a group's subgroups using its name */
    public static final int GROUP_NAME = 7;
    /** Get all subgroups */
    public static final int SUBGROUP = 3;
    /** Get a subgroup by ID */
    public static final int SUBGROUP_ID = 4;
    /** Get a subgroup by name */
    public static final int SUBGROUP_NAME = 8;
    /** Get all the mappings of subgroups to groups */
    public static final int SUBGROUPGROUP = 5; 

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, Group.TABLE_NAME, GROUP);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, Group.TABLE_NAME + "/#", GROUP_ID);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, Group.TABLE_NAME + "/*", GROUP_NAME);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, Subgroup.TABLE_NAME, SUBGROUP);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, Subgroup.TABLE_NAME + "/#", SUBGROUP_ID);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, Subgroup.TABLE_NAME + "/*", SUBGROUP_NAME);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, SubgroupGroup.TABLE_NAME, SUBGROUPGROUP);  
    }

    @Override
    public boolean onCreate() {
        mDB = new DeviceInfoDatabaseHelper(getContext());
        return true;
    }
    
    @Override
    public String getType(Uri uri) {
    	switch (sUriMatcher.match(uri)) {
        case GROUP:
        	return Group.CONTENT_TYPE;              
        case GROUP_ID:        	
        	return Group.CONTENT_ITEM_TYPE;
        case GROUP_NAME:        	
        	return Group.CONTENT_ITEM_TYPE;
        case SUBGROUP:
        	return Subgroup.CONTENT_TYPE;
        case SUBGROUP_ID:
        	return Subgroup.CONTENT_ITEM_TYPE;
        case SUBGROUP_NAME:
        	return Subgroup.CONTENT_ITEM_TYPE;
        case SUBGROUPGROUP:
        	return SubgroupGroup.CONTENT_TYPE;
        default:
            return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

    	final String g = Group.TABLE_NAME;
    	final String sg = Subgroup.TABLE_NAME;
    	final String sgg = SubgroupGroup.TABLE_NAME;    	
    	
    	// Last path segment contains invalid characters
    	if (!uri.getLastPathSegment().matches("[0-9a-zA-Z_]+")) return null;
    	
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        
        //TODO create Intent flag to SHOW_HIDDEN
        switch (sUriMatcher.match(uri)) {
        case GROUP:
        	queryBuilder.setTables(g);       
        	break;
        case GROUP_ID:       
        	queryBuilder.setTables(sg + " INNER JOIN " + sgg + " ON " 
        			+ sg + "." + Subgroup.COL_ID + "=" + sgg + "." + SubgroupGroup.COL_SUBGROUP_ID);
        	queryBuilder.appendWhere(" " + sgg + "." + SubgroupGroup.COL_GROUP_ID + "="
                    + uri.getLastPathSegment());
        	if (selection == null || selection.isEmpty())
        		queryBuilder.appendWhere(" AND " + sg + "." + Subgroup.COL_HIDDEN + "=0");
        	if (sortOrder == null || sortOrder.isEmpty())
        		sortOrder = sgg + "." + SubgroupGroup.COL_INDEX + " ASC";
        	break;
        case GROUP_NAME:        	
        	queryBuilder.setTables(sg + " INNER JOIN " + sgg + " INNER JOIN " + g + " ON " 
        			+ sg + "." + Subgroup.COL_ID + "=" + sgg + "." + SubgroupGroup.COL_SUBGROUP_ID 
        			+ " AND " + g + "." + Group.COL_ID + "=" + sgg + "." + SubgroupGroup.COL_GROUP_ID);
        	queryBuilder.appendWhere(" " + g + "." + Group.COL_NAME + "="
                    + uri.getLastPathSegment());
        	if (selection == null || selection.isEmpty())
        		queryBuilder.appendWhere(" AND " + sg + "." + Subgroup.COL_HIDDEN + "=0");
        	if (sortOrder == null || sortOrder.isEmpty())
        		sortOrder = sgg + "." + SubgroupGroup.COL_INDEX + " ASC";
        	break;
        case SUBGROUP:
        	queryBuilder.setTables(sg);
        	break;
        case SUBGROUP_ID:        	
        	queryBuilder.setTables(sg);
        	queryBuilder.appendWhere(Subgroup.COL_ID + "="
                    + uri.getLastPathSegment());
        	break;
        case SUBGROUP_NAME:     
        	queryBuilder.setTables(sg);
        	queryBuilder.appendWhere(Subgroup.COL_NAME + "="
                    + uri.getLastPathSegment());
        	break;
        case SUBGROUPGROUP:
        	queryBuilder.setTables(sgg);
        	break;        
        default:
        	throw new IllegalArgumentException(LOG_TAG + ": Unknown URI " + uri);
        }


        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        int uriType = sUriMatcher.match(uri);
//        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
//        int rowsAffected = 0;
//        switch (uriType) {
//        case TUTORIALS:
//            rowsAffected = sqlDB.delete(TutListDatabase.TABLE_TUTORIALS,
//                    selection, selectionArgs);
//            break;
//        case TUTORIAL_ID:
//            String id = uri.getLastPathSegment();
//            if (TextUtils.isEmpty(selection)) {
//                rowsAffected = sqlDB.delete(TutListDatabase.TABLE_TUTORIALS,
//                        TutListDatabase.ID + "=" + id, null);
//            } else {
//                rowsAffected = sqlDB.delete(TutListDatabase.TABLE_TUTORIALS,
//                        selection + " and " + TutListDatabase.ID + "=" + id,
//                        selectionArgs);
//            }
//            break;
//        default:
//        	throw new IllegalArgumentException(LOG_TAG + ": Unknown URI " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return 0; //rowsAffected;
    }

    

    @Override
    public Uri insert(Uri uri, ContentValues values) {
//        int uriType = sUriMatcher.match(uri);
//        if (uriType != TUTORIALS) {
//            throw new IllegalArgumentException("Invalid URI for insert");
//        }
//        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
//        try {
//            long newID = sqlDB.insertOrThrow(TutListDatabase.TABLE_TUTORIALS,
//                    null, values);
//            if (newID > 0) {
//                Uri newUri = ContentUris.withAppendedId(uri, newID);
//                getContext().getContentResolver().notifyChange(uri, null);
//                return newUri;
//            } else {
//                throw new SQLException("Failed to insert row into " + uri);
//            }
//        } catch (SQLiteConstraintException e) {
//            Log.i(LOG_TAG, "Ignoring constraint failure.");
//        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
//        int uriType = sUriMatcher.match(uri);
//        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
//
//        int rowsAffected;
//
//        switch (uriType) {
//        case TUTORIAL_ID:
//            String id = uri.getLastPathSegment();
//            StringBuilder modSelection = new StringBuilder(TutListDatabase.ID
//                    + "=" + id);
//
//            if (!TextUtils.isEmpty(selection)) {
//                modSelection.append(" AND " + selection);
//            }
//
//            rowsAffected = sqlDB.update(TutListDatabase.TABLE_TUTORIALS,
//                    values, modSelection.toString(), null);
//            break;
//        case TUTORIALS:
//            rowsAffected = sqlDB.update(TutListDatabase.TABLE_TUTORIALS,
//                    values, selection, selectionArgs);
//            break;
//        default:
//            throw new IllegalArgumentException("Unknown URI");
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return 0; //rowsAffected;
    }
}
