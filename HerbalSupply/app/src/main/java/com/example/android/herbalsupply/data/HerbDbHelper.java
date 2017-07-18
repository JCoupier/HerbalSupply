package com.example.android.herbalsupply.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.herbalsupply.data.HerbContract.PlantEntry;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 *
 * Database helper for Herbal Supply app. Manages database creation and version management.
 */
public class HerbDbHelper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "herbashop.db";

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
        // Create a String that contains the SQL statement to create the plants table
        String SQL_CREATE_PLANTS_TABLE =  "CREATE TABLE " + PlantEntry.TABLE_NAME + " ("
                + PlantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PlantEntry.COLUMN_PLANT_NAME + " TEXT NOT NULL, "
                + PlantEntry.COLUMN_PLANT_PICTURE + " TEXT NOT NULL, "
                + PlantEntry.COLUMN_PLANT_PRICE + " TEXT NOT NULL DEFAULT 0, "
                + PlantEntry.COLUMN_PLANT_QUANTITY + " INTEGER DEFAULT 0, "
                + PlantEntry.COLUMN_PLANT_SUPPLIER_INFO + " TEXT NOT NULL, "
                + PlantEntry.COLUMN_PLANT_SUPPLIER_MAIL + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PLANTS_TABLE);
    }

    /** This is called when the database needs to be upgraded. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_ENTRIES = "DELETE TABLE IF EXISTS " + PlantEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}