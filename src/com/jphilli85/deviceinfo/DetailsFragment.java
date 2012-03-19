package com.jphilli85.deviceinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    	Bundle args = getArguments();
        return args != null ? args.getInt("index", 0) : 0;
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
			//container = (ViewGroup) getActivity().findViewById(R.id.detailsFragmentWrapper);
		//super.onCreateView(inflater, container, savedInstanceState);		
		View v = inflater.inflate(R.layout.details, container, false);
		mLayout = (LinearLayout) v.findViewById(R.id.detailsLayout);
		
		TextView tv = new TextView(getActivity());
		tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT));
		tv.setText(String.valueOf(getShownIndex()));
		mLayout.addView(tv);
		
		return v; 		
	}
}
