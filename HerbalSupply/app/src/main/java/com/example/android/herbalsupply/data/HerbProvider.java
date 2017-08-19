package com.example.android.herbalsupply.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

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
 *
 * {@link ContentProvider} for Herbal Supply app.
 */
public class HerbProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = HerbProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the plants table */
    private static final int HYDROLATS = 100;

    /** URI matcher code for the content URI for a single plant in the plants table */
    private static final int HYDROLAT_ID = 101;

    /** URI matcher code for the content URI for the plants table */
    private static final int HVS = 200;

    /** URI matcher code for the content URI for a single plant in the plants table */
    private static final int HV_ID = 201;

    /** URI matcher code for the content URI for the plants table */
    private static final int HES = 300;

    /** URI matcher code for the content URI for a single plant in the plants table */
    private static final int HE_ID = 301;

    /** URI matcher code for the content URI for the plants table */
    private static final int EXTRAITS = 400;

    /** URI matcher code for the content URI for a single plant in the plants table */
    private static final int EXTRAIT_ID = 401;

    /** URI matcher code for the content URI for the plants table */
    private static final int POUDRES = 500;

    /** URI matcher code for the content URI for a single plant in the plants table */
    private static final int POUDRE_ID = 501;

    /** URI matcher code for the content URI for the plants table */
    private static final int ACTIFS = 600;

    /** URI matcher code for the content URI for a single plant in the plants table */
    private static final int ACTIF_ID = 601;

    /** URI matcher code for the content URI for the plants table */
    private static final int DIVERS = 700;

    /** URI matcher code for the content URI for a single plant in the plants table */
    private static final int DIVER_ID = 701;

    /** URI matcher code for the content URI for the plants table */
    private static final int CONTENANTS = 800;

    /** URI matcher code for the content URI for a single plant in the plants table */
    private static final int CONTENANT_ID = 801;

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
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_HYDROLATS, HYDROLATS);

        // The content URI of the form "content://com.example.android.herbalsupply/plants/#" will map to the
        // integer code {@link #PLANT_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.herbalsupply/plants/3" matches, but
        // "content://com.example.android.herbalsupply/plants" (without a number at the end) doesn't match.
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_HYDROLATS + "/#", HYDROLAT_ID);

        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_HVS, HVS);
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_HVS + "/#", HV_ID);

        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_HES, HES);
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_HES + "/#", HE_ID);

        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_EXTRAITS, EXTRAITS);
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_EXTRAITS + "/#", EXTRAIT_ID);

        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_POUDRES, POUDRES);
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_POUDRES + "/#", POUDRE_ID);

        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_ACTIFS, ACTIFS);
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_ACTIFS + "/#", ACTIF_ID);

        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_DIVERS, DIVERS);
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_DIVERS + "/#", DIVER_ID);

        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_CONTENANTS,CONTENANTS);
        sUriMatcher.addURI(HerbContract.CONTENT_AUTHORITY, HerbContract.PATH_CONTENANTS + "/#", CONTENANT_ID);
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
            case HYDROLATS:
                // For the HYDROLATS code, query the hydrolats table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the hydrolats table.
                cursor = mDb.query(HydrolatEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case HYDROLAT_ID:
                // For the HYDROLAT_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = HydrolatEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the hydrolats table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor =mDb.query(HydrolatEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case HVS:
                cursor = mDb.query(HvEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case HV_ID:
                selection = HvEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor =mDb.query(HvEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case HES:
                cursor = mDb.query(HeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case HE_ID:
                selection = HeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor =mDb.query(HeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EXTRAITS:
                cursor = mDb.query(ExtrEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EXTRAIT_ID:
                selection = ExtrEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor =mDb.query(ExtrEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case POUDRES:
                cursor = mDb.query(PoudrEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case POUDRE_ID:
                selection = PoudrEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor =mDb.query(PoudrEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ACTIFS:
                cursor = mDb.query(ActifEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ACTIF_ID:
                selection = ActifEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor =mDb.query(ActifEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case DIVERS:
                cursor = mDb.query(DiversEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case DIVER_ID:
                selection = DiversEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor =mDb.query(DiversEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CONTENANTS:
                cursor = mDb.query(ContenEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CONTENANT_ID:
                selection = ContenEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor =mDb.query(ContenEntry.TABLE_NAME, projection, selection, selectionArgs,
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
            case HYDROLATS:
                return insertHydrolat(uri, contentValues);
            case HVS:
                return insertHv(uri, contentValues);
            case HES:
                return insertHe(uri, contentValues);
            case EXTRAITS:
                return insertExtrait(uri, contentValues);
            case POUDRES:
                return insertPoudre(uri, contentValues);
            case ACTIFS:
                return insertActif(uri, contentValues);
            case DIVERS:
                return insertDiver(uri, contentValues);
            case CONTENANTS:
                return insertContenant(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a hydrolat into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertHydrolat(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(HydrolatEntry.COLUMN_HYDRO_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Hydrolat requires a name");
        }

        String picture = values.getAsString(HydrolatEntry.COLUMN_HYDRO_PICTURE);
        if (picture == null) {
            throw new IllegalArgumentException("Hydrolat requires a picture");
        }

        String density = values.getAsString(HydrolatEntry.COLUMN_HYDRO_DENSITY);
        if (density == null) {
            throw new IllegalArgumentException("Hydrolat requires a density");
        }

        String quantityGram = values.getAsString(HydrolatEntry.COLUMN_HYDRO_QUANTITY_GRAM);
        if (quantityGram == null) {
            throw new IllegalArgumentException("Hydrolat requires a quantity in gram");
        }

        String quantityMl = values.getAsString(HydrolatEntry.COLUMN_HYDRO_QUANTITY_ML);
        if (quantityMl == null) {
            throw new IllegalArgumentException("Hydrolat requires a quantity in mL");
        }

        String website = values.getAsString(HydrolatEntry.COLUMN_HYDRO_AROMAZONE_URL);
        if (website == null) {
            throw new IllegalArgumentException("Hydrolat requires a website url");
        }

        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Insert the new hydrolat with the given values
        long id = mDb.insert(HydrolatEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the hydrolat content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Close the instance of the Database
        mDb.close();

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a Hv into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertHv(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(HvEntry.COLUMN_HV_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Hv requires a name");
        }

        String picture = values.getAsString(HvEntry.COLUMN_HV_PICTURE);
        if (picture == null) {
            throw new IllegalArgumentException("Hv requires a picture");
        }

        String density = values.getAsString(HvEntry.COLUMN_HV_DENSITY);
        if (density == null) {
            throw new IllegalArgumentException("Hv requires a density");
        }

        String quantityGram = values.getAsString(HvEntry.COLUMN_HV_QUANTITY_GRAM);
        if (quantityGram == null) {
            throw new IllegalArgumentException("Hv requires a quantity in gram");
        }

        String quantityMl = values.getAsString(HvEntry.COLUMN_HV_QUANTITY_ML);
        if (quantityMl == null) {
            throw new IllegalArgumentException("Hv requires a quantity in mL");
        }

        String website = values.getAsString(HvEntry.COLUMN_HV_AROMAZONE_URL);
        if (website == null) {
            throw new IllegalArgumentException("Hv requires a website url");
        }

        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Insert the new Hv with the given values
        long id = mDb.insert(HvEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the Hv content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Close the instance of the Database
        mDb.close();

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a He into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertHe(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(HeEntry.COLUMN_HE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("He requires a name");
        }

        String picture = values.getAsString(HeEntry.COLUMN_HE_PICTURE);
        if (picture == null) {
            throw new IllegalArgumentException("He requires a picture");
        }

        String density = values.getAsString(HeEntry.COLUMN_HE_DENSITY);
        if (density == null) {
            throw new IllegalArgumentException("He requires a density");
        }

        String quantityGram = values.getAsString(HeEntry.COLUMN_HE_QUANTITY_GRAM);
        if (quantityGram == null) {
            throw new IllegalArgumentException("He requires a quantity in gram");
        }

        String quantityMl = values.getAsString(HeEntry.COLUMN_HE_QUANTITY_ML);
        if (quantityMl == null) {
            throw new IllegalArgumentException("He requires a quantity in mL");
        }

        String website = values.getAsString(HeEntry.COLUMN_HE_AROMAZONE_URL);
        if (website == null) {
            throw new IllegalArgumentException("He requires a website url");
        }

        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Insert the new He with the given values
        long id = mDb.insert(HeEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the He content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Close the instance of the Database
        mDb.close();

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a Extrait into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertExtrait(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(ExtrEntry.COLUMN_EXTR_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Extrait requires a name");
        }

        String picture = values.getAsString(ExtrEntry.COLUMN_EXTR_PICTURE);
        if (picture == null) {
            throw new IllegalArgumentException("Extrait requires a picture");
        }

        String density = values.getAsString(ExtrEntry.COLUMN_EXTR_DENSITY);
        if (density == null) {
            throw new IllegalArgumentException("Extrait requires a density");
        }

        String quantityGram = values.getAsString(ExtrEntry.COLUMN_EXTR_QUANTITY_GRAM);
        if (quantityGram == null) {
            throw new IllegalArgumentException("Extrait requires a quantity in gram");
        }

        String quantityMl = values.getAsString(ExtrEntry.COLUMN_EXTR_QUANTITY_ML);
        if (quantityMl == null) {
            throw new IllegalArgumentException("Extrait requires a quantity in mL");
        }

        String website = values.getAsString(ExtrEntry.COLUMN_EXTR_AROMAZONE_URL);
        if (website == null) {
            throw new IllegalArgumentException("Extrait requires a website url");
        }

        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Insert the new Extr with the given values
        long id = mDb.insert(ExtrEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the Extr content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Close the instance of the Database
        mDb.close();

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a Poudre into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPoudre(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(PoudrEntry.COLUMN_POUDR_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Poudr requires a name");
        }

        String picture = values.getAsString(PoudrEntry.COLUMN_POUDR_PICTURE);
        if (picture == null) {
            throw new IllegalArgumentException("Poudr requires a picture");
        }

        String density = values.getAsString(PoudrEntry.COLUMN_POUDR_DENSITY);
        if (density == null) {
            throw new IllegalArgumentException("Poudr requires a density");
        }

        String quantityGram = values.getAsString(PoudrEntry.COLUMN_POUDR_QUANTITY_GRAM);
        if (quantityGram == null) {
            throw new IllegalArgumentException("Poudr requires a quantity in gram");
        }

        String quantityMl = values.getAsString(PoudrEntry.COLUMN_POUDR_QUANTITY_ML);
        if (quantityMl == null) {
            throw new IllegalArgumentException("Poudr requires a quantity in mL");
        }

        String website = values.getAsString(PoudrEntry.COLUMN_POUDR_AROMAZONE_URL);
        if (website == null) {
            throw new IllegalArgumentException("Poudr requires a website url");
        }

        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Insert the new Poudre with the given values
        long id = mDb.insert(PoudrEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the Poudr content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Close the instance of the Database
        mDb.close();

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a Actif into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertActif(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(ActifEntry.COLUMN_ACTIF_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Actif requires a name");
        }

        String picture = values.getAsString(ActifEntry.COLUMN_ACTIF_PICTURE);
        if (picture == null) {
            throw new IllegalArgumentException("Actif requires a picture");
        }

        String density = values.getAsString(ActifEntry.COLUMN_ACTIF_DENSITY);
        if (density == null) {
            throw new IllegalArgumentException("Actif requires a density");
        }

        String quantityGram = values.getAsString(ActifEntry.COLUMN_ACTIF_QUANTITY_GRAM);
        if (quantityGram == null) {
            throw new IllegalArgumentException("Actif requires a quantity in gram");
        }

        String quantityMl = values.getAsString(ActifEntry.COLUMN_ACTIF_QUANTITY_ML);
        if (quantityMl == null) {
            throw new IllegalArgumentException("Actif requires a quantity in mL");
        }

        String website = values.getAsString(ActifEntry.COLUMN_ACTIF_AROMAZONE_URL);
        if (website == null) {
            throw new IllegalArgumentException("Actif requires a website url");
        }

        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Insert the new Actif with the given values
        long id = mDb.insert(ActifEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the Actif content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Close the instance of the Database
        mDb.close();

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a Diver into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertDiver(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(DiversEntry.COLUMN_DIVERS_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Divers requires a name");
        }

        String picture = values.getAsString(DiversEntry.COLUMN_DIVERS_PICTURE);
        if (picture == null) {
            throw new IllegalArgumentException("Divers requires a picture");
        }

        String density = values.getAsString(DiversEntry.COLUMN_DIVERS_DENSITY);
        if (density == null) {
            throw new IllegalArgumentException("Divers requires a density");
        }

        String quantityGram = values.getAsString(DiversEntry.COLUMN_DIVERS_QUANTITY_GRAM);
        if (quantityGram == null) {
            throw new IllegalArgumentException("Divers requires a quantity in gram");
        }

        String quantityMl = values.getAsString(DiversEntry.COLUMN_DIVERS_QUANTITY_ML);
        if (quantityMl == null) {
            throw new IllegalArgumentException("Divers requires a quantity in mL");
        }

        String website = values.getAsString(DiversEntry.COLUMN_DIVERS_AROMAZONE_URL);
        if (website == null) {
            throw new IllegalArgumentException("Divers requires a website url");
        }

        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Insert the new Divers with the given values
        long id = mDb.insert(DiversEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the Diver content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Close the instance of the Database
        mDb.close();

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a Contenant into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertContenant(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(ContenEntry.COLUMN_CONTEN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Conten requires a name");
        }

        String picture = values.getAsString(ContenEntry.COLUMN_CONTEN_PICTURE);
        if (picture == null) {
            throw new IllegalArgumentException("Conten requires a picture");
        }

        String quantityTotal = values.getAsString(ContenEntry.COLUMN_CONTEN_QUANTITY_TOTAL);
        if (quantityTotal == null) {
            throw new IllegalArgumentException("Conten requires a valid quantity total");
        }

       String quantityDispo = values.getAsString(ContenEntry.COLUMN_CONTEN_QUANTITY_DISPO);
        if (quantityDispo == null) {
            throw new IllegalArgumentException("Conten requires a valid quantity dispo");
        }

        String website = values.getAsString(ContenEntry.COLUMN_CONTEN_AROMAZONE_URL);
        if (website == null) {
            throw new IllegalArgumentException("Conten requires a website url");
        }

        // Get writable database
        mDb = mDbHelper.getWritableDatabase();

        // Insert the new Contenant with the given values
        long id = mDb.insert(ContenEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the Contenant content URI
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
            case HYDROLATS:
                return updateHydrolat(uri, contentValues, selection, selectionArgs);
            case HYDROLAT_ID:
                // For the HYDROLAT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = HydrolatEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateHydrolat(uri, contentValues, selection, selectionArgs);
            case HVS:
                return updateHv(uri, contentValues, selection, selectionArgs);
            case HV_ID:
                selection = HvEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateHv(uri, contentValues, selection, selectionArgs);
            case HES:
                return updateHe(uri, contentValues, selection, selectionArgs);
            case HE_ID:
                selection = HeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateHe(uri, contentValues, selection, selectionArgs);
            case EXTRAITS:
                return updateExtrait(uri, contentValues, selection, selectionArgs);
            case EXTRAIT_ID:
                selection = ExtrEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateExtrait(uri, contentValues, selection, selectionArgs);
            case POUDRES:
                return updatePoudre(uri, contentValues, selection, selectionArgs);
            case POUDRE_ID:
                selection = PoudrEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePoudre(uri, contentValues, selection, selectionArgs);
            case ACTIFS:
                return updateActif(uri, contentValues, selection, selectionArgs);
            case ACTIF_ID:
                selection = ActifEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateActif(uri, contentValues, selection, selectionArgs);
            case DIVERS:
                return updateDiver(uri, contentValues, selection, selectionArgs);
            case DIVER_ID:
                selection = DiversEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateDiver(uri, contentValues, selection, selectionArgs);
            case CONTENANTS:
                return updateContenant(uri, contentValues, selection, selectionArgs);
            case CONTENANT_ID:
                selection = ContenEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateContenant(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update hydrolats in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more plants).
     * Return the number of rows that were successfully updated.
     */
    private int updateHydrolat(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link HydrolatEntry#COLUMN_HYDRO_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(HydrolatEntry.COLUMN_HYDRO_NAME)) {
            String name = values.getAsString(HydrolatEntry.COLUMN_HYDRO_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Hydrolat requires at least a name");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        mDb = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mDb.update(HydrolatEntry.TABLE_NAME, values, selection, selectionArgs);

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

    /**
     * Update Hvs in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more plants).
     * Return the number of rows that were successfully updated.
     */
    private int updateHv(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link HvEntry#COLUMN_HV_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(HvEntry.COLUMN_HV_NAME)) {
            String name = values.getAsString(HvEntry.COLUMN_HV_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Hv requires at least a name");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        mDb = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mDb.update(HvEntry.TABLE_NAME, values, selection, selectionArgs);

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

    /**
     * Update Hes in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more plants).
     * Return the number of rows that were successfully updated.
     */
    private int updateHe(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link HeEntry#COLUMN_HYDRO_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(HeEntry.COLUMN_HE_NAME)) {
            String name = values.getAsString(HeEntry.COLUMN_HE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("He requires at least a name");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        mDb = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mDb.update(HeEntry.TABLE_NAME, values, selection, selectionArgs);

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

    /**
     * Update Extraits in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more plants).
     * Return the number of rows that were successfully updated.
     */
    private int updateExtrait(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link ExtrEntry#COLUMN_HYDRO_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(ExtrEntry.COLUMN_EXTR_NAME)) {
            String name = values.getAsString(ExtrEntry.COLUMN_EXTR_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Extrait requires at least a name");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        mDb = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mDb.update(ExtrEntry.TABLE_NAME, values, selection, selectionArgs);

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

    /**
     * Update Poudrs in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more plants).
     * Return the number of rows that were successfully updated.
     */
    private int updatePoudre(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PoudrEntry#COLUMN_HYDRO_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PoudrEntry.COLUMN_POUDR_NAME)) {
            String name = values.getAsString(PoudrEntry.COLUMN_POUDR_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Poudre requires at least a name");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        mDb = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mDb.update(PoudrEntry.TABLE_NAME, values, selection, selectionArgs);

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

    /**
     * Update Actifs in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more plants).
     * Return the number of rows that were successfully updated.
     */
    private int updateActif(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link ActifEntry#COLUMN_HYDRO_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(ActifEntry.COLUMN_ACTIF_NAME)) {
            String name = values.getAsString(ActifEntry.COLUMN_ACTIF_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Actif requires at least a name");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        mDb = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mDb.update(ActifEntry.TABLE_NAME, values, selection, selectionArgs);

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

    /**
     * Update Diverss in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more plants).
     * Return the number of rows that were successfully updated.
     */
    private int updateDiver(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link DiversEntry#COLUMN_HYDRO_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(DiversEntry.COLUMN_DIVERS_NAME)) {
            String name = values.getAsString(DiversEntry.COLUMN_DIVERS_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Divers requires at least a name");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        mDb = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mDb.update(DiversEntry.TABLE_NAME, values, selection, selectionArgs);

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

    /**
     * Update Contens in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more plants).
     * Return the number of rows that were successfully updated.
     */
    private int updateContenant(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link ContenEntry#COLUMN_HYDRO_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(ContenEntry.COLUMN_CONTEN_NAME)) {
            String name = values.getAsString(ContenEntry.COLUMN_CONTEN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Contenant requires at least a name");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        mDb = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = mDb.update(ContenEntry.TABLE_NAME, values, selection, selectionArgs);

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
            case HYDROLATS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = mDb.delete(HydrolatEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HYDROLAT_ID:
                // Delete a single row given by the ID in the URI
                selection = HydrolatEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = mDb.delete(HydrolatEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HVS:
                rowsDeleted = mDb.delete(HvEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HV_ID:
                selection = HvEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = mDb.delete(HvEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HES:
                rowsDeleted = mDb.delete(HeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HE_ID:
                selection = HeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = mDb.delete(HeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EXTRAITS:
                rowsDeleted = mDb.delete(ExtrEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EXTRAIT_ID:
                selection = ExtrEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = mDb.delete(ExtrEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case POUDRES:
                rowsDeleted = mDb.delete(PoudrEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case POUDRE_ID:
                selection = PoudrEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = mDb.delete(PoudrEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ACTIFS:
                rowsDeleted = mDb.delete(ActifEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ACTIF_ID:
                selection = ActifEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = mDb.delete(ActifEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DIVERS:
                rowsDeleted = mDb.delete(DiversEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DIVER_ID:
                selection = DiversEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = mDb.delete(DiversEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CONTENANTS:
                rowsDeleted = mDb.delete(ContenEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CONTENANT_ID:
                selection = ContenEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = mDb.delete(ContenEntry.TABLE_NAME, selection, selectionArgs);
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
            case HYDROLATS:
                return HydrolatEntry.CONTENT_LIST_TYPE;
            case HYDROLAT_ID:
                return HydrolatEntry.CONTENT_ITEM_TYPE;
            case HVS:
                return HvEntry.CONTENT_LIST_TYPE;
            case HV_ID:
                return HvEntry.CONTENT_ITEM_TYPE;
            case HES:
                return HeEntry.CONTENT_LIST_TYPE;
            case HE_ID:
                return HeEntry.CONTENT_ITEM_TYPE;
            case EXTRAITS:
                return ExtrEntry.CONTENT_LIST_TYPE;
            case EXTRAIT_ID:
                return ExtrEntry.CONTENT_ITEM_TYPE;
            case POUDRES:
                return PoudrEntry.CONTENT_LIST_TYPE;
            case POUDRE_ID:
                return PoudrEntry.CONTENT_ITEM_TYPE;
            case ACTIFS:
                return ActifEntry.CONTENT_LIST_TYPE;
            case ACTIF_ID:
                return ActifEntry.CONTENT_ITEM_TYPE;
            case DIVERS:
                return DiversEntry.CONTENT_LIST_TYPE;
            case DIVER_ID:
                return DiversEntry.CONTENT_ITEM_TYPE;
            case CONTENANTS:
                return ContenEntry.CONTENT_LIST_TYPE;
            case CONTENANT_ID:
                return ContenEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
