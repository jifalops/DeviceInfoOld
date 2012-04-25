package com.jphilli85.deviceinfo.app;

import android.content.res.Configuration;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class DetailsActivity extends SherlockFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }
		
		//setContentView(R.layout.details_activity);
//		ViewGroup detailsWrapper = (ViewGroup)
//				findViewById();
		
		if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
			int group = getIntent().getExtras().getInt(GroupListFragment.KEY_GROUP, 0);
			
			DetailsFragment detailsFragment = DetailsFragment.newInstance(group);
//					(DetailsFragment) 
//					getSupportFragmentManager().
//					findFragmentById(R.id.detailsFragment);
//            detailsFragment.setArguments(getIntent().getExtras());  
            getSupportFragmentManager().beginTransaction()
            .add(android.R.id.content, detailsFragment)
            .commit();
        }
		
    }
}
