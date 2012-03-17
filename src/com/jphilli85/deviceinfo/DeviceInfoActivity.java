package com.jphilli85.deviceinfo;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.jphilli85.deviceinfo.GroupListFragment.OnGroupSelectedListener;
import com.jphilli85.deviceinfo.data.DeviceInfo;
import com.jphilli85.deviceinfo.data.DeviceInfoDatabaseHelper;

public class DeviceInfoActivity extends FragmentActivity implements 
		OnGroupSelectedListener {
	
	public static Context sAppContext;
	private SQLiteDatabase mDatabase;
		
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		if (sAppContext == null) sAppContext = getApplicationContext();
		
		mDatabase = (new DeviceInfoDatabaseHelper(this)).getReadableDatabase();
		
		setContentView(R.layout.main);			
	}

	@Override
	public void onGroupSelected(int id) {
		Toast.makeText(this, "Group " + id + " selected", Toast.LENGTH_SHORT).show();		
	}
}
