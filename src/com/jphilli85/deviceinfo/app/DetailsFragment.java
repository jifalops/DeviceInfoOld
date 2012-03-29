package com.jphilli85.deviceinfo.app;

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

import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.DeviceInfo.DetailsTextView;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.data.DeviceInfoContract.Group;
import com.jphilli85.deviceinfo.data.DeviceInfoContract.Subgroup;
import com.jphilli85.deviceinfo.unit.Cpu;
import com.jphilli85.deviceinfo.unit.Display;
import com.jphilli85.deviceinfo.unit.Graphics;
import com.jphilli85.deviceinfo.unit.Ram;
import com.jphilli85.deviceinfo.unit.Storage;
import com.jphilli85.deviceinfo.unit.Unit;

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
    	if (DeviceInfo.DEBUG) dumpResult(cursor);
    	if (!cursor.moveToFirst()) return;  
    	do {
    		// name, label
    		mSubgroups.put(cursor.getString(0), cursor.getString(1));
    	} while(cursor.moveToNext());
    	loadSubgroups();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       // Empty
    }
    
    
    private void loadSubgroups() {
    	for (String name : mSubgroups.keySet()) {
    		loadSubgroup(name);
    	}
    }
    
    private void loadSubgroup(final String name) {
    	Unit unit = null;
    	
    	if (name.equals(Subgroup.SUBGROUP_CPU)) {
    		unit = new Cpu();
    		((Cpu) unit).updateCpuStats();    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_DISPLAY)) {
    		unit = new Display(getActivity());    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_GRAPHICS)) {
    		unit = new Graphics();    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_RAM)) {
    		unit = new Ram();    		
    	}
    	else if (name.equals(Subgroup.SUBGROUP_STORAGE)) {
    		unit = new Storage();    		
    	}
    	
    	
    	if (unit != null) {
	    	Map<String, String> contents = unit.getContents();
			TextView tv = new DetailsTextView(getActivity(), name + "\n");
			for (String s : contents.keySet()) {
				tv.append(s + ": " + contents.get(s) + "\n");
			}
			mLayout.addView(tv);
    	}
    }
    
    private void dumpResult(Cursor c) {
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
		mLayout.addView(new DetailsTextView(getActivity(), rows));
    }
    
   
}
