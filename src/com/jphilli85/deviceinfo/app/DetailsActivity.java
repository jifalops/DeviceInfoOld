package com.jphilli85.deviceinfo.app;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class DetailsActivity extends SherlockFragmentActivity {
	//TODO manage fragment in list activity
	private DetailsFragment mDetailsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
//		if (getResources().getConfiguration().orientation
//                == Configuration.ORIENTATION_LANDSCAPE) {
//            // If the screen is now in landscape mode, we can show the
//            // dialog in-line with the list so we don't need this activity.
//            finish();
//            return;
//        }
		
		if (savedInstanceState != null) {
			mDetailsFragment = (DetailsFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, DetailsFragment.class.getName());
		}
		
		if (mDetailsFragment == null) {
			int[] elements = getIntent().getExtras().getIntArray(DetailsFragment.KEY_ELEMENTS);			
			mDetailsFragment = DetailsFragment.newInstance(elements);
		}
		
		if (mDetailsFragment != null) {
            getSupportFragmentManager().beginTransaction()
            .add(android.R.id.content, mDetailsFragment)
            .commit();
        }	
		
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {	
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, DetailsFragment.class.getName(), mDetailsFragment);
	}
}
