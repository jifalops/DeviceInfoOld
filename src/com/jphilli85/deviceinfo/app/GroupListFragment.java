package com.jphilli85.deviceinfo.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.jphilli85.deviceinfo.R;

public class GroupListFragment extends SherlockListFragment {

	/** Interface for communication with container Activity. */
	public interface OnGroupSelectedListener {
		public void onGroupSelected(int id);
	}
	
	public static final String KEY_GROUP = GroupListFragment.class.getName() + ".GROUP";
	
	public static final int[] GROUP_OVERVIEW;
	public static final int[] GROUP_CPU;
	public static final int[] GROUP_MEMORY;
	public static final int[] GROUP_AUDIO_VIDEO;
	public static final int[] GROUP_BATTERY_SENSORS;
	public static final int[] GROUP_LOCATION;
	public static final int[] GROUP_CONNECTIONS;
	public static final int[] GROUP_SYSTEM;
	
	public static final int[][] GROUPS;
	
	static {
		GROUP_OVERVIEW = new int[] { DetailsFragment.ELEMENT_OVERVIEW };
		GROUP_CPU = new int[] { DetailsFragment.ELEMENT_CPU };
		GROUP_MEMORY = new int[] { DetailsFragment.ELEMENT_RAM, DetailsFragment.ELEMENT_STORAGE };
		GROUP_AUDIO_VIDEO = new int[] { DetailsFragment.ELEMENT_DISPLAY, DetailsFragment.ELEMENT_GRAPHICS, 
				DetailsFragment.ELEMENT_CAMERA, DetailsFragment.ELEMENT_AUDIO };
		GROUP_BATTERY_SENSORS = new int[] { DetailsFragment.ELEMENT_BATTERY, DetailsFragment.ELEMENT_SENSORS };
		GROUP_LOCATION = new int[] { DetailsFragment.ELEMENT_LOCATION };
		GROUP_CONNECTIONS = new int[] { DetailsFragment.ELEMENT_NETWORK, DetailsFragment.ELEMENT_CELLULAR,
				DetailsFragment.ELEMENT_WIFI, DetailsFragment.ELEMENT_BLUETOOTH };
		GROUP_SYSTEM = new int[] { DetailsFragment.ELEMENT_UPTIME, DetailsFragment.ELEMENT_PLATFORM,
				DetailsFragment.ELEMENT_IDENTIFIERS, DetailsFragment.ELEMENT_FEATURES, 
				DetailsFragment.ELEMENT_PROPERTIES,	DetailsFragment.ELEMENT_KEYS };
		
		GROUPS = new int[][] { GROUP_OVERVIEW, GROUP_CPU, GROUP_MEMORY, GROUP_AUDIO_VIDEO,
				GROUP_BATTERY_SENSORS, GROUP_LOCATION, GROUP_CONNECTIONS, GROUP_SYSTEM };		
	}	
	
	private OnGroupSelectedListener mOnGroupSelectedListener;	

    private boolean mDualPane;
    int mCurrentGroup;
    
	private ListAdapter mAdapter;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	mOnGroupSelectedListener = (OnGroupSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnGroupSelectedListener");
        }
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context c = getActivity();
        String[] groups = new String[] {
        	c.getString(R.string.group_label_overview), c.getString(R.string.group_label_cpu),
        	c.getString(R.string.group_label_memory), c.getString(R.string.group_label_visual_audio),
        	c.getString(R.string.group_label_battery_sensors), c.getString(R.string.group_label_location),
        	c.getString(R.string.group_label_connections), c.getString(R.string.group_label_system)
        };
        
        mAdapter = new ArrayAdapter<String>(getActivity(), 
        		R.layout.group_listitem, R.id.itemLabel, groups);
        setListAdapter(mAdapter);
        setHasOptionsMenu(true);
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if (savedInstanceState == null) mCurrentGroup = 0;
        else mCurrentGroup = savedInstanceState.getInt(KEY_GROUP);        
        
        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        ViewGroup detailsWrapper = (ViewGroup) 
        		getActivity().findViewById(R.id.detailsFragmentWrapper);
        mDualPane = detailsWrapper != null && detailsWrapper.getVisibility() == View.VISIBLE;
        
        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurrentGroup);
        }   
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_GROUP, mCurrentGroup);
    }

    
    
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        String projection[] = { DeviceInfo.Group.COL_ID };
//        Cursor cursor = getActivity().getContentResolver().query(
//                Uri.withAppendedPath(DeviceInfo.Group.CONTENT_URI,
//                        String.valueOf(id)), projection, null, null, null);
//        if (cursor.moveToFirst()) {
//            mOnGroupSelectedListener.onGroupSelected(cursor.getInt(0));
//            Intent detailsIntent = new Intent();
//    		detailsIntent.setAction(Intent.ACTION_VIEW)
//    		.setData(Uri.parse(DeviceInfo.AUTHORITY))
//    		.putExtra(EXTRA_DETAILS_ARRAY, new String[] { 
//    			DeviceInfo.Group.CONTENT_URI + "/" + id });
//        }
//        cursor.close();
//        l.setItemChecked(position, true); 
    	mOnGroupSelectedListener.onGroupSelected(position);
    	showDetails(position);    	
    }
    
    
    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurrentGroup = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            DetailsFragment detailsFragment = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.detailsFragment);
            if (detailsFragment == null || detailsFragment.getGroup() != index) {
                // Make new fragment to show this selection.
                detailsFragment = DetailsFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                getFragmentManager().beginTransaction()
                .replace(R.id.detailsFragmentWrapper, detailsFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra(KEY_GROUP, index);
            startActivity(intent);
        }
    }
	
	
	
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        String[] projection = { DeviceInfoContract.Group.COL_ID, DeviceInfoContract.Group.COL_LABEL };
//
//        CursorLoader cursorLoader = new CursorLoader(getActivity(),
//                DeviceInfoContract.Group.CONTENT_URI, projection, null, null, null);
//        return cursorLoader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        mAdapter.swapCursor(cursor);
////        cursor.close();
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        mAdapter.swapCursor(null);
//    }
}
