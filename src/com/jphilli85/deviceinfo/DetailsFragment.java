package com.jphilli85.deviceinfo;

import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jphilli85.deviceinfo.data.DeviceInfo.Group;
import com.jphilli85.deviceinfo.data.DeviceInfo.Subgroup;

public class DetailsFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final int SUBGROUP_LOADER = 1;
	
	private LinearLayout mLayout;
	private Map<String, String> mSubgroups;
	

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
        return args != null ? args.getInt("index", 1) : 1;
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSubgroups = new HashMap<String, String>();
		getLoaderManager().initLoader(SUBGROUP_LOADER, null, this);		
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
	
	@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
        	//Subgroup.TABLE_NAME +"."+ Subgroup.COL_ID,
    		Subgroup.TABLE_NAME +"."+ Subgroup.COL_NAME, 
    		Subgroup.TABLE_NAME +"."+ Subgroup.COL_LABEL 
		};

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                Uri.parse(Group.CONTENT_URI.toString() 
                		+ "/" + String.valueOf(getShownIndex())), 
        		projection, 
                //Group.COL_NAME + "="	+ String.valueOf(getShownIndex())
                null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //mAdapter.swapCursor(cursor);
    	testResult(cursor);
    	while(cursor.moveToNext()) {
    		mSubgroups.put(cursor.getString(0), cursor.getString(1));
    	}
    	loadSubgroups();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       //mAdapter.swapCursor(null);
    }
    
    
    private void loadSubgroups() {
    	for (String name : mSubgroups.keySet()) {
    		loadSubgroup(name);
    	}
    }
    
    private void loadSubgroup(final String name) {
    	if (name.equals(Subgroup.SUBGROUP_AUDIO)) {
    		//TODO complete
    		
    	}
    }
    
    private void testResult(Cursor c) {
    	if (!c.moveToFirst()) return;    	

    	String rows = "";
    	for (int i = 0; i < c.getColumnCount(); ++i) {
			rows += c.getColumnName(i) + "|";
		}
    	rows += "\n";
    	do {    		
    		for (int i = 0; i < c.getColumnCount(); ++i) {
    			rows += c.getString(i) + "|";
    		}
    		rows += "\n";
    	} while (c.moveToNext()); 
    	
    	TextView tv = new TextView(getActivity());
		tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT));
		tv.setText(rows);
		mLayout.addView(tv);
    }
}
