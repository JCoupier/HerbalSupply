package com.example.android.herbalsupply.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.herbalsupply.data.HerbContract.PlantEntry;

import static android.R.attr.name;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 *
 * {@link ContentProvider} for Herbal Supply app.
 */
public class HerbProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = HerbProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the plants table */
    private static final int PLANTS = 100;

    /** URI matcher code for the content URI for a single plant in the plants table */
    private static final int PLANT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.herbalsupply/plants" will map to the
        // integer code {@link #PLANTS}. This URI is used to provide access to MULTIPLE rows
        // of the plants table.
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_PLANTS, PLANTS);

        // The content URI of the form "content://com.example.android.herbalsupply/plants/#" will map to the
        // integer code {@link #PLANT_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.herbalsupply/plants/3" matches, but
        // "content://com.example.android.herbalsupply/plants" (without a number at the end) doesn't match.
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_PLANTS + "/#", PLANT_ID);
    }

    /** Database helper object */
    private HerbDbHelper mDbHelper;

    // The Database
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        mDbHelper = new HerbDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        mDb = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANTS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = mDb.query(PlantEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PLANT_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PlantEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor =mDb.query(PlantEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANTS:
                return insertPlant(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a plant into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPlant(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(PlantEntry.COLUMN_PLANT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Plant requires a name");
        }

        String pictureUri = values.getAsString(PlantEntry.COLUMN_PLANT_PICTURE);
        if (pictureUri == null) {
            throw new IllegalArgumentException("Plant requires a picture");
        }

        String price = values.getAsString(PlantEntry.COLUMN_PLANT_PRICE);
        if (price == null ) {
            throw new IllegalArgumentException("Plant requires a price");
        }

        String supplierInfo = values.getAsString(PlantEntry.COLUMN_PLANT_SUPPLIER_INFO);
        if (supplierInfo == null) {
            throw new IllegalArgumentException("Plant requires supplier info");
        }

        String supplierMail = values.getAsString(PlantEntry.COLUMN_PLANT_SUPPLIER_MAIL);
        if (supplierMail == null) {
            throw new IllegalArgumentException("Plant requires a supplier mail");
        }

        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Insert the new plant with the given values
        long id = mDb.insert(PlantEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the plant content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Close the instance of the Database
        mDb.close();

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANTS:
                return updatePlant(uri, contentValues, selection, selectionArgs);
            case PLANT_ID:
                // For the PLANT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PlantEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePlant(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update plants in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more plants).
     * Return the number of rows that were successfully updated.
     */
    private int updatePlant(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PlantEntry#COLUMN_PLANT_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PlantEntry.COLUMN_PLANT_NAME)) {
            String name = values.getAsString(PlantEntry.COLUMN_PLANT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Plant requires a name");
            }
        }

        // If the {@link PlantEntry#COLUMN_PLANT_PICTURE} key is present,
        // check that the picture uri is not null.
        if (values.containsKey(PlantEntry.COLUMN_PLANT_PICTURE)) {
            String pictureUri = values.getAsString(PlantEntry.COLUMN_PLANT_PICTURE);
            if (pictureUri == null) {
                throw new IllegalArgumentException("Plant requires a picture");
            }
        }

        // If the {@link PlantEntry#COLUMN_PLANT_PRICE} key is present,
        // check that the price value is not null.
        if (values.containsKey(PlantEntry.COLUMN_PLANT_PRICE)) {
            String price = values.getAsString(PlantEntry.COLUMN_PLANT_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Product requires a price");
            }
        }

        // If the {@link PlantEntry#COLUMN_PLANT_SUPPLIER_INFO} key is present,
        // check that the supplier info value is not null.
        if (values.containsKey(PlantEntry.COLUMN_PLANT_SUPPLIER_INFO)) {
            String supplierInfo = values.getAsString(PlantEntry.COLUMN_PLANT_SUPPLIER_INFO);
            if (supplierInfo == null) {
                throw new IllegalArgumentException("Product requires supplier info");
            }
        }

        // If the {@link PlantEntry#COLUMN_PLANT_SUPPLIER_MAIL} key is present,
        // check that the supplier mail value is not null.
        if (values.containsKey(PlantEntry.COLUMN_PLANT_SUPPLIER_MAIL)) {
            String supplierMail = values.getAsString(PlantEntry.COLUMN_PLANT_SUPPLIER_MAIL);
            if (supplierMail == null) {
                throw new IllegalArgumentException("Product requires a supplier mail");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        mDb = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mDb.update(PlantEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Close the instance of the Database
        mDb.close();

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = mDb.delete(PlantEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PLANT_ID:
                // Delete a single row given by the ID in the URI
                selection = PlantEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = mDb.delete(PlantEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Close the instance of the Database
        mDb.close();

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLANTS:
                return PlantEntry.CONTENT_LIST_TYPE;
            case PLANT_ID:
                return PlantEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
