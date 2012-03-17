package com.jphilli85.deviceinfo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DeviceInfoProvider extends ContentProvider {
	private static final String LOG_TAG = "DeviceInfoProvider";
	
    private DeviceInfoDatabase mDB;

    public static final int GROUP = 1;
    public static final int GROUP_ID = 2;
    public static final int SUBGROUP = 3;
    public static final int SUBGROUP_ID = 4;
    public static final int SUBGROUP_GROUP = 5;
    public static final int SUBGROUP_GROUP_ID = 6;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, DeviceInfo.Group.TABLE_NAME, GROUP);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, DeviceInfo.Group.TABLE_NAME + "/#", GROUP_ID);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, DeviceInfo.Subgroup.TABLE_NAME, SUBGROUP);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, DeviceInfo.Subgroup.TABLE_NAME + "/#", SUBGROUP_ID);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, DeviceInfo.SubgroupGroup.TABLE_NAME, SUBGROUP_GROUP);
        sUriMatcher.addURI(DeviceInfo.AUTHORITY, DeviceInfo.SubgroupGroup.TABLE_NAME + "/#", SUBGROUP_GROUP_ID);
    }

    @Override
    public boolean onCreate() {
        mDB = new DeviceInfoDatabase(getContext());
        return true;
    }
    
    @Override
    public String getType(Uri uri) {
    	switch (sUriMatcher.match(uri)) {
        case GROUP:
        	return DeviceInfo.Group.CONTENT_TYPE;              
        case GROUP_ID:        	
        	return DeviceInfo.Group.CONTENT_ITEM_TYPE;
        case SUBGROUP:
        	return DeviceInfo.Subgroup.CONTENT_TYPE;
        case SUBGROUP_ID:
        	return DeviceInfo.Subgroup.CONTENT_ITEM_TYPE;
        case SUBGROUP_GROUP:
        	return DeviceInfo.SubgroupGroup.CONTENT_TYPE;
        case SUBGROUP_GROUP_ID:
        	return DeviceInfo.SubgroupGroup.CONTENT_ITEM_TYPE;
        default:
            return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        
        switch (sUriMatcher.match(uri)) {
        case GROUP:
        	queryBuilder.setTables(DeviceInfo.Group.TABLE_NAME);       
        	break;
        case GROUP_ID:        	
        	queryBuilder.setTables(DeviceInfo.Group.TABLE_NAME);
        	break;
        case SUBGROUP:
        	queryBuilder.setTables(DeviceInfo.Subgroup.TABLE_NAME);
        	break;
        case SUBGROUP_ID:
        	queryBuilder.setTables(DeviceInfo.Subgroup.TABLE_NAME);
        	break;
        case SUBGROUP_GROUP:
        	queryBuilder.setTables(DeviceInfo.SubgroupGroup.TABLE_NAME);
        	break;
        case SUBGROUP_GROUP_ID:
        	queryBuilder.setTables(DeviceInfo.SubgroupGroup.TABLE_NAME);
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
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int rowsAffected = 0;
        switch (uriType) {
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
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
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
