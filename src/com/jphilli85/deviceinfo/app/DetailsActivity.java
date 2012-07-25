package com.jphilli85.deviceinfo.app;

import android.content.res.Configuration;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class DetailsActivity extends SherlockFragmentActivity {
	//TODO manage fragment in list activity
	private DetailsFragment mDetailsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		Configuration config = getResources().getConfiguration();
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE 
//				&& ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) 
//						> Configuration.SCREENLAYOUT_SIZE_LARGE)
				) {
            finish();
            return;
        }
		
		if (savedInstanceState != null) {
			mDetailsFragment = (DetailsFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, DetailsFragment.class.getName());
		}
		
		if (mDetailsFragment == null) {
			int[] elements = null;
			try { elements = getIntent().getExtras().getIntArray(DetailsFragment.KEY_ELEMENTS); }
			catch (NullPointerException ignored) {}
			// works with null
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
//		getSupportFragmentManager().putFragment(outState, DetailsFragment.class.getName(), mDetailsFragment);
	}
}
