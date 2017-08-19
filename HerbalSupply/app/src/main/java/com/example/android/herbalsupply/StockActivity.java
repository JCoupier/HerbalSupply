package com.example.android.herbalsupply;

import android.content.ContentValues;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.herbalsupply.data.HerbContract.HydrolatEntry;
import com.example.android.herbalsupply.data.HerbContract.HvEntry;
import com.example.android.herbalsupply.data.HerbContract.HeEntry;
import com.example.android.herbalsupply.data.HerbContract.ExtrEntry;
import com.example.android.herbalsupply.data.HerbContract.PoudrEntry;
import com.example.android.herbalsupply.data.HerbContract.ActifEntry;
import com.example.android.herbalsupply.data.HerbContract.DiversEntry;
import com.example.android.herbalsupply.data.HerbContract.ContenEntry;
/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 */
public class StockActivity extends AppCompatActivity {

    /** Tag for the log messages */
    public static final String LOG_TAG = StockActivity.class.getSimpleName();

    // The viewpager
    private ViewPager mViewPager;

    // Current fragment displayed
    private int mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_stock);

        // Find the view pager that will allow the user to swipe between fragments
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        HerbalFragmentPagerAdapter adapter = new HerbalFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        mViewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Helper method to insert hardcoded plant data into the database. For debugging purposes only.
     */
    private void insertPlant() {
        // Get current Fragment
        mCurrentFragment = mViewPager.getCurrentItem();
        ContentValues values = new ContentValues();
        switch (mCurrentFragment){
            case 0:
                // Create a ContentValues object where column names are the keys,
                // and a dummy plant attributes are the values.
                values.put(HydrolatEntry.COLUMN_HYDRO_NAME, getString(R.string.dummy_plant_name));
                values.put(HydrolatEntry.COLUMN_HYDRO_PICTURE, getString(R.string.dummy_plant_picture_uri));
                values.put(HydrolatEntry.COLUMN_HYDRO_DENSITY, getString(R.string.dummy_plant_density));
                values.put(HydrolatEntry.COLUMN_HYDRO_QUANTITY_GRAM, getString(R.string.dummy_plant_price));
                values.put(HydrolatEntry.COLUMN_HYDRO_QUANTITY_ML, getString(R.string.dummy_plant_price));
                values.put(HydrolatEntry.COLUMN_HYDRO_AROMAZONE_URL, getString(R.string.dummy_website_url));

                // Insert the Dummy data with the ContentValues into the database
                Uri hydrolatUri = getContentResolver().insert(HydrolatEntry.CONTENT_URI, values);
                Log.v(LOG_TAG, "Uri of new product: " + hydrolatUri);
                break;
            case 1:
                // Create a ContentValues object where column names are the keys,
                // and a dummy plant attributes are the values.
                values.put(HvEntry.COLUMN_HV_NAME, getString(R.string.dummy_plant_name));
                values.put(HvEntry.COLUMN_HV_PICTURE, getString(R.string.dummy_plant_picture_uri));
                values.put(HvEntry.COLUMN_HV_DENSITY, getString(R.string.dummy_plant_density));
                values.put(HvEntry.COLUMN_HV_QUANTITY_GRAM, getString(R.string.dummy_plant_price));
                values.put(HvEntry.COLUMN_HV_QUANTITY_ML, getString(R.string.dummy_plant_price));
                values.put(HvEntry.COLUMN_HV_AROMAZONE_URL, getString(R.string.dummy_website_url));

                // Insert the Dummy data with the ContentValues into the database
                Uri hvUri = getContentResolver().insert(HvEntry.CONTENT_URI, values);
                Log.v(LOG_TAG, "Uri of new product: " + hvUri);
                break;
            case 2:
                // Create a ContentValues object where column names are the keys,
                // and a dummy plant attributes are the values.
                values.put(HeEntry.COLUMN_HE_NAME, getString(R.string.dummy_plant_name));
                values.put(HeEntry.COLUMN_HE_PICTURE, getString(R.string.dummy_plant_picture_uri));
                values.put(HeEntry.COLUMN_HE_DENSITY, getString(R.string.dummy_plant_density));
                values.put(HeEntry.COLUMN_HE_QUANTITY_GRAM, getString(R.string.dummy_plant_price));
                values.put(HeEntry.COLUMN_HE_QUANTITY_ML, getString(R.string.dummy_plant_price));
                values.put(HeEntry.COLUMN_HE_AROMAZONE_URL, getString(R.string.dummy_website_url));

                // Insert the Dummy data with the ContentValues into the database
                Uri HeUri = getContentResolver().insert(HeEntry.CONTENT_URI, values);
                Log.v(LOG_TAG, "Uri of new product: " + HeUri);
                break;
            case 3:
                // Create a ContentValues object where column names are the keys,
                // and a dummy plant attributes are the values.
                values.put(ExtrEntry.COLUMN_EXTR_NAME, getString(R.string.dummy_plant_name));
                values.put(ExtrEntry.COLUMN_EXTR_PICTURE, getString(R.string.dummy_plant_picture_uri));
                values.put(ExtrEntry.COLUMN_EXTR_DENSITY, getString(R.string.dummy_plant_density));
                values.put(ExtrEntry.COLUMN_EXTR_QUANTITY_GRAM, getString(R.string.dummy_plant_price));
                values.put(ExtrEntry.COLUMN_EXTR_QUANTITY_ML, getString(R.string.dummy_plant_price));
                values.put(ExtrEntry.COLUMN_EXTR_AROMAZONE_URL, getString(R.string.dummy_website_url));

                // Insert the Dummy data with the ContentValues into the database
                Uri ExtrUri = getContentResolver().insert(ExtrEntry.CONTENT_URI, values);
                Log.v(LOG_TAG, "Uri of new product: " + ExtrUri);
                break;
            case 4:
                // Create a ContentValues object where column names are the keys,
                // and a dummy plant attributes are the values.
                values.put(PoudrEntry.COLUMN_POUDR_NAME, getString(R.string.dummy_plant_name));
                values.put(PoudrEntry.COLUMN_POUDR_PICTURE, getString(R.string.dummy_plant_picture_uri));
                values.put(PoudrEntry.COLUMN_POUDR_DENSITY, getString(R.string.dummy_plant_density));
                values.put(PoudrEntry.COLUMN_POUDR_QUANTITY_GRAM, getString(R.string.dummy_plant_price));
                values.put(PoudrEntry.COLUMN_POUDR_QUANTITY_ML, getString(R.string.dummy_plant_price));
                values.put(PoudrEntry.COLUMN_POUDR_AROMAZONE_URL, getString(R.string.dummy_website_url));

                // Insert the Dummy data with the ContentValues into the database
                Uri PoudrUri = getContentResolver().insert(PoudrEntry.CONTENT_URI, values);
                Log.v(LOG_TAG, "Uri of new product: " + PoudrUri);
                break;
            case 5:
                // Create a ContentValues object where column names are the keys,
                // and a dummy plant attributes are the values.
                values.put(ActifEntry.COLUMN_ACTIF_NAME, getString(R.string.dummy_plant_name));
                values.put(ActifEntry.COLUMN_ACTIF_PICTURE, getString(R.string.dummy_plant_picture_uri));
                values.put(ActifEntry.COLUMN_ACTIF_DENSITY, getString(R.string.dummy_plant_density));
                values.put(ActifEntry.COLUMN_ACTIF_QUANTITY_GRAM, getString(R.string.dummy_plant_price));
                values.put(ActifEntry.COLUMN_ACTIF_QUANTITY_ML, getString(R.string.dummy_plant_price));
                values.put(ActifEntry.COLUMN_ACTIF_AROMAZONE_URL, getString(R.string.dummy_website_url));

                // Insert the Dummy data with the ContentValues into the database
                Uri ActifUri = getContentResolver().insert(ActifEntry.CONTENT_URI, values);
                Log.v(LOG_TAG, "Uri of new product: " + ActifUri);
                break;
            case 6:
                // Create a ContentValues object where column names are the keys,
                // and a dummy plant attributes are the values.
                values.put(DiversEntry.COLUMN_DIVERS_NAME, getString(R.string.dummy_plant_name));
                values.put(DiversEntry.COLUMN_DIVERS_PICTURE, getString(R.string.dummy_plant_picture_uri));
                values.put(DiversEntry.COLUMN_DIVERS_DENSITY, getString(R.string.dummy_plant_density));
                values.put(DiversEntry.COLUMN_DIVERS_QUANTITY_GRAM, getString(R.string.dummy_plant_price));
                values.put(DiversEntry.COLUMN_DIVERS_QUANTITY_ML, getString(R.string.dummy_plant_price));
                values.put(DiversEntry.COLUMN_DIVERS_AROMAZONE_URL, getString(R.string.dummy_website_url));

                // Insert the Dummy data with the ContentValues into the database
                Uri DiversUri = getContentResolver().insert(DiversEntry.CONTENT_URI, values);
                Log.v(LOG_TAG, "Uri of new product: " + DiversUri);
                break;
            case 7:
                // Create a ContentValues object where column names are the keys,
                // and a dummy plant attributes are the values.
                values.put(ContenEntry.COLUMN_CONTEN_NAME, getString(R.string.dummy_plant_name));
                values.put(ContenEntry.COLUMN_CONTEN_PICTURE, getString(R.string.dummy_plant_picture_uri));
                values.put(ContenEntry.COLUMN_CONTEN_QUANTITY_TOTAL, 0);
                values.put(ContenEntry.COLUMN_CONTEN_QUANTITY_DISPO, 0);
                values.put(ContenEntry.COLUMN_CONTEN_AROMAZONE_URL, getString(R.string.dummy_website_url));

                // Insert the Dummy data with the ContentValues into the database
                Uri ContenUri = getContentResolver().insert(ContenEntry.CONTENT_URI, values);
                Log.v(LOG_TAG, "Uri of new product: " + ContenUri);
                break;
        }
    }

    private void deleteAllPlant() {
        // Get current Fragment
        mCurrentFragment = mViewPager.getCurrentItem();
        switch (mCurrentFragment) {
            case 0:
                int rowsHydrolatDeleted = getContentResolver().delete(HydrolatEntry.CONTENT_URI, null, null);
                Log.v(LOG_TAG, rowsHydrolatDeleted + " rows deleted from Hydrolat database");
                break;
            case 1:
                int rowsHvDeleted = getContentResolver().delete(HvEntry.CONTENT_URI, null, null);
                Log.v(LOG_TAG, rowsHvDeleted + " rows deleted from Hv database");
                break;
            case 2:
                int rowsHeDeleted = getContentResolver().delete(HeEntry.CONTENT_URI, null, null);
                Log.v(LOG_TAG, rowsHeDeleted + " rows deleted from He database");
                break;
            case 3:
                int rowsExtrDeleted = getContentResolver().delete(ExtrEntry.CONTENT_URI, null, null);
                Log.v(LOG_TAG, rowsExtrDeleted + " rows deleted from Extr database");
                break;
            case 4:
                int rowsPoudrDeleted = getContentResolver().delete(PoudrEntry.CONTENT_URI, null, null);
                Log.v(LOG_TAG, rowsPoudrDeleted + " rows deleted from Poudr database");
                break;
            case 5:
                int rowsActifDeleted = getContentResolver().delete(ActifEntry.CONTENT_URI, null, null);
                Log.v(LOG_TAG, rowsActifDeleted + " rows deleted from Actif database");
                break;
            case 6:
                int rowsDiversDeleted = getContentResolver().delete(DiversEntry.CONTENT_URI, null, null);
                Log.v(LOG_TAG, rowsDiversDeleted + " rows deleted from Divers database");
                break;
            case 7:
                int rowsContenDeleted = getContentResolver().delete(ContenEntry.CONTENT_URI, null, null);
                Log.v(LOG_TAG, rowsContenDeleted + " rows deleted from Conten database");
                break;
        }
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
}
