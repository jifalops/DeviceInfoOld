package com.jphilli85.deviceinfo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.GroupListFragment.Callback;

public class DeviceInfoActivity extends SherlockFragmentActivity implements 
		Callback {
	
//	private SQLiteDatabase mDatabase;
	
	private GroupListFragment mGroupListFragment;
	private DetailsFragment mDetailsFragment;
	private int mCurrentGroup; 
	
	public static final String KEY_GROUP = DeviceInfoActivity.class.getName() + ".GROUP";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
//		mDatabase = (new DeviceInfoDatabaseHelper(this)).getReadableDatabase();
		
		setContentView(R.layout.main);
		
		
		
		
		if (savedInstanceState != null) {			
			mCurrentGroup = savedInstanceState.getInt(KEY_GROUP, 0);
			
			mGroupListFragment = (GroupListFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, GroupListFragment.class.getName()); 
			
			mDetailsFragment = (DetailsFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, DetailsFragment.class.getName());
		}
		
		if (mGroupListFragment == null) {
			mGroupListFragment = (GroupListFragment) getSupportFragmentManager().findFragmentById(R.id.groupListFragment);			
		}
		
		if (isDualPane()) {
			if (mDetailsFragment == null) {			
				int[] elements = null;
				try { elements = getIntent().getExtras().getIntArray(DetailsFragment.KEY_ELEMENTS); }
				catch (NullPointerException ignored) {}
				// works with null
				mDetailsFragment = DetailsFragment.newInstance(elements); 
			}
			
			getSupportFragmentManager().beginTransaction()
            .add(R.id.detailsFragmentWrapper, mDetailsFragment)
            .commit();
			
			showDetails(mCurrentGroup);
		}
//		else if (mDetailsFragment != null) {
//			getSupportFragmentManager().beginTransaction()
//            .remove(mDetailsFragment).commit();
//		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {	
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_GROUP, mCurrentGroup);
		if (mGroupListFragment != null) getSupportFragmentManager()
			.putFragment(outState, GroupListFragment.class.getName(), mGroupListFragment);
		if (mDetailsFragment != null) getSupportFragmentManager()
			.putFragment(outState, DetailsFragment.class.getName(), mDetailsFragment);        
	}

	@Override
	public void showDetails(int group) {
	 	mCurrentGroup = group;
        int[] elements = GroupListFragment.getElements(group);

        if (isDualPane()) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            mGroupListFragment.getListView().setItemChecked(group, true);

            // Check what fragment is currently shown, replace if needed.
            if (mDetailsFragment == null) {
	            mDetailsFragment = (DetailsFragment)
	                    getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
            }
            if (mDetailsFragment == null || GroupListFragment.getGroup(mDetailsFragment.getElements()) != group) {
                // Make new fragment to show this selection.
            	mDetailsFragment = DetailsFragment.newInstance(elements);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.detailsFragmentWrapper, mDetailsFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(this, DetailsActivity.class);
            intent.putExtra(DetailsFragment.KEY_ELEMENTS, elements);
            startActivity(intent);
        }
	}

	@Override
	public boolean isDualPane() {
		return findViewById(R.id.detailsFragmentWrapper) != null;    
	}
}
