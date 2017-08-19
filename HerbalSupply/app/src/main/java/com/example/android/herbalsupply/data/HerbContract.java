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

    /** Possible path (appended to base content URI for possible URI's) */
    public static final String PATH_HYDROLATS = "hydrolats";
    public static final String PATH_HVS = "hvs";
    public static final String PATH_HES = "hes";
    public static final String PATH_EXTRAITS = "extraits";
    public static final String PATH_POUDRES = "poudres";
    public static final String PATH_ACTIFS = "actifs";
    public static final String PATH_DIVERS = "divers";
    public static final String PATH_CONTENANTS = "contenants";

    /**
     * Inner class that defines constant values for the hydrolats database table.
     * Each entry in the table represents a single plant product.
     */
    public static final class HydrolatEntry implements BaseColumns {

        /** The content URI to access the plant data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HYDROLATS);

        /** Name of database table for plants */
        public final static String TABLE_NAME = "hydrolats";

        /**
         * Unique ID number for the hydrolat (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the hydrolat.
         * Type: TEXT
         */
        public final static String COLUMN_HYDRO_NAME ="name";

        /**
         * Picture of the hydrolat.
         * Type: TEXT
         */
        public final static String COLUMN_HYDRO_PICTURE = "picture";

        /**
         * Density of the hydrolat.
         * Type: TEXT
         */
        public final static String COLUMN_HYDRO_DENSITY = "density";

        /**
         * Quantity in g of the hydrolat.
         * Type: TEXT
         */
        public final static String COLUMN_HYDRO_QUANTITY_GRAM = "gram_quantity";

        /**
         * Quantity in mL of the hydrolat.
         * Type: TEXT
         */
        public final static String COLUMN_HYDRO_QUANTITY_ML = "mL_quantity";

        /**
         * Aromazone url for the hydrolat.
         * Type: TEXT
         */
        public final static String COLUMN_HYDRO_AROMAZONE_URL = "aromazone_url";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of plants.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HYDROLATS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single plant.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HYDROLATS;
    }

    /**
     * Inner class that defines constant values for the Hvs database table.
     * Each entry in the table represents a single plant product.
     */
    public static final class HvEntry implements BaseColumns {

        /** The content URI to access the plant data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HVS);

        /** Name of database table for plants */
        public final static String TABLE_NAME = "hvs";

        /**
         * Unique ID number for the Hv (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the Hv.
         * Type: TEXT
         */
        public final static String COLUMN_HV_NAME ="name";

        /**
         * Picture of the Hv.
         * Type: TEXT
         */
        public final static String COLUMN_HV_PICTURE = "picture";

        /**
         * Density of the Hv.
         * Type: TEXT
         */
        public final static String COLUMN_HV_DENSITY = "density";

        /**
         * Quantity in g of the Hv.
         * Type: TEXT
         */
        public final static String COLUMN_HV_QUANTITY_GRAM = "gram_quantity";

        /**
         * Quantity in mL of the Hv.
         * Type: TEXT
         */
        public final static String COLUMN_HV_QUANTITY_ML = "mL_quantity";

        /**
         * Aromazone url for the Hv.
         * Type: TEXT
         */
        public final static String COLUMN_HV_AROMAZONE_URL = "aromazone_url";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of plants.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HVS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single plant.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HVS;
    }

    /**
     * Inner class that defines constant values for the Hes database table.
     * Each entry in the table represents a single plant product.
     */
    public static final class HeEntry implements BaseColumns {

        /** The content URI to access the plant data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HES);

        /** Name of database table for plants */
        public final static String TABLE_NAME = "hes";

        /**
         * Unique ID number for the He (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the He.
         * Type: TEXT
         */
        public final static String COLUMN_HE_NAME ="name";

        /**
         * Picture of the He.
         * Type: TEXT
         */
        public final static String COLUMN_HE_PICTURE = "picture";

        /**
         * Density of the He.
         * Type: TEXT
         */
        public final static String COLUMN_HE_DENSITY = "density";

        /**
         * Quantity in g of the He.
         * Type: TEXT
         */
        public final static String COLUMN_HE_QUANTITY_GRAM = "gram_quantity";

        /**
         * Quantity in mL of the He.
         * Type: TEXT
         */
        public final static String COLUMN_HE_QUANTITY_ML = "mL_quantity";

        /**
         * Aromazone url for the He.
         * Type: TEXT
         */
        public final static String COLUMN_HE_AROMAZONE_URL = "aromazone_url";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of plants.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single plant.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HES;
    }

    /**
     * Inner class that defines constant values for the Extraits database table.
     * Each entry in the table represents a single plant product.
     */
    public static final class ExtrEntry implements BaseColumns {

        /** The content URI to access the plant data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXTRAITS);

        /** Name of database table for plants */
        public final static String TABLE_NAME = "extraits";

        /**
         * Unique ID number for the Extr (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the Extr.
         * Type: TEXT
         */
        public final static String COLUMN_EXTR_NAME ="name";

        /**
         * Picture of the Extr.
         * Type: TEXT
         */
        public final static String COLUMN_EXTR_PICTURE = "picture";

        /**
         * Density of the Extr.
         * Type: TEXT
         */
        public final static String COLUMN_EXTR_DENSITY = "density";

        /**
         * Quantity in g of the Extr.
         * Type: TEXT
         */
        public final static String COLUMN_EXTR_QUANTITY_GRAM = "gram_quantity";

        /**
         * Quantity in mL of the Extr.
         * Type: TEXT
         */
        public final static String COLUMN_EXTR_QUANTITY_ML = "mL_quantity";

        /**
         * Aromazone url for the Extr.
         * Type: TEXT
         */
        public final static String COLUMN_EXTR_AROMAZONE_URL = "aromazone_url";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of plants.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXTRAITS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single plant.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXTRAITS;
    }

    /**
     * Inner class that defines constant values for the Poudres database table.
     * Each entry in the table represents a single plant product.
     */
    public static final class PoudrEntry implements BaseColumns {

        /** The content URI to access the plant data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_POUDRES);

        /** Name of database table for plants */
        public final static String TABLE_NAME = "poudres";

        /**
         * Unique ID number for the Poudres (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the Poudres.
         * Type: TEXT
         */
        public final static String COLUMN_POUDR_NAME ="name";

        /**
         * Picture of the Poudres.
         * Type: TEXT
         */
        public final static String COLUMN_POUDR_PICTURE = "picture";

        /**
         * Density of the Poudres.
         * Type: TEXT
         */
        public final static String COLUMN_POUDR_DENSITY = "density";

        /**
         * Quantity in g of the Poudres.
         * Type: TEXT
         */
        public final static String COLUMN_POUDR_QUANTITY_GRAM = "gram_quantity";

        /**
         * Quantity in mL of the Poudres.
         * Type: TEXT
         */
        public final static String COLUMN_POUDR_QUANTITY_ML = "mL_quantity";

        /**
         * Aromazone url for the Poudres.
         * Type: TEXT
         */
        public final static String COLUMN_POUDR_AROMAZONE_URL = "aromazone_url";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of plants.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POUDRES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single plant.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POUDRES;
    }

    /**
     * Inner class that defines constant values for the Actifs database table.
     * Each entry in the table represents a single plant product.
     */
    public static final class ActifEntry implements BaseColumns {

        /** The content URI to access the plant data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ACTIFS);

        /** Name of database table for plants */
        public final static String TABLE_NAME = "actifs";

        /**
         * Unique ID number for the Actifs (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the Actifs.
         * Type: TEXT
         */
        public final static String COLUMN_ACTIF_NAME ="name";

        /**
         * Picture of the Actifs.
         * Type: TEXT
         */
        public final static String COLUMN_ACTIF_PICTURE = "picture";

        /**
         * Density of the Actifs.
         * Type: TEXT
         */
        public final static String COLUMN_ACTIF_DENSITY = "density";

        /**
         * Quantity in g of the Actifs.
         * Type: TEXT
         */
        public final static String COLUMN_ACTIF_QUANTITY_GRAM = "gram_quantity";

        /**
         * Quantity in mL of the Actifs.
         * Type: TEXT
         */
        public final static String COLUMN_ACTIF_QUANTITY_ML = "mL_quantity";

        /**
         * Aromazone url for the Actifs.
         * Type: TEXT
         */
        public final static String COLUMN_ACTIF_AROMAZONE_URL = "aromazone_url";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of plants.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTIFS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single plant.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTIFS;
    }

    /**
     * Inner class that defines constant values for the Divers database table.
     * Each entry in the table represents a single plant product.
     */
    public static final class DiversEntry implements BaseColumns {

        /** The content URI to access the plant data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DIVERS);

        /** Name of database table for plants */
        public final static String TABLE_NAME = "divers";

        /**
         * Unique ID number for the Divers (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the Divers.
         * Type: TEXT
         */
        public final static String COLUMN_DIVERS_NAME ="name";

        /**
         * Picture of the Divers.
         * Type: TEXT
         */
        public final static String COLUMN_DIVERS_PICTURE = "picture";

        /**
         * Density of the Divers.
         * Type: TEXT
         */
        public final static String COLUMN_DIVERS_DENSITY = "density";

        /**
         * Quantity in g of the Divers.
         * Type: TEXT
         */
        public final static String COLUMN_DIVERS_QUANTITY_GRAM = "gram_quantity";

        /**
         * Quantity in mL of the Divers.
         * Type: TEXT
         */
        public final static String COLUMN_DIVERS_QUANTITY_ML = "mL_quantity";

        /**
         * Aromazone url for the Divers.
         * Type: TEXT
         */
        public final static String COLUMN_DIVERS_AROMAZONE_URL = "aromazone_url";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of plants.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DIVERS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single plant.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DIVERS;
    }

    /**
     * Inner class that defines constant values for the Contenenants database table.
     * Each entry in the table represents a single plant product.
     */
    public static final class ContenEntry implements BaseColumns {

        /** The content URI to access the plant data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CONTENANTS);

        /** Name of database table for plants */
        public final static String TABLE_NAME = "contenants";

        /**
         * Unique ID number for the Contenants (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the Contenants.
         * Type: TEXT
         */
        public final static String COLUMN_CONTEN_NAME ="name";

        /**
         * Picture of the Contenants.
         * Type: TEXT
         */
        public final static String COLUMN_CONTEN_PICTURE = "picture";

        /**
         * Quantity in g of the Contenants.
         * Type: TEXT
         */
        public final static String COLUMN_CONTEN_QUANTITY_TOTAL = "quantity_total";

        /**
         * Quantity in mL of the Contenants.
         * Type: TEXT
         */
        public final static String COLUMN_CONTEN_QUANTITY_DISPO = "quantity_dispo";

        /**
         * Aromazone url for the Contenants.
         * Type: TEXT
         */
        public final static String COLUMN_CONTEN_AROMAZONE_URL = "aromazone_url";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of plants.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENANTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single plant.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENANTS;
    }
}
