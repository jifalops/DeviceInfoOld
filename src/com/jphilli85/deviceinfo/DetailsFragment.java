package com.jphilli85.deviceinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DetailsFragment extends Fragment {
	private LinearLayout mLayout;

	/**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailsFragment newInstance(int index) {
        DetailsFragment f = new DetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }
	
    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) return null;
		//super.onCreateView(inflater, container, savedInstanceState);		
		View v = inflater.inflate(R.layout.details, container);
		mLayout = (LinearLayout) v.findViewById(R.id.detailsLayout);
		// TODO stuff with mLayout ... 
		return v; 		
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
	}
}
