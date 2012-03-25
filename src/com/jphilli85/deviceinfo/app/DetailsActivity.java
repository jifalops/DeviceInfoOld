package com.jphilli85.deviceinfo.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

public class DetailsActivity extends FragmentActivity {
	
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
			
			DetailsFragment detailsFragment = new DetailsFragment();
//					(DetailsFragment) 
//					getSupportFragmentManager().
//					findFragmentById(R.id.detailsFragment);
            detailsFragment.setArguments(getIntent().getExtras());  
            getSupportFragmentManager().beginTransaction()
            .add(android.R.id.content, detailsFragment)
            .commit();
        }
		
    }
}
