package com.jphilli85.deviceinfo.app;


import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jphilli85.deviceinfo.R;

import java.util.Arrays;

public class GroupListFragment extends ListFragment {

	/** Interface for communication with container Activity. */
	public interface Callback {
		void showDetails(int group);
		boolean isDualPane();
	}
	
	
	
	private Callback mCallback;	
//    private boolean mDualPane;
//    private int mCurrentGroup; 
	private ListAdapter mAdapter;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	mCallback = (Callback) activity;
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
        
//        if (savedInstanceState == null) mCurrentGroup = 0;
//        else mCurrentGroup = savedInstanceState.getInt(KEY_GROUP, 0);        
        
       
        
        if (((Callback) getActivity()).isDualPane()) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
//            showDetails(mCurrentGroup);           
        }   
//        setRetainInstance(true);
    }
    


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	mCallback.showDetails(position);	
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
