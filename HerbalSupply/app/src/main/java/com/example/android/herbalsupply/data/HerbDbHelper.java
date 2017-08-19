package com.example.android.herbalsupply.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
 * Database helper for Herbal Supply app. Manages database creation and version management.
 */
public class HerbDbHelper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "aromazone.db";

    /** Database version. If you change the database schema, you must increment the database version. */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link HerbDbHelper}.
     *
     * @param context of the app
     */
    public HerbDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** This is called when the database is created for the first time. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the hydrolats table
        String SQL_CREATE_HYDRO_TABLE =  "CREATE TABLE " + HydrolatEntry.TABLE_NAME + " ("
                + HydrolatEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HydrolatEntry.COLUMN_HYDRO_NAME + " TEXT NOT NULL, "
                + HydrolatEntry.COLUMN_HYDRO_PICTURE + " TEXT, "
                + HydrolatEntry.COLUMN_HYDRO_DENSITY + " TEXT, "
                + HydrolatEntry.COLUMN_HYDRO_QUANTITY_GRAM + " TEXT DEFAULT NA, "
                + HydrolatEntry.COLUMN_HYDRO_QUANTITY_ML + " TEXT DEFAULT NA, "
                + HydrolatEntry.COLUMN_HYDRO_AROMAZONE_URL + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_HYDRO_TABLE);

        // Create a String that contains the SQL statement to create the huiles vegetales table
        String SQL_CREATE_HV_TABLE =  "CREATE TABLE " + HvEntry.TABLE_NAME + " ("
                + HvEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HvEntry.COLUMN_HV_NAME + " TEXT NOT NULL, "
                + HvEntry.COLUMN_HV_PICTURE + " TEXT, "
                + HvEntry.COLUMN_HV_DENSITY + " TEXT, "
                + HvEntry.COLUMN_HV_QUANTITY_GRAM + " TEXT DEFAULT NA, "
                + HvEntry.COLUMN_HV_QUANTITY_ML + " TEXT DEFAULT NA, "
                + HvEntry.COLUMN_HV_AROMAZONE_URL + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_HV_TABLE);

        // Create a String that contains the SQL statement to create the huiles essentielles table
        String SQL_CREATE_HE_TABLE =  "CREATE TABLE " + HeEntry.TABLE_NAME + " ("
                + HeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HeEntry.COLUMN_HE_NAME + " TEXT NOT NULL, "
                + HeEntry.COLUMN_HE_PICTURE + " TEXT, "
                + HeEntry.COLUMN_HE_DENSITY + " TEXT, "
                + HeEntry.COLUMN_HE_QUANTITY_GRAM + " TEXT DEFAULT NA, "
                + HeEntry.COLUMN_HE_QUANTITY_ML + " TEXT DEFAULT NA, "
                + HeEntry.COLUMN_HE_AROMAZONE_URL + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_HE_TABLE);

        // Create a String that contains the SQL statement to create the Extraits table
        String SQL_CREATE_EXTR_TABLE =  "CREATE TABLE " + ExtrEntry.TABLE_NAME + " ("
                + ExtrEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExtrEntry.COLUMN_EXTR_NAME + " TEXT NOT NULL, "
                + ExtrEntry.COLUMN_EXTR_PICTURE + " TEXT, "
                + ExtrEntry.COLUMN_EXTR_DENSITY + " TEXT, "
                + ExtrEntry.COLUMN_EXTR_QUANTITY_GRAM + " TEXT DEFAULT NA, "
                + ExtrEntry.COLUMN_EXTR_QUANTITY_ML + " TEXT DEFAULT NA, "
                + ExtrEntry.COLUMN_EXTR_AROMAZONE_URL + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_EXTR_TABLE);

        // Create a String that contains the SQL statement to create the Poudres table
        String SQL_CREATE_POUDR_TABLE =  "CREATE TABLE " + PoudrEntry.TABLE_NAME + " ("
                + PoudrEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PoudrEntry.COLUMN_POUDR_NAME + " TEXT NOT NULL, "
                + PoudrEntry.COLUMN_POUDR_PICTURE + " TEXT, "
                + PoudrEntry.COLUMN_POUDR_DENSITY + " TEXT, "
                + PoudrEntry.COLUMN_POUDR_QUANTITY_GRAM + " TEXT DEFAULT NA, "
                + PoudrEntry.COLUMN_POUDR_QUANTITY_ML + " TEXT DEFAULT NA, "
                + PoudrEntry.COLUMN_POUDR_AROMAZONE_URL + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_POUDR_TABLE);

        // Create a String that contains the SQL statement to create the Actifs table
        String SQL_CREATE_ACTIF_TABLE =  "CREATE TABLE " + ActifEntry.TABLE_NAME + " ("
                + ActifEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ActifEntry.COLUMN_ACTIF_NAME + " TEXT NOT NULL, "
                + ActifEntry.COLUMN_ACTIF_PICTURE + " TEXT, "
                + ActifEntry.COLUMN_ACTIF_DENSITY + " TEXT, "
                + ActifEntry.COLUMN_ACTIF_QUANTITY_GRAM + " TEXT DEFAULT NA, "
                + ActifEntry.COLUMN_ACTIF_QUANTITY_ML + " TEXT DEFAULT NA, "
                + ActifEntry.COLUMN_ACTIF_AROMAZONE_URL + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ACTIF_TABLE);

        // Create a String that contains the SQL statement to create the Divers table
        String SQL_CREATE_DIVERS_TABLE =  "CREATE TABLE " + DiversEntry.TABLE_NAME + " ("
                + DiversEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DiversEntry.COLUMN_DIVERS_NAME + " TEXT NOT NULL, "
                + DiversEntry.COLUMN_DIVERS_PICTURE + " TEXT, "
                + DiversEntry.COLUMN_DIVERS_DENSITY + " TEXT, "
                + DiversEntry.COLUMN_DIVERS_QUANTITY_GRAM + " TEXT DEFAULT NA, "
                + DiversEntry.COLUMN_DIVERS_QUANTITY_ML + " TEXT DEFAULT NA, "
                + DiversEntry.COLUMN_DIVERS_AROMAZONE_URL + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_DIVERS_TABLE);

        // Create a String that contains the SQL statement to create the Contens table
        String SQL_CREATE_CONTEN_TABLE =  "CREATE TABLE " + ContenEntry.TABLE_NAME + " ("
                + ContenEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContenEntry.COLUMN_CONTEN_NAME + " TEXT NOT NULL, "
                + ContenEntry.COLUMN_CONTEN_PICTURE + " TEXT, "
                + ContenEntry.COLUMN_CONTEN_QUANTITY_TOTAL + " TEXT DEFAULT 0, "
                + ContenEntry.COLUMN_CONTEN_QUANTITY_DISPO + " TEXT DEFAULT 0, "
                + ContenEntry.COLUMN_CONTEN_AROMAZONE_URL + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_CONTEN_TABLE);
    }

    /** This is called when the database needs to be upgraded. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_HYDRO_ENTRIES = "DELETE TABLE IF EXISTS " + HydrolatEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_HYDRO_ENTRIES);
        String SQL_DELETE_HV_ENTRIES = "DELETE TABLE IF EXISTS " + HvEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_HV_ENTRIES);
        String SQL_DELETE_HE_ENTRIES = "DELETE TABLE IF EXISTS " + HeEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_HE_ENTRIES);
        String SQL_DELETE_EXTR_ENTRIES = "DELETE TABLE IF EXISTS " + ExtrEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_EXTR_ENTRIES);
        String SQL_DELETE_POUDR_ENTRIES = "DELETE TABLE IF EXISTS " + PoudrEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_POUDR_ENTRIES);
        String SQL_DELETE_ACTIF_ENTRIES = "DELETE TABLE IF EXISTS " + ActifEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ACTIF_ENTRIES);
        String SQL_DELETE_DIVERS_ENTRIES = "DELETE TABLE IF EXISTS " + DiversEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_DIVERS_ENTRIES);
        String SQL_DELETE_CONTEN_ENTRIES = "DELETE TABLE IF EXISTS " + ContenEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_CONTEN_ENTRIES);
        onCreate(db);
    }
}