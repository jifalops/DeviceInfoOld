package com.jphilli85.deviceinfo;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import com.jphilli85.deviceinfo.data.DeviceInfo;

public class GroupListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
// TODO current task: handling sending data to the DetailsFragment.
	// will need to replace the default implementation of showDetails(int).
	/** Interface for communication with container Activity. */
	public interface OnGroupSelectedListener {
		public void onGroupSelected(int id);
	}
	
	private static final int GROUP_LIST_LOADER = 1;
	public static final String EXTRA_DETAILS_ARRAY = 
			"com.jphilli85.deviceinfo.extra.DETAILS_ARRAY";
	
	private OnGroupSelectedListener mOnGroupSelectedListener;	
    private SimpleCursorAdapter mAdapter;
    
    private boolean mDualPane;
    int mCurCheckPosition = 0;
    
	
    
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

        String[] uiBindFrom = { DeviceInfo.Group.COL_LABEL };
        int[] uiBindTo = { R.id.itemLabel };

        getLoaderManager().initLoader(GROUP_LIST_LOADER, null, this);

        mAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.group_listitem,
                null, uiBindFrom, uiBindTo, 0); //CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER

        setListAdapter(mAdapter);
        setHasOptionsMenu(true);
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
        
        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.detailsFragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        
        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }   
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    
    
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String projection[] = { DeviceInfo.Group.COL_ID };
        Cursor cursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(DeviceInfo.Group.CONTENT_URI,
                        String.valueOf(id)), projection, null, null, null);
        if (cursor.moveToFirst()) {
            mOnGroupSelectedListener.onGroupSelected(cursor.getInt(0));
//            Intent detailsIntent = new Intent();
//    		detailsIntent.setAction(Intent.ACTION_VIEW)
//    		.setData(Uri.parse(DeviceInfo.AUTHORITY))
//    		.putExtra(EXTRA_DETAILS_ARRAY, new String[] { 
//    			DeviceInfo.Group.CONTENT_URI + "/" + id });
        }
        cursor.close();
        l.setItemChecked(position, true); 
    }
    
    
    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.detailsFragment);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailsFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.detailsFragment, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }
	
	
	

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { DeviceInfo.Group.COL_ID, DeviceInfo.Group.COL_LABEL };

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                DeviceInfo.Group.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}