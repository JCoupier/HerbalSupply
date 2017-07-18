package com.example.android.herbalsupply;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.herbalsupply.data.HerbContract.PlantEntry;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 */
public class StockActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Tag for the log messages */
    public static final String LOG_TAG = StockActivity.class.getSimpleName();

    /** Identifier for the plant data loader */
    private static final int PLANT_LOADER = 0;

    // The recyclerView
    private RecyclerView mRecyclerView;

    // The cursorAdapter
    private HerbCursorAdapter mCursorAdapter;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StockActivity.this, EditorActivity.class);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // Lookup the recyclerView in activity layout
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // Set layout manager to position the items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Find the emptyStateTextView
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        // Create adapter with null cursor and set the listener creating the intent
        mCursorAdapter = new HerbCursorAdapter(this, null, new HerbCursorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                Intent intent = new Intent(StockActivity.this, EditorActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(PlantEntry.CONTENT_URI, id);
                intent.setData(currentProductUri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // Attach the adapter to the recyclerView to populate items
        mRecyclerView.setAdapter(mCursorAdapter);

        // Kick off the loader
        getLoaderManager().initLoader(PLANT_LOADER, null, this);
    }

    // Method used in the HerbCursorAdapter, attached to the sell Button
    public void onSellButtonClicked(long id, int quantity) {
        Uri currentPlantUri = ContentUris.withAppendedId(PlantEntry.CONTENT_URI, id);
        quantity--;
        ContentValues values = new ContentValues();
        values.put(PlantEntry.COLUMN_PLANT_QUANTITY, quantity);
        int rowsAffected = getContentResolver().update(currentPlantUri, values, null, null);
        Log.v(LOG_TAG, rowsAffected + "rows updated from plant database");
    }

    /**
     * Helper method to insert hardcoded plant data into the database. For debugging purposes only.
     */
    private void insertPlant() {

        // Create a ContentValues object where column names are the keys,
        // and a dummy plant attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PlantEntry.COLUMN_PLANT_NAME, getString(R.string.dummy_plant_name));
        values.put(PlantEntry.COLUMN_PLANT_PICTURE, getString(R.string.dummy_plant_picture_uri));
        values.put(PlantEntry.COLUMN_PLANT_PRICE, getString(R.string.dummy_plant_price));
        values.put(PlantEntry.COLUMN_PLANT_QUANTITY, 1);
        values.put(PlantEntry.COLUMN_PLANT_SUPPLIER_INFO, getString(R.string.dummy_plant_supplier_info));
        values.put(PlantEntry.COLUMN_PLANT_SUPPLIER_MAIL, getString(R.string.dummy_plant_supplier_mail));

        // Insert the Dummy data with the ContentValues into the database
        Uri newUri = getContentResolver().insert(PlantEntry.CONTENT_URI, values);
        Log.v(LOG_TAG, "Uri of new product: " + newUri);
    }

    private void deleteAllPlant() {
        int rowsDeleted = getContentResolver().delete(PlantEntry.CONTENT_URI, null, null);
        Log.v(LOG_TAG, rowsDeleted + " rows deleted from plant database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_stock.xml.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPlant();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPlant();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                PlantEntry._ID,
                PlantEntry.COLUMN_PLANT_NAME,
                PlantEntry.COLUMN_PLANT_PICTURE,
                PlantEntry.COLUMN_PLANT_QUANTITY,
                PlantEntry.COLUMN_PLANT_PRICE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                PlantEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
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
