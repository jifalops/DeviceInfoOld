package com.jphilli85.deviceinfo.app;


import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.R.array;
import com.jphilli85.deviceinfo.R.id;
import com.jphilli85.deviceinfo.R.layout;

public class GroupListFragment extends SherlockListFragment {

	/** Interface for communication with container Activity. */
	public interface OnGroupSelectedListener {
		public void onGroupSelected(int index);
	}
	
	public static final String KEY_GROUP = GroupListFragment.class.getName() + ".GROUP";
	
	private OnGroupSelectedListener mOnGroupSelectedListener;	
    private boolean mDualPane;
    private int mCurrentGroup; 
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
        mAdapter = new ArrayAdapter<String>(getActivity(), 
        		R.layout.group_item, getResources().getStringArray(R.array.groups));
        setListAdapter(mAdapter);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if (savedInstanceState == null) mCurrentGroup = 0;
        else mCurrentGroup = savedInstanceState.getInt(KEY_GROUP, 0);        
        
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
    	mOnGroupSelectedListener.onGroupSelected(position);
    	showDetails(position);    	
    }
    
    public static int getGroup(int[] elements) {  
    	int[][] groups = DeviceInfo.getGroups();
    	for (int i = 0; i < groups.length; ++i) {    		
    		if (Arrays.equals(groups[i], elements)) return i;
    	}
    	return -1; 
    }
    
    public static int[] getElements(int group) {
    	int[][] groups = DeviceInfo.getGroups();
    	return groups[group];
    }

    
    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    private void showDetails(int index) {
        mCurrentGroup = index;
        int[] elements = getElements(index);

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            DetailsFragment detailsFragment = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.detailsFragment);
            if (detailsFragment == null || getGroup(detailsFragment.getElements()) != index) {
                // Make new fragment to show this selection.
                detailsFragment = DetailsFragment.newInstance(elements);

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
            intent.putExtra(DetailsFragment.KEY_ELEMENTS, elements);
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
