package com.example.android.herbalsupply.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 *
 * API Contract for the Herbal Supply app.
 */
public class HerbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private HerbContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.herbalsupply";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     */
    public static final String PATH_PLANTS = "plants";

    /**
     * Inner class that defines constant values for the plants database table.
     * Each entry in the table represents a single plant product.
     */
    public static final class PlantEntry implements BaseColumns {

        /** The content URI to access the plant data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLANTS);

        /** Name of database table for plants */
        public final static String TABLE_NAME = "plants";

        /**
         * Unique ID number for the plant (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the plant.
         * Type: TEXT
         */
        public final static String COLUMN_PLANT_NAME ="name";

        /**
         * Picture of the plant.
         * Type: TEXT
         */
        public final static String COLUMN_PLANT_PICTURE = "picture";

        /**
         * Price of the plant.
         * Type: TEXT
         */
        public final static String COLUMN_PLANT_PRICE = "price";

        /**
         * Quantity of the plant.
         * Type: INTEGER
         */
        public final static String COLUMN_PLANT_QUANTITY = "quantity";

        /**
         * Supplier info for the plant.
         * Type: TEXT
         */
        public final static String COLUMN_PLANT_SUPPLIER_INFO = "supinfo";

        /**
         * Supplier mail for the plant.
         * Type: TEXT
         */
        public final static String COLUMN_PLANT_SUPPLIER_MAIL = "supmail";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of plants.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLANTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single plant.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLANTS;
    }
}
