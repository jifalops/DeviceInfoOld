package com.jphilli85.deviceinfo;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.data.DeviceInfoDatabase;

public class GroupListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	
	/** Interface for communication with container Activity. */
	public interface OnGroupSelectedListener {
		public void onGroupSelected(int id);
	}
	
	private static final int GROUP_LIST_LOADER = 1;	
	private OnGroupSelectedListener mGroupSelectedListener;	
    private SimpleCursorAdapter mAdapter;
	
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	mGroupSelectedListener = (OnGroupSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnGroupSelectedListener");
        }
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] uiBindFrom = { DeviceInfoDatabase.COL_LABEL };
        int[] uiBindTo = { R.id.item_label };

        getLoaderManager().initLoader(GROUP_LIST_LOADER, null, this);

        mAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.grouplist_item,
                null, uiBindFrom, uiBindTo, 0); //CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER

        setListAdapter(mAdapter);
        setHasOptionsMenu(true);
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
    
    
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String projection[] = { DeviceInfoDatabase.COL_NAME };
        Cursor cursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(DeviceInfoProvider.CONTENT_URI,
                        String.valueOf(id)), projection, null, null, null);
        if (cursor.moveToFirst()) {
            String tutorialUrl = cursor.getString(0);
            tutSelectedListener.onTutSelected(tutorialUrl);
        }
        cursor.close();
        l.setItemChecked(position, true); 
    }
	
	
	

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}
