package com.example.android.herbalsupply;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.herbalsupply.data.HerbContract.HeEntry;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 */
public class HuileEssFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the He data loader */
    private static final int HE_LOADER = 2;

    private static final int TABLE_ID = 2;

    // The recyclerView
    private RecyclerView mRecyclerView;

    // The cursorAdapter
    private HerbCursorAdapter mCursorAdapter;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    public HuileEssFragment(){
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Kick off the loader
        getLoaderManager().initLoader(HE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.herbal_list, container, false);

        // Lookup the recyclerView in activity layout
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // Set layout manager to position the items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Find the emptyStateTextView
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);

        // Create adapter with null cursor and set the listener creating the intent
        mCursorAdapter = new HerbCursorAdapter(getContext(), null, new HerbCursorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(HeEntry.CONTENT_URI, id);
                intent.setData(currentProductUri);
                intent.putExtra("TableType", TABLE_ID);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                intent.putExtra("TableType", TABLE_ID);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // Attach the adapter to the recyclerView to populate items
        mRecyclerView.setAdapter(mCursorAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                HeEntry._ID,
                HeEntry.COLUMN_HE_NAME,
                HeEntry.COLUMN_HE_PICTURE,
                HeEntry.COLUMN_HE_QUANTITY_GRAM,
                HeEntry.COLUMN_HE_QUANTITY_ML,
                HeEntry.COLUMN_HE_AROMAZONE_URL};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(),   // Parent activity context
                HeEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                HeEntry.COLUMN_HE_NAME); // Alpha sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()) {
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        } else {
            mEmptyStateTextView.setVisibility(View.GONE);
        }
        // / Update {@link HerbCursorAdapter} with this new cursor containing updated plant data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}


