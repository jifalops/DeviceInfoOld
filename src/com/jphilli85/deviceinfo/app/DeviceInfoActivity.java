package com.jphilli85.deviceinfo.app;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.GroupListFragment.OnGroupSelectedListener;

public class DeviceInfoActivity extends SherlockFragmentActivity implements 
		OnGroupSelectedListener {

//	private SQLiteDatabase mDatabase;
		
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		if (DeviceInfo.getAppContext() == null) DeviceInfo.init(getApplication());
		
//		mDatabase = (new DeviceInfoDatabaseHelper(this)).getReadableDatabase();
		
		setContentView(R.layout.main);

	}

	@Override
	public void onGroupSelected(int id) {
		//Toast.makeText(this, "Group " + id + " selected", Toast.LENGTH_SHORT).show();		
	}
}
