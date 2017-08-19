package com.example.android.herbalsupply;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.herbalsupply.data.HerbContract.HydrolatEntry;
import com.example.android.herbalsupply.data.HerbContract.HvEntry;
import com.example.android.herbalsupply.data.HerbContract.HeEntry;
import com.example.android.herbalsupply.data.HerbContract.ExtrEntry;
import com.example.android.herbalsupply.data.HerbContract.PoudrEntry;
import com.example.android.herbalsupply.data.HerbContract.ActifEntry;
import com.example.android.herbalsupply.data.HerbContract.DiversEntry;
import com.example.android.herbalsupply.data.HerbContract.ContenEntry;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 *
 * Allows user to create a new plant or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the plant data loader */
    private static final int EXISTING_PLANT_LOADER = 0;

    /** Content URI for the existing plant (null if it's a new plant) */
    private Uri mCurrentPlantUri;

    // The table we are in
    private int mTableType;

    // EditText field to enter the plant's name
    private EditText mNameEditText;

    // ImageView for the plant's picture
    private ImageView mPictureImageView;

    // Choose a picture button to select an existing picture in the gallery
    private Button mChoosePictureButton;

    // Id for the choose a picture Intent
    private static final int REQUEST_CHOOSE_PICTURE = 0;

    // Take a picture button to take a picture for the plant
    private Button mTakePictureButton;

    // Id for the take a picture Intent
    private static final int REQUEST_TAKE_PICTURE = 1;

    // EditTextView field for the density
    private EditText mDensityEditText;

    // EditTextView to display the quantity in g
    private EditText mQuantityGramEditText;

    // EditTextView to display the quantity in mL
    private EditText mQuantityMlEditText;

    // EditTextView to display the quantity total
    private TextView mQuantityTotalTextView;

    // EditTextView to display the quantity dispo
    private TextView mQuantityDispoTextView;

    // Quantity total global variable
    private int mQuantityTotal = 0;

    // Quantity dispo global variable
    private int mQuantityDispo = 0;

    // Plus total button to increment the quantity
    private Button mTotalPlusButton;

    // Minus total button to decrement the quantity
    private Button mTotalMinusButton;

    // Plus dispo button to increment the quantity
    private Button mDispoPlusButton;

    // Minus dispo button to decrement the quantity
    private Button mDispoMinusButton;

    // EditTextView field to enter the plant's supplier mail
    private EditText mAromazoneUrlEditText;

    // Calculate button to the conversion of quantity with density
    private Button mCalculateButton;

    // Reset button to reset the two quantity EditText
    private Button mResetButton;

    // Website button to go to the website of the url provide
    private Button mWebsiteButton;

    // Path for the picture taken with the camera
    private String mCurrentPhotoPath;

    // Uri of the plant's picture
    private Uri mImageUri = null;

    /** Boolean flag that keeps track of whether the plant has been edited (true) or not (false) */
    private boolean mPlantHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPlantHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPlantHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_plant_name);
        mPictureImageView = (ImageView) findViewById(R.id.edit_plant_picture);

        // Find the take a picture button and setup the listener
        mTakePictureButton = (Button) findViewById(R.id.take_picture_button);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                mPlantHasChanged = true;
            }
        });

        // Find the choose a picture button and setup the listener
        mChoosePictureButton = (Button) findViewById(R.id.choose_picture_button);
        mChoosePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                mPlantHasChanged = true;
            }
        });

        mDensityEditText = (EditText) findViewById(R.id.edit_density);
        mQuantityGramEditText = (EditText) findViewById(R.id.edit_plant_quantity_gram);
        mQuantityMlEditText = (EditText) findViewById(R.id.edit_plant_quantity_ml);

        mCalculateButton = (Button) findViewById(R.id.calculate_button);
        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateWithDensity();
                mPlantHasChanged = true;
            }
        });

        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetQuantityEditTexts();
                mPlantHasChanged = true;
            }
        });

        mQuantityTotalTextView = (TextView) findViewById(R.id.edit_quantity_total_text_view);
        mQuantityDispoTextView = (TextView) findViewById(R.id.edit_quantity_dispo_text_view);

        // Find the total plus button and setup the listener
        mTotalPlusButton = (Button) findViewById(R.id.total_button_plus);
        mTotalPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusTotalButtonClicked();
            }
        });

        // Find the total minus button and setup the listener
        mTotalMinusButton = (Button) findViewById(R.id.total_button_minus);
        mTotalMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusTotalButtonClicked();
            }
        });

        // Find the dispo plus button and setup the listener
        mDispoPlusButton = (Button) findViewById(R.id.dispo_button_plus);
        mDispoPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusDispoButtonClicked();
            }
        });

        // Find the dispo minus button and setup the listener
        mDispoMinusButton = (Button) findViewById(R.id.dispo_button_minus);
        mDispoMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusDispoButtonClicked();
            }
        });

        mAromazoneUrlEditText = (EditText) findViewById(R.id.edit_aromazone_url);

        mWebsiteButton = (Button) findViewById(R.id.website_button);
        mWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWebsite();
            }
        });

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new plant or editing an existing one.
        Intent intent = getIntent();
        mCurrentPlantUri = intent.getData();
        Bundle bundle = getIntent().getExtras();
        mTableType = bundle.getInt("TableType");

        if (mTableType == 7){
            TextView densityText = (TextView) findViewById(R.id.density_text);
            densityText.setVisibility(View.INVISIBLE);
            mDensityEditText.setVisibility(View.INVISIBLE);
            TextView quantityText = (TextView) findViewById(R.id.quantity_text);
            quantityText.setVisibility(View.INVISIBLE);
            View layoutQuantityGramMl = findViewById(R.id.layout_quantity_g_ml);
            layoutQuantityGramMl.setVisibility(View.INVISIBLE);
            View layoutCalculateReset = findViewById(R.id.layout_calculate_reset);
            layoutCalculateReset.setVisibility(View.INVISIBLE);
        } else {
            View layoutQuantityTotalDispo = findViewById(R.id.layout_quantity_total_dispo);
            layoutQuantityTotalDispo.setVisibility(View.GONE);
        }

        // If the intent DOES NOT contain a plant content URI, then we know that we are
        // creating a new plant.
        if (mCurrentPlantUri == null) {
            // This is a new plant, so change the app bar to say "Add a Plant"
            setTitle(getString(R.string.editor_activity_title_new_plant));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a plant that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing plant, so change app bar to say "Edit Plant"
            setTitle(getString(R.string.editor_activity_title_edit_plant));
            // Initialize a loader to read the plant data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PLANT_LOADER, null, this);
        }

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mPictureImageView.setOnTouchListener(mTouchListener);
        mDensityEditText.setOnTouchListener(mTouchListener);
        mQuantityGramEditText.setOnTouchListener(mTouchListener);
        mQuantityMlEditText.setOnTouchListener(mTouchListener);
        mQuantityTotalTextView.setOnTouchListener(mTouchListener);
        mQuantityDispoTextView.setOnTouchListener(mTouchListener);
        mAromazoneUrlEditText.setOnTouchListener(mTouchListener);
    }

    // Method that send an intent to the camera to take a picture
    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            }
        }
    }

    // Method to create the picture file
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Method that send an intent to the device to go the gallery to choose an existing picture
    public void choosePicture(){
        Intent choosePictureIntent;
        if (Build.VERSION.SDK_INT < 19) {
            choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            choosePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            choosePictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        choosePictureIntent.setType("image/*");
        if (choosePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(choosePictureIntent, REQUEST_CHOOSE_PICTURE);
        }
    }

    // Method called when returning from the camera or the gallery to get a picture for the plant
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case REQUEST_CHOOSE_PICTURE:
                    if (data != null) {
                        // Get the uri of the picture in the gallery
                        mImageUri = data.getData();
                        mPictureImageView.setImageURI(mImageUri);
                        mPictureImageView.invalidate();
                    }
                    break;
                case REQUEST_TAKE_PICTURE:
                    if (data != null) {
                        // Get the uri from the Path created in createImageFile method
                        mImageUri = Uri.parse(mCurrentPhotoPath);
                        mPictureImageView.setImageURI(mImageUri);
                        mPictureImageView.invalidate();
                    }
                    break;
            }
        }
    }

    /**
     * Get user input from editor and save pet into database.
     */
    private boolean savePlant() {
        // Create a ContentValues object where column names are the keys,
        // and plant attributes from the editor are the values.
        ContentValues values = new ContentValues();

        switch (mTableType){
            case 0:
                // Read from input fields
                // Use trim to eliminate leading or trailing white space
                String nameString = mNameEditText.getText().toString().trim();
                String densityString = mDensityEditText.getText().toString();
                String quantityGramString = mQuantityGramEditText.getText().toString();
                String quantityMlString = mQuantityMlEditText.getText().toString();
                String aromazoneString = mAromazoneUrlEditText.getText().toString();

                // Check if this is supposed to be a new plant
                // and check if all the fields in the editor are blank
                if (mCurrentPlantUri == null &&
                        TextUtils.isEmpty(nameString) && TextUtils.isEmpty(densityString) &&
                        TextUtils.isEmpty(quantityGramString) && TextUtils.isEmpty(quantityMlString)
                        && TextUtils.isEmpty(aromazoneString) && mImageUri == null) {
                    // Since no fields were modified, we can return early without creating a new plant.
                    // No need to create ContentValues and no need to do any ContentProvider operations.
                    return true;
                }

                // Inform the user that the name must be filled
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, getString(R.string.plant_name_required), Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Put the name value in the ContentValues
                values.put(HydrolatEntry.COLUMN_HYDRO_NAME, nameString);

                if (mImageUri == null) {
                    String imagePlaceHolderUri = getString(R.string.dummy_plant_picture_uri);
                    values.put(HydrolatEntry.COLUMN_HYDRO_PICTURE, imagePlaceHolderUri);
                } else {
                    values.put(HydrolatEntry.COLUMN_HYDRO_PICTURE, mImageUri.toString());
                }

                // If density not provided put Unknown in the database
                if (TextUtils.isEmpty(densityString)) {
                    densityString = "Unknown";
                    values.put(HydrolatEntry.COLUMN_HYDRO_DENSITY, densityString);
                } else {
                    values.put(HydrolatEntry.COLUMN_HYDRO_DENSITY, densityString);
                }

                // If the quantity in gram is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityGramString)) {
                    quantityGramString = "NA";
                    values.put(HydrolatEntry.COLUMN_HYDRO_QUANTITY_GRAM, quantityGramString);
                } else {
                    values.put(HydrolatEntry.COLUMN_HYDRO_QUANTITY_GRAM, quantityGramString);
                }

                // If the quantity in mL is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityMlString)) {
                    quantityMlString = "NA";
                    values.put(HydrolatEntry.COLUMN_HYDRO_QUANTITY_ML, quantityMlString);
                } else {
                    values.put(HydrolatEntry.COLUMN_HYDRO_QUANTITY_ML, quantityMlString);
                }

                // If the aromazone url is not provided put Not provided in the database
                if (TextUtils.isEmpty(aromazoneString)) {
                    aromazoneString = "Not Provided";
                    values.put(HydrolatEntry.COLUMN_HYDRO_AROMAZONE_URL, aromazoneString);
                } else {
                    values.put(HydrolatEntry.COLUMN_HYDRO_AROMAZONE_URL, aromazoneString);
                }

                // Determine if this is a new or existing plant by checking if mCurrentPlantUri is null or not
                if (mCurrentPlantUri == null) {
                    // This is a NEW plant, so insert a new plant into the provider,
                    // returning the content URI for the new plant.
                    Uri newUri = getContentResolver().insert(HydrolatEntry.CONTENT_URI, values);

                    // Show a toast message depending on whether or not the insertion was successful.
                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Otherwise this is an EXISTING plant, so update the plant with content URI: mCurrentPlantUri
                    // and pass in the new ContentValues. Pass in null for the selection and selection args
                    // because mCurrentPlantUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsAffected = getContentResolver().update(mCurrentPlantUri, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(this, getString(R.string.editor_update_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_update_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case 1:
                // Read from input fields
                // Use trim to eliminate leading or trailing white space
                nameString = mNameEditText.getText().toString().trim();
                densityString = mDensityEditText.getText().toString();
                quantityGramString = mQuantityGramEditText.getText().toString();
                quantityMlString = mQuantityMlEditText.getText().toString();
                aromazoneString = mAromazoneUrlEditText.getText().toString();

                // Check if this is supposed to be a new plant
                // and check if all the fields in the editor are blank
                if (mCurrentPlantUri == null &&
                        TextUtils.isEmpty(nameString) && TextUtils.isEmpty(densityString) &&
                        TextUtils.isEmpty(quantityGramString) && TextUtils.isEmpty(quantityMlString)
                        && TextUtils.isEmpty(aromazoneString) && mImageUri == null) {
                    // Since no fields were modified, we can return early without creating a new plant.
                    // No need to create ContentValues and no need to do any ContentProvider operations.
                    return true;
                }

                // Inform the user that the name must be filled
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, getString(R.string.plant_name_required), Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Put the name value in the ContentValues
                values.put(HvEntry.COLUMN_HV_NAME, nameString);

                if (mImageUri == null) {
                    String imagePlaceHolderUri = getString(R.string.dummy_plant_picture_uri);
                    values.put(HvEntry.COLUMN_HV_PICTURE, imagePlaceHolderUri);
                } else {
                    values.put(HvEntry.COLUMN_HV_PICTURE, mImageUri.toString());
                }

                // If density not provided put Unknown in the database
                if (TextUtils.isEmpty(densityString)) {
                    densityString = "Unknown";
                    values.put(HvEntry.COLUMN_HV_DENSITY, densityString);
                } else {
                    values.put(HvEntry.COLUMN_HV_DENSITY, densityString);
                }

                // If the quantity in gram is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityGramString)) {
                    quantityGramString = "NA";
                    values.put(HvEntry.COLUMN_HV_QUANTITY_GRAM, quantityGramString);
                } else {
                    values.put(HvEntry.COLUMN_HV_QUANTITY_GRAM, quantityGramString);
                }

                // If the quantity in mL is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityMlString)) {
                    quantityMlString = "NA";
                    values.put(HvEntry.COLUMN_HV_QUANTITY_ML, quantityMlString);
                } else {
                    values.put(HvEntry.COLUMN_HV_QUANTITY_ML, quantityMlString);
                }

                // If the aromazone url is not prov
                if (TextUtils.isEmpty(aromazoneString)) {
                    aromazoneString = "Not Provided";
                    values.put(HvEntry.COLUMN_HV_AROMAZONE_URL, aromazoneString);
                } else {
                    values.put(HvEntry.COLUMN_HV_AROMAZONE_URL, aromazoneString);
                }

                // Determine if this is a new or existing plant by checking if mCurrentPlantUri is null or not
                if (mCurrentPlantUri == null) {
                    // This is a NEW plant, so insert a new plant into the provider,
                    // returning the content URI for the new plant.
                    Uri newUri = getContentResolver().insert(HvEntry.CONTENT_URI, values);

                    // Show a toast message depending on whether or not the insertion was successful.
                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Otherwise this is an EXISTING plant, so update the plant with content URI: mCurrentPlantUri
                    // and pass in the new ContentValues. Pass in null for the selection and selection args
                    // because mCurrentPlantUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsAffected = getContentResolver().update(mCurrentPlantUri, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(this, getString(R.string.editor_update_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_update_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case 2:
                // Read from input fields
                // Use trim to eliminate leading or trailing white space
                nameString = mNameEditText.getText().toString().trim();
                densityString = mDensityEditText.getText().toString();
                quantityGramString = mQuantityGramEditText.getText().toString();
                quantityMlString = mQuantityMlEditText.getText().toString();
                aromazoneString = mAromazoneUrlEditText.getText().toString();

                // Check if this is supposed to be a new plant
                // and check if all the fields in the editor are blank
                if (mCurrentPlantUri == null &&
                        TextUtils.isEmpty(nameString) && TextUtils.isEmpty(densityString) &&
                        TextUtils.isEmpty(quantityGramString) && TextUtils.isEmpty(quantityMlString)
                        && TextUtils.isEmpty(aromazoneString) && mImageUri == null) {
                    // Since no fields were modified, we can return early without creating a new plant.
                    // No need to create ContentValues and no need to do any ContentProvider operations.
                    return true;
                }

                // Inform the user that the name must be filled
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, getString(R.string.plant_name_required), Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Put the name value in the ContentValues
                values.put(HeEntry.COLUMN_HE_NAME, nameString);

                if (mImageUri == null) {
                    String imagePlaceHolderUri = getString(R.string.dummy_plant_picture_uri);
                    values.put(HeEntry.COLUMN_HE_PICTURE, imagePlaceHolderUri);
                } else {
                    values.put(HeEntry.COLUMN_HE_PICTURE, mImageUri.toString());
                }

                // If density not provided put Unknown in the database
                if (TextUtils.isEmpty(densityString)) {
                    densityString = "Unknown";
                    values.put(HeEntry.COLUMN_HE_DENSITY, densityString);
                } else {
                    values.put(HeEntry.COLUMN_HE_DENSITY, densityString);
                }

                // If the quantity in gram is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityGramString)) {
                    quantityGramString = "NA";
                    values.put(HeEntry.COLUMN_HE_QUANTITY_GRAM, quantityGramString);
                } else {
                    values.put(HeEntry.COLUMN_HE_QUANTITY_GRAM, quantityGramString);
                }

                // If the quantity in mL is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityMlString)) {
                    quantityMlString = "NA";
                    values.put(HeEntry.COLUMN_HE_QUANTITY_ML, quantityMlString);
                } else {
                    values.put(HeEntry.COLUMN_HE_QUANTITY_ML, quantityMlString);
                }

                // If the aromazone url is not provided put Not provided in the database
                if (TextUtils.isEmpty(aromazoneString)) {
                    aromazoneString = "Not Provided";
                    values.put(HeEntry.COLUMN_HE_AROMAZONE_URL, aromazoneString);
                } else {
                    values.put(HeEntry.COLUMN_HE_AROMAZONE_URL, aromazoneString);
                }

                // Determine if this is a new or existing plant by checking if mCurrentPlantUri is null or not
                if (mCurrentPlantUri == null) {
                    // This is a NEW plant, so insert a new plant into the provider,
                    // returning the content URI for the new plant.
                    Uri newUri = getContentResolver().insert(HeEntry.CONTENT_URI, values);

                    // Show a toast message depending on whether or not the insertion was successful.
                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Otherwise this is an EXISTING plant, so update the plant with content URI: mCurrentPlantUri
                    // and pass in the new ContentValues. Pass in null for the selection and selection args
                    // because mCurrentPlantUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsAffected = getContentResolver().update(mCurrentPlantUri, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(this, getString(R.string.editor_update_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_update_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case 3:
                // Read from input fields
                // Use trim to eliminate leading or trailing white space
                nameString = mNameEditText.getText().toString().trim();
                densityString = mDensityEditText.getText().toString();
                quantityGramString = mQuantityGramEditText.getText().toString();
                quantityMlString = mQuantityMlEditText.getText().toString();
                aromazoneString = mAromazoneUrlEditText.getText().toString();

                // Check if this is supposed to be a new plant
                // and check if all the fields in the editor are blank
                if (mCurrentPlantUri == null &&
                        TextUtils.isEmpty(nameString) && TextUtils.isEmpty(densityString) &&
                        TextUtils.isEmpty(quantityGramString) && TextUtils.isEmpty(quantityMlString)
                        && TextUtils.isEmpty(aromazoneString) && mImageUri == null) {
                    // Since no fields were modified, we can return early without creating a new plant.
                    // No need to create ContentValues and no need to do any ContentProvider operations.
                    return true;
                }

                // Inform the user that the name must be filled
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, getString(R.string.plant_name_required), Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Put the name value in the ContentValues
                values.put(ExtrEntry.COLUMN_EXTR_NAME, nameString);

                if (mImageUri == null) {
                    String imagePlaceHolderUri = getString(R.string.dummy_plant_picture_uri);
                    values.put(ExtrEntry.COLUMN_EXTR_PICTURE, imagePlaceHolderUri);
                } else {
                    values.put(ExtrEntry.COLUMN_EXTR_PICTURE, mImageUri.toString());
                }

                // If density not provided put Unknown in the database
                if (TextUtils.isEmpty(densityString)) {
                    densityString = "Unknown";
                    values.put(ExtrEntry.COLUMN_EXTR_DENSITY, densityString);
                } else {
                    values.put(ExtrEntry.COLUMN_EXTR_DENSITY, densityString);
                }

                // If the quantity in gram is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityGramString)) {
                    quantityGramString = "NA";
                    values.put(ExtrEntry.COLUMN_EXTR_QUANTITY_GRAM, quantityGramString);
                } else {
                    values.put(ExtrEntry.COLUMN_EXTR_QUANTITY_GRAM, quantityGramString);
                }

                // If the quantity in mL is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityMlString)) {
                    quantityMlString = "NA";
                    values.put(ExtrEntry.COLUMN_EXTR_QUANTITY_ML, quantityMlString);
                } else {
                    values.put(ExtrEntry.COLUMN_EXTR_QUANTITY_ML, quantityMlString);
                }

                // If the aromazone url is not provided put Not provided in the database
                if (TextUtils.isEmpty(aromazoneString)) {
                    aromazoneString = "Not Provided";
                    values.put(ExtrEntry.COLUMN_EXTR_AROMAZONE_URL, aromazoneString);
                } else {
                    values.put(ExtrEntry.COLUMN_EXTR_AROMAZONE_URL, aromazoneString);
                }

                // Determine if this is a new or existing plant by checking if mCurrentPlantUri is null or not
                if (mCurrentPlantUri == null) {
                    // This is a NEW plant, so insert a new plant into the provider,
                    // returning the content URI for the new plant.
                    Uri newUri = getContentResolver().insert(ExtrEntry.CONTENT_URI, values);

                    // Show a toast message depending on whether or not the insertion was successful.
                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Otherwise this is an EXISTING plant, so update the plant with content URI: mCurrentPlantUri
                    // and pass in the new ContentValues. Pass in null for the selection and selection args
                    // because mCurrentPlantUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsAffected = getContentResolver().update(mCurrentPlantUri, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(this, getString(R.string.editor_update_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_update_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case 4:
                // Read from input fields
                // Use trim to eliminate leading or trailing white space
                nameString = mNameEditText.getText().toString().trim();
                densityString = mDensityEditText.getText().toString();
                quantityGramString = mQuantityGramEditText.getText().toString();
                quantityMlString = mQuantityMlEditText.getText().toString();
                aromazoneString = mAromazoneUrlEditText.getText().toString();

                // Check if this is supposed to be a new plant
                // and check if all the fields in the editor are blank
                if (mCurrentPlantUri == null &&
                        TextUtils.isEmpty(nameString) && TextUtils.isEmpty(densityString) &&
                        TextUtils.isEmpty(quantityGramString) && TextUtils.isEmpty(quantityMlString)
                        && TextUtils.isEmpty(aromazoneString) && mImageUri == null) {
                    // Since no fields were modified, we can return early without creating a new plant.
                    // No need to create ContentValues and no need to do any ContentProvider operations.
                    return true;
                }

                // Inform the user that the name must be filled
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, getString(R.string.plant_name_required), Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Put the name value in the ContentValues
                values.put(PoudrEntry.COLUMN_POUDR_NAME, nameString);

                if (mImageUri == null) {
                    String imagePlaceHolderUri = getString(R.string.dummy_plant_picture_uri);
                    values.put(PoudrEntry.COLUMN_POUDR_PICTURE, imagePlaceHolderUri);
                } else {
                    values.put(PoudrEntry.COLUMN_POUDR_PICTURE, mImageUri.toString());
                }

                // If density not provided put Unknown in the database
                if (TextUtils.isEmpty(densityString)) {
                    densityString = "Unknown";
                    values.put(PoudrEntry.COLUMN_POUDR_DENSITY, densityString);
                } else {
                    values.put(PoudrEntry.COLUMN_POUDR_DENSITY, densityString);
                }

                // If the quantity in gram is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityGramString)) {
                    quantityGramString = "NA";
                    values.put(PoudrEntry.COLUMN_POUDR_QUANTITY_GRAM, quantityGramString);
                } else {
                    values.put(PoudrEntry.COLUMN_POUDR_QUANTITY_GRAM, quantityGramString);
                }

                // If the quantity in mL is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityMlString)) {
                    quantityMlString = "NA";
                    values.put(PoudrEntry.COLUMN_POUDR_QUANTITY_ML, quantityMlString);
                } else {
                    values.put(PoudrEntry.COLUMN_POUDR_QUANTITY_ML, quantityMlString);
                }

                // If the aromazone url is not prov
                if (TextUtils.isEmpty(aromazoneString)) {
                    aromazoneString = "Not Provided";
                    values.put(PoudrEntry.COLUMN_POUDR_AROMAZONE_URL, aromazoneString);
                } else {
                    values.put(PoudrEntry.COLUMN_POUDR_AROMAZONE_URL, aromazoneString);
                }

                // Determine if this is a new or existing plant by checking if mCurrentPlantUri is null or not
                if (mCurrentPlantUri == null) {
                    // This is a NEW plant, so insert a new plant into the provider,
                    // returning the content URI for the new plant.
                    Uri newUri = getContentResolver().insert(PoudrEntry.CONTENT_URI, values);

                    // Show a toast message depending on whether or not the insertion was successful.
                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Otherwise this is an EXISTING plant, so update the plant with content URI: mCurrentPlantUri
                    // and pass in the new ContentValues. Pass in null for the selection and selection args
                    // because mCurrentPlantUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsAffected = getContentResolver().update(mCurrentPlantUri, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(this, getString(R.string.editor_update_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_update_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case 5:
                // Read from input fields
                // Use trim to eliminate leading or trailing white space
                nameString = mNameEditText.getText().toString().trim();
                densityString = mDensityEditText.getText().toString();
                quantityGramString = mQuantityGramEditText.getText().toString();
                quantityMlString = mQuantityMlEditText.getText().toString();
                aromazoneString = mAromazoneUrlEditText.getText().toString();

                // Check if this is supposed to be a new plant
                // and check if all the fields in the editor are blank
                if (mCurrentPlantUri == null &&
                        TextUtils.isEmpty(nameString) && TextUtils.isEmpty(densityString) &&
                        TextUtils.isEmpty(quantityGramString) && TextUtils.isEmpty(quantityMlString)
                        && TextUtils.isEmpty(aromazoneString) && mImageUri == null) {
                    // Since no fields were modified, we can return early without creating a new plant.
                    // No need to create ContentValues and no need to do any ContentProvider operations.
                    return true;
                }

                // Inform the user that the name must be filled
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, getString(R.string.plant_name_required), Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Put the name value in the ContentValues
                values.put(ActifEntry.COLUMN_ACTIF_NAME, nameString);

                if (mImageUri == null) {
                    String imagePlaceHolderUri = getString(R.string.dummy_plant_picture_uri);
                    values.put(ActifEntry.COLUMN_ACTIF_PICTURE, imagePlaceHolderUri);
                } else {
                    values.put(ActifEntry.COLUMN_ACTIF_PICTURE, mImageUri.toString());
                }

                // If density not provided put Unknown in the database
                if (TextUtils.isEmpty(densityString)) {
                    densityString = "Unknown";
                    values.put(ActifEntry.COLUMN_ACTIF_DENSITY, densityString);
                } else {
                    values.put(ActifEntry.COLUMN_ACTIF_DENSITY, densityString);
                }

                // If the quantity in gram is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityGramString)) {
                    quantityGramString = "NA";
                    values.put(ActifEntry.COLUMN_ACTIF_QUANTITY_GRAM, quantityGramString);
                } else {
                    values.put(ActifEntry.COLUMN_ACTIF_QUANTITY_GRAM, quantityGramString);
                }

                // If the quantity in mL is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityMlString)) {
                    quantityMlString = "NA";
                    values.put(ActifEntry.COLUMN_ACTIF_QUANTITY_ML, quantityMlString);
                } else {
                    values.put(ActifEntry.COLUMN_ACTIF_QUANTITY_ML, quantityMlString);
                }

                // If the aromazone url is not provided put Not provided in the database
                if (TextUtils.isEmpty(aromazoneString)) {
                    aromazoneString = "Not Provided";
                    values.put(ActifEntry.COLUMN_ACTIF_AROMAZONE_URL, aromazoneString);
                } else {
                    values.put(ActifEntry.COLUMN_ACTIF_AROMAZONE_URL, aromazoneString);
                }

                // Determine if this is a new or existing plant by checking if mCurrentPlantUri is null or not
                if (mCurrentPlantUri == null) {
                    // This is a NEW plant, so insert a new plant into the provider,
                    // returning the content URI for the new plant.
                    Uri newUri = getContentResolver().insert(ActifEntry.CONTENT_URI, values);

                    // Show a toast message depending on whether or not the insertion was successful.
                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Otherwise this is an EXISTING plant, so update the plant with content URI: mCurrentPlantUri
                    // and pass in the new ContentValues. Pass in null for the selection and selection args
                    // because mCurrentPlantUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsAffected = getContentResolver().update(mCurrentPlantUri, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(this, getString(R.string.editor_update_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_update_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case 6:
                // Read from input fields
                // Use trim to eliminate leading or trailing white space
                nameString = mNameEditText.getText().toString().trim();
                densityString = mDensityEditText.getText().toString();
                quantityGramString = mQuantityGramEditText.getText().toString();
                quantityMlString = mQuantityMlEditText.getText().toString();
                aromazoneString = mAromazoneUrlEditText.getText().toString();

                // Check if this is supposed to be a new plant
                // and check if all the fields in the editor are blank
                if (mCurrentPlantUri == null &&
                        TextUtils.isEmpty(nameString) && TextUtils.isEmpty(densityString) &&
                        TextUtils.isEmpty(quantityGramString) && TextUtils.isEmpty(quantityMlString)
                        && TextUtils.isEmpty(aromazoneString) && mImageUri == null) {
                    // Since no fields were modified, we can return early without creating a new plant.
                    // No need to create ContentValues and no need to do any ContentProvider operations.
                    return true;
                }

                // Inform the user that the name must be filled
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, getString(R.string.plant_name_required), Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Put the name value in the ContentValues
                values.put(DiversEntry.COLUMN_DIVERS_NAME, nameString);

                if (mImageUri == null) {
                    String imagePlaceHolderUri = getString(R.string.dummy_plant_picture_uri);
                    values.put(DiversEntry.COLUMN_DIVERS_PICTURE, imagePlaceHolderUri);
                } else {
                    values.put(DiversEntry.COLUMN_DIVERS_PICTURE, mImageUri.toString());
                }

                // If density not provided put Unknown in the database
                if (TextUtils.isEmpty(densityString)) {
                    densityString = "Unknown";
                    values.put(DiversEntry.COLUMN_DIVERS_DENSITY, densityString);
                } else {
                    values.put(DiversEntry.COLUMN_DIVERS_DENSITY, densityString);
                }

                // If the quantity in gram is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityGramString)) {
                    quantityGramString = "NA";
                    values.put(DiversEntry.COLUMN_DIVERS_QUANTITY_GRAM, quantityGramString);
                } else {
                    values.put(DiversEntry.COLUMN_DIVERS_QUANTITY_GRAM, quantityGramString);
                }

                // If the quantity in mL is not provided put Not provided in the database
                if (TextUtils.isEmpty(quantityMlString)) {
                    quantityMlString = "NA";
                    values.put(DiversEntry.COLUMN_DIVERS_QUANTITY_ML, quantityMlString);
                } else {
                    values.put(DiversEntry.COLUMN_DIVERS_QUANTITY_ML, quantityMlString);
                }

                // If the aromazone url is not provided put Not provided in the database
                if (TextUtils.isEmpty(aromazoneString)) {
                    aromazoneString = "Not Provided";
                    values.put(DiversEntry.COLUMN_DIVERS_AROMAZONE_URL, aromazoneString);
                } else {
                    values.put(DiversEntry.COLUMN_DIVERS_AROMAZONE_URL, aromazoneString);
                }

                // Determine if this is a new or existing plant by checking if mCurrentPlantUri is null or not
                if (mCurrentPlantUri == null) {
                    // This is a NEW plant, so insert a new plant into the provider,
                    // returning the content URI for the new plant.
                    Uri newUri = getContentResolver().insert(DiversEntry.CONTENT_URI, values);

                    // Show a toast message depending on whether or not the insertion was successful.
                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Otherwise this is an EXISTING plant, so update the plant with content URI: mCurrentPlantUri
                    // and pass in the new ContentValues. Pass in null for the selection and selection args
                    // because mCurrentPlantUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsAffected = getContentResolver().update(mCurrentPlantUri, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(this, getString(R.string.editor_update_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_update_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case 7:
                nameString = mNameEditText.getText().toString().trim();
                String quantityTotal = mQuantityTotalTextView.getText().toString();
                String quantityDispo = mQuantityDispoTextView.getText().toString();
                aromazoneString = mAromazoneUrlEditText.getText().toString();

                // Check if this is supposed to be a new plant
                // and check if all the fields in the editor are blank
                if (mCurrentPlantUri == null &&
                        TextUtils.isEmpty(nameString)  && TextUtils.isEmpty(quantityTotal) &&
                        TextUtils.isEmpty(quantityDispo) && TextUtils.isEmpty(aromazoneString) &&
                        mImageUri == null) {
                    // Since no fields were modified, we can return early without creating a new plant.
                    // No need to create ContentValues and no need to do any ContentProvider operations.
                    return true;
                }

                // Inform the user that the name must be filled
                if (TextUtils.isEmpty(nameString)) {
                    Toast.makeText(this, getString(R.string.plant_name_required), Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Put the name value in the ContentValues
                values.put(ContenEntry.COLUMN_CONTEN_NAME, nameString);

                if (mImageUri == null) {
                    String imagePlaceHolderUri = getString(R.string.dummy_plant_picture_uri);
                    values.put(ContenEntry.COLUMN_CONTEN_PICTURE, imagePlaceHolderUri);
                } else {
                    values.put(ContenEntry.COLUMN_CONTEN_PICTURE, mImageUri.toString());
                }

                if (TextUtils.isEmpty(quantityTotal)) {
                    quantityTotal = "0";
                    values.put(ContenEntry.COLUMN_CONTEN_QUANTITY_TOTAL, quantityTotal);
                } else {
                    values.put(ContenEntry.COLUMN_CONTEN_QUANTITY_TOTAL, quantityTotal);
                }

                if (TextUtils.isEmpty(quantityDispo)) {
                    quantityDispo = "0";
                    values.put(ContenEntry.COLUMN_CONTEN_QUANTITY_DISPO, quantityDispo);
                } else {
                    values.put(ContenEntry.COLUMN_CONTEN_QUANTITY_DISPO, quantityDispo);
                }

                // If the aromazone url is not provided put Not provided in the database
                if (TextUtils.isEmpty(aromazoneString)) {
                    aromazoneString = "Not Provided";
                    values.put(ContenEntry.COLUMN_CONTEN_AROMAZONE_URL, aromazoneString);
                } else {
                    values.put(ContenEntry.COLUMN_CONTEN_AROMAZONE_URL, aromazoneString);
                }

                // Determine if this is a new or existing plant by checking if mCurrentPlantUri is null or not
                if (mCurrentPlantUri == null) {
                    // This is a NEW plant, so insert a new plant into the provider,
                    // returning the content URI for the new plant.
                    Uri newUri = getContentResolver().insert(ContenEntry.CONTENT_URI, values);

                    // Show a toast message depending on whether or not the insertion was successful.
                    if (newUri == null) {
                        // If the new content URI is null, then there was an error with insertion.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_insert_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Otherwise this is an EXISTING plant, so update the plant with content URI: mCurrentPlantUri
                    // and pass in the new ContentValues. Pass in null for the selection and selection args
                    // because mCurrentPlantUri will already identify the correct row in the database that
                    // we want to modify.
                    int rowsAffected = getContentResolver().update(mCurrentPlantUri, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(this, getString(R.string.editor_update_plant_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(this, getString(R.string.editor_update_plant_successful), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                return false;
        }
    }

    // Method to calculate the conversion in g and mL
    public void calculateWithDensity(){
        try {
            float density = Float.parseFloat(mDensityEditText.getText().toString());
            if (!TextUtils.isEmpty(mQuantityGramEditText.getText().toString()) || mQuantityGramEditText.getText().toString().contains("NA")){
                try {
                    Float conversionGramMl = Float.parseFloat(mQuantityGramEditText.getText().toString()) / density;
                    mQuantityMlEditText.setText(String.format("%.2f",conversionGramMl));
                } catch (NumberFormatException nfEx){
                    Toast.makeText(this, "Not a valid Gram Quantity", Toast.LENGTH_SHORT).show();
                }
            } else if (!TextUtils.isEmpty(mQuantityMlEditText.getText().toString()) || mQuantityMlEditText.getText().toString().contains("NA")){
                try {
                    Float conversionMlGram = Float.parseFloat(mQuantityMlEditText.getText().toString()) * density;
                    mQuantityGramEditText.setText(String.format("%.2f",conversionMlGram));
                } catch (NumberFormatException nfEx) {
                    Toast.makeText(this, "Not a valid mL Quantity", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NumberFormatException nfEx){
            Toast.makeText(this, "Not a valid Density", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to reset both quantity EditTextViews
    public void resetQuantityEditTexts(){
        mQuantityGramEditText.setText("");
        mQuantityMlEditText.setText("");
    }

    // Method to go to the website provided by the user
    public void goToWebsite(){
        Uri websiteUrl = Uri.parse(mAromazoneUrlEditText.toString());
        Intent website = new Intent(Intent.ACTION_VIEW, websiteUrl);
        if (website.resolveActivity(getPackageManager()) != null) {
            startActivity(website);
        } else {
            Toast.makeText(this, "Problem with the url provided", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new plant, hide the "Delete" menu item.
        if (mCurrentPlantUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save plant to database
                if(savePlant()) {
                    // Exit activity
                    finish();
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the plant hasn't changed, continue with navigating up to parent activity
                // which is the {@link StockActivity}.
                if (!mPlantHasChanged) {
                    finish();
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                finish();
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the plant hasn't changed, continue with handling back button press
        if (!mPlantHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (mTableType){
            case 0:
                // Since the editor shows all plant attributes, define a projection that contains
                // all columns from the plants table
                String[] projectionHydrolat = {
                        HydrolatEntry._ID,
                        HydrolatEntry.COLUMN_HYDRO_NAME,
                        HydrolatEntry.COLUMN_HYDRO_PICTURE,
                        HydrolatEntry.COLUMN_HYDRO_DENSITY,
                        HydrolatEntry.COLUMN_HYDRO_QUANTITY_GRAM,
                        HydrolatEntry.COLUMN_HYDRO_QUANTITY_ML,
                        HydrolatEntry.COLUMN_HYDRO_AROMAZONE_URL};

                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,   // Parent activity context
                        mCurrentPlantUri,       // Query the content URI for the current pet
                        projectionHydrolat,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order
            case 1:
                // Since the editor shows all plant attributes, define a projection that contains
                // all columns from the plants table
                String[] projectionHv = {
                        HvEntry._ID,
                        HvEntry.COLUMN_HV_NAME,
                        HvEntry.COLUMN_HV_PICTURE,
                        HvEntry.COLUMN_HV_DENSITY,
                        HvEntry.COLUMN_HV_QUANTITY_GRAM,
                        HvEntry.COLUMN_HV_QUANTITY_ML,
                        HvEntry.COLUMN_HV_AROMAZONE_URL};

                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,   // Parent activity context
                        mCurrentPlantUri,       // Query the content URI for the current pet
                        projectionHv,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);
            case 2:
                // Since the editor shows all plant attributes, define a projection that contains
                // all columns from the plants table
                String[] projectionHe = {
                        HeEntry._ID,
                        HeEntry.COLUMN_HE_NAME,
                        HeEntry.COLUMN_HE_PICTURE,
                        HeEntry.COLUMN_HE_DENSITY,
                        HeEntry.COLUMN_HE_QUANTITY_GRAM,
                        HeEntry.COLUMN_HE_QUANTITY_ML,
                        HeEntry.COLUMN_HE_AROMAZONE_URL};

                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,   // Parent activity context
                        mCurrentPlantUri,       // Query the content URI for the current pet
                        projectionHe,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order
            case 3:
                // Since the editor shows all plant attributes, define a projection that contains
                // all columns from the plants table
                String[] projectionExtr = {
                        ExtrEntry._ID,
                        ExtrEntry.COLUMN_EXTR_NAME,
                        ExtrEntry.COLUMN_EXTR_PICTURE,
                        ExtrEntry.COLUMN_EXTR_DENSITY,
                        ExtrEntry.COLUMN_EXTR_QUANTITY_GRAM,
                        ExtrEntry.COLUMN_EXTR_QUANTITY_ML,
                        ExtrEntry.COLUMN_EXTR_AROMAZONE_URL};

                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,   // Parent activity context
                        mCurrentPlantUri,       // Query the content URI for the current pet
                        projectionExtr,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order
            case 4:
                // Since the editor shows all plant attributes, define a projection that contains
                // all columns from the plants table
                String[] projectionPoudr = {
                        PoudrEntry._ID,
                        PoudrEntry.COLUMN_POUDR_NAME,
                        PoudrEntry.COLUMN_POUDR_PICTURE,
                        PoudrEntry.COLUMN_POUDR_DENSITY,
                        PoudrEntry.COLUMN_POUDR_QUANTITY_GRAM,
                        PoudrEntry.COLUMN_POUDR_QUANTITY_ML,
                        PoudrEntry.COLUMN_POUDR_AROMAZONE_URL};

                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,   // Parent activity context
                        mCurrentPlantUri,       // Query the content URI for the current pet
                        projectionPoudr,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order
            case 5:
                // Since the editor shows all plant attributes, define a projection that contains
                // all columns from the plants table
                String[] projectionActif = {
                        ActifEntry._ID,
                        ActifEntry.COLUMN_ACTIF_NAME,
                        ActifEntry.COLUMN_ACTIF_PICTURE,
                        ActifEntry.COLUMN_ACTIF_DENSITY,
                        ActifEntry.COLUMN_ACTIF_QUANTITY_GRAM,
                        ActifEntry.COLUMN_ACTIF_QUANTITY_ML,
                        ActifEntry.COLUMN_ACTIF_AROMAZONE_URL};

                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,   // Parent activity context
                        mCurrentPlantUri,       // Query the content URI for the current pet
                        projectionActif,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order
            case 6:
                // Since the editor shows all plant attributes, define a projection that contains
                // all columns from the plants table
                String[] projectionDivers = {
                        DiversEntry._ID,
                        DiversEntry.COLUMN_DIVERS_NAME,
                        DiversEntry.COLUMN_DIVERS_PICTURE,
                        DiversEntry.COLUMN_DIVERS_DENSITY,
                        DiversEntry.COLUMN_DIVERS_QUANTITY_GRAM,
                        DiversEntry.COLUMN_DIVERS_QUANTITY_ML,
                        DiversEntry.COLUMN_DIVERS_AROMAZONE_URL};

                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,   // Parent activity context
                        mCurrentPlantUri,       // Query the content URI for the current pet
                        projectionDivers,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order
            case 7:
                // Since the editor shows all plant attributes, define a projection that contains
                // all columns from the plants table
                String[] projectionConten = {
                        ContenEntry._ID,
                        ContenEntry.COLUMN_CONTEN_NAME,
                        ContenEntry.COLUMN_CONTEN_PICTURE,
                        ContenEntry.COLUMN_CONTEN_QUANTITY_TOTAL,
                        ContenEntry.COLUMN_CONTEN_QUANTITY_DISPO,
                        ContenEntry.COLUMN_CONTEN_AROMAZONE_URL};

                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,   // Parent activity context
                        mCurrentPlantUri,       // Query the content URI for the current pet
                        projectionConten,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            switch (mTableType){
                case 0:
                    // Find the columns of plant attributes that we're interested in
                    int nameHydrolatColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_NAME);
                    int pictureHydrolatColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_PICTURE);
                    int densityHydrolatColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_DENSITY);
                    int quantityGramHydrolatColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_QUANTITY_GRAM);
                    int quantityMlHydrolatColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_QUANTITY_ML);
                    int aromazoneUrlHydrolatColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_AROMAZONE_URL);

                    // Extract out the value from the Cursor for the given column index
                    String nameHydrolat = cursor.getString(nameHydrolatColumnIndex);
                    String imageUriHydrolatString = cursor.getString(pictureHydrolatColumnIndex);
                    String densityHydrolat = cursor.getString(densityHydrolatColumnIndex);
                    String quantityGramHydrolat = cursor.getString(quantityGramHydrolatColumnIndex);
                    String quantityMlHydrolat = cursor.getString(quantityMlHydrolatColumnIndex);
                    String aromazoneWebsiteHydrolat = cursor.getString(aromazoneUrlHydrolatColumnIndex);

                    // Update the views on the screen with the values from the database
                    mNameEditText.setText(nameHydrolat);
                    mImageUri = Uri.parse(imageUriHydrolatString);
                    mPictureImageView.setImageURI(mImageUri);
                    mDensityEditText.setText(densityHydrolat);
                    mQuantityGramEditText.setText(quantityGramHydrolat);
                    mQuantityMlEditText.setText(quantityMlHydrolat);
                    mAromazoneUrlEditText.setText(aromazoneWebsiteHydrolat);
                    break;

                case 1:
                    // Find the columns of plant attributes that we're interested in
                    int nameHvColumnIndex = cursor.getColumnIndex(HvEntry.COLUMN_HV_NAME);
                    int pictureHvColumnIndex = cursor.getColumnIndex(HvEntry.COLUMN_HV_PICTURE);
                    int densityHvColumnIndex = cursor.getColumnIndex(HvEntry.COLUMN_HV_DENSITY);
                    int quantityGramHvColumnIndex = cursor.getColumnIndex(HvEntry.COLUMN_HV_QUANTITY_GRAM);
                    int quantityMlHvColumnIndex = cursor.getColumnIndex(HvEntry.COLUMN_HV_QUANTITY_ML);
                    int aromazoneUrlHvColumnIndex = cursor.getColumnIndex(HvEntry.COLUMN_HV_AROMAZONE_URL);

                    // Extract out the value from the Cursor for the given column index
                    String nameHv = cursor.getString(nameHvColumnIndex);
                    String imageUriHvString = cursor.getString(pictureHvColumnIndex);
                    String densityHv = cursor.getString(densityHvColumnIndex);
                    String quantityGramHv = cursor.getString(quantityGramHvColumnIndex);
                    String quantityMlHv = cursor.getString(quantityMlHvColumnIndex);
                    String aromazoneWebsiteHv = cursor.getString(aromazoneUrlHvColumnIndex);

                    // Update the views on the screen with the values from the database
                    mNameEditText.setText(nameHv);
                    mImageUri = Uri.parse(imageUriHvString);
                    mPictureImageView.setImageURI(mImageUri);
                    mDensityEditText.setText(densityHv);
                    mQuantityGramEditText.setText(quantityGramHv);
                    mQuantityMlEditText.setText(quantityMlHv);
                    mAromazoneUrlEditText.setText(aromazoneWebsiteHv);
                    break;

                case 2:
                    // Find the columns of plant attributes that we're interested in
                    int nameHeColumnIndex = cursor.getColumnIndex(HeEntry.COLUMN_HE_NAME);
                    int pictureHeColumnIndex = cursor.getColumnIndex(HeEntry.COLUMN_HE_PICTURE);
                    int densityHeColumnIndex = cursor.getColumnIndex(HeEntry.COLUMN_HE_DENSITY);
                    int quantityGramHeColumnIndex = cursor.getColumnIndex(HeEntry.COLUMN_HE_QUANTITY_GRAM);
                    int quantityMlHeColumnIndex = cursor.getColumnIndex(HeEntry.COLUMN_HE_QUANTITY_ML);
                    int aromazoneUrlHeColumnIndex = cursor.getColumnIndex(HeEntry.COLUMN_HE_AROMAZONE_URL);

                    // Extract out the value from the Cursor for the given column index
                    String nameHe = cursor.getString(nameHeColumnIndex);
                    String imageUriHeString = cursor.getString(pictureHeColumnIndex);
                    String densityHe = cursor.getString(densityHeColumnIndex);
                    String quantityGramHe = cursor.getString(quantityGramHeColumnIndex);
                    String quantityMlHe = cursor.getString(quantityMlHeColumnIndex);
                    String aromazoneWebsiteHe = cursor.getString(aromazoneUrlHeColumnIndex);

                    // Update the views on the screen with the values from the database
                    mNameEditText.setText(nameHe);
                    mImageUri = Uri.parse(imageUriHeString);
                    mPictureImageView.setImageURI(mImageUri);
                    mDensityEditText.setText(densityHe);
                    mQuantityGramEditText.setText(quantityGramHe);
                    mQuantityMlEditText.setText(quantityMlHe);
                    mAromazoneUrlEditText.setText(aromazoneWebsiteHe);
                    break;

                case 3:
                    // Find the columns of plant attributes that we're interested in
                    int nameExtrColumnIndex = cursor.getColumnIndex(ExtrEntry.COLUMN_EXTR_NAME);
                    int pictureExtrColumnIndex = cursor.getColumnIndex(ExtrEntry.COLUMN_EXTR_PICTURE);
                    int densityExtrColumnIndex = cursor.getColumnIndex(ExtrEntry.COLUMN_EXTR_DENSITY);
                    int quantityGramExtrColumnIndex = cursor.getColumnIndex(ExtrEntry.COLUMN_EXTR_QUANTITY_GRAM);
                    int quantityMlExtrColumnIndex = cursor.getColumnIndex(ExtrEntry.COLUMN_EXTR_QUANTITY_ML);
                    int aromazoneUrlExtrColumnIndex = cursor.getColumnIndex(ExtrEntry.COLUMN_EXTR_AROMAZONE_URL);

                    // Extract out the value from the Cursor for the given column index
                    String nameExtr = cursor.getString(nameExtrColumnIndex);
                    String imageUriExtrString = cursor.getString(pictureExtrColumnIndex);
                    String densityExtr = cursor.getString(densityExtrColumnIndex);
                    String quantityGramExtr = cursor.getString(quantityGramExtrColumnIndex);
                    String quantityMlExtr = cursor.getString(quantityMlExtrColumnIndex);
                    String aromazoneWebsiteExtr = cursor.getString(aromazoneUrlExtrColumnIndex);

                    // Update the views on the screen with the values from the database
                    mNameEditText.setText(nameExtr);
                    mImageUri = Uri.parse(imageUriExtrString);
                    mPictureImageView.setImageURI(mImageUri);
                    mDensityEditText.setText(densityExtr);
                    mQuantityGramEditText.setText(quantityGramExtr);
                    mQuantityMlEditText.setText(quantityMlExtr);
                    mAromazoneUrlEditText.setText(aromazoneWebsiteExtr);
                    break;

                case 4:
                    // Find the columns of plant attributes that we're interested in
                    int namePoudrColumnIndex = cursor.getColumnIndex(PoudrEntry.COLUMN_POUDR_NAME);
                    int picturePoudrColumnIndex = cursor.getColumnIndex(PoudrEntry.COLUMN_POUDR_PICTURE);
                    int densityPoudrColumnIndex = cursor.getColumnIndex(PoudrEntry.COLUMN_POUDR_DENSITY);
                    int quantityGramPoudrColumnIndex = cursor.getColumnIndex(PoudrEntry.COLUMN_POUDR_QUANTITY_GRAM);
                    int quantityMlPoudrColumnIndex = cursor.getColumnIndex(PoudrEntry.COLUMN_POUDR_QUANTITY_ML);
                    int aromazoneUrlPoudrColumnIndex = cursor.getColumnIndex(PoudrEntry.COLUMN_POUDR_AROMAZONE_URL);

                    // Extract out the value from the Cursor for the given column index
                    String namePoudr = cursor.getString(namePoudrColumnIndex);
                    String imageUriPoudrString = cursor.getString(picturePoudrColumnIndex);
                    String densityPoudr = cursor.getString(densityPoudrColumnIndex);
                    String quantityGramPoudr = cursor.getString(quantityGramPoudrColumnIndex);
                    String quantityMlPoudr = cursor.getString(quantityMlPoudrColumnIndex);
                    String aromazoneWebsitePoudr = cursor.getString(aromazoneUrlPoudrColumnIndex);

                    // Update the views on the screen with the values from the database
                    mNameEditText.setText(namePoudr);
                    mImageUri = Uri.parse(imageUriPoudrString);
                    mPictureImageView.setImageURI(mImageUri);
                    mDensityEditText.setText(densityPoudr);
                    mQuantityGramEditText.setText(quantityGramPoudr);
                    mQuantityMlEditText.setText(quantityMlPoudr);
                    mAromazoneUrlEditText.setText(aromazoneWebsitePoudr);
                    break;

                case 5:
                    // Find the columns of plant attributes that we're interested in
                    int nameActifColumnIndex = cursor.getColumnIndex(ActifEntry.COLUMN_ACTIF_NAME);
                    int pictureActifColumnIndex = cursor.getColumnIndex(ActifEntry.COLUMN_ACTIF_PICTURE);
                    int densityActifColumnIndex = cursor.getColumnIndex(ActifEntry.COLUMN_ACTIF_DENSITY);
                    int quantityGramActifColumnIndex = cursor.getColumnIndex(ActifEntry.COLUMN_ACTIF_QUANTITY_GRAM);
                    int quantityMlActifColumnIndex = cursor.getColumnIndex(ActifEntry.COLUMN_ACTIF_QUANTITY_ML);
                    int aromazoneUrlActifColumnIndex = cursor.getColumnIndex(ActifEntry.COLUMN_ACTIF_AROMAZONE_URL);

                    // Extract out the value from the Cursor for the given column index
                    String nameActif = cursor.getString(nameActifColumnIndex);
                    String imageUriActifString = cursor.getString(pictureActifColumnIndex);
                    String densityActif = cursor.getString(densityActifColumnIndex);
                    String quantityGramActif = cursor.getString(quantityGramActifColumnIndex);
                    String quantityMlActif = cursor.getString(quantityMlActifColumnIndex);
                    String aromazoneWebsiteActif = cursor.getString(aromazoneUrlActifColumnIndex);

                    // Update the views on the screen with the values from the database
                    mNameEditText.setText(nameActif);
                    mImageUri = Uri.parse(imageUriActifString);
                    mPictureImageView.setImageURI(mImageUri);
                    mDensityEditText.setText(densityActif);
                    mQuantityGramEditText.setText(quantityGramActif);
                    mQuantityMlEditText.setText(quantityMlActif);
                    mAromazoneUrlEditText.setText(aromazoneWebsiteActif);

                case 6:
                    // Find the columns of plant attributes that we're interested in
                    int nameDiversColumnIndex = cursor.getColumnIndex(DiversEntry.COLUMN_DIVERS_NAME);
                    int pictureDiversColumnIndex = cursor.getColumnIndex(DiversEntry.COLUMN_DIVERS_PICTURE);
                    int densityDiversColumnIndex = cursor.getColumnIndex(DiversEntry.COLUMN_DIVERS_DENSITY);
                    int quantityGramDiversColumnIndex = cursor.getColumnIndex(DiversEntry.COLUMN_DIVERS_QUANTITY_GRAM);
                    int quantityMlDiversColumnIndex = cursor.getColumnIndex(DiversEntry.COLUMN_DIVERS_QUANTITY_ML);
                    int aromazoneUrlDiversColumnIndex = cursor.getColumnIndex(DiversEntry.COLUMN_DIVERS_AROMAZONE_URL);

                    // Extract out the value from the Cursor for the given column index
                    String nameDivers = cursor.getString(nameDiversColumnIndex);
                    String imageUriDiversString = cursor.getString(pictureDiversColumnIndex);
                    String densityDivers = cursor.getString(densityDiversColumnIndex);
                    String quantityGramDivers = cursor.getString(quantityGramDiversColumnIndex);
                    String quantityMlDivers = cursor.getString(quantityMlDiversColumnIndex);
                    String aromazoneWebsiteDivers = cursor.getString(aromazoneUrlDiversColumnIndex);

                    // Update the views on the screen with the values from the database
                    mNameEditText.setText(nameDivers);
                    mImageUri = Uri.parse(imageUriDiversString);
                    mPictureImageView.setImageURI(mImageUri);
                    mDensityEditText.setText(densityDivers);
                    mQuantityGramEditText.setText(quantityGramDivers);
                    mQuantityMlEditText.setText(quantityMlDivers);
                    mAromazoneUrlEditText.setText(aromazoneWebsiteDivers);
                    break;

                case 7:
                    // Find the columns of plant attributes that we're interested in
                    int nameContenColumnIndex = cursor.getColumnIndex(ContenEntry.COLUMN_CONTEN_NAME);
                    int pictureContenColumnIndex = cursor.getColumnIndex(ContenEntry.COLUMN_CONTEN_PICTURE);
                    int quantityTotalContenColumnIndex = cursor.getColumnIndex(ContenEntry.COLUMN_CONTEN_QUANTITY_TOTAL);
                    int quantityDispoContenColumnIndex = cursor.getColumnIndex(ContenEntry.COLUMN_CONTEN_QUANTITY_DISPO);
                    int aromazoneUrlContenColumnIndex = cursor.getColumnIndex(ContenEntry.COLUMN_CONTEN_AROMAZONE_URL);

                    // Extract out the value from the Cursor for the given column index
                    String nameConten = cursor.getString(nameContenColumnIndex);
                    String imageUriContenString = cursor.getString(pictureContenColumnIndex);
                    String quantityTotalConten = cursor.getString(quantityTotalContenColumnIndex);
                    String quantityDispoConten = cursor.getString(quantityDispoContenColumnIndex);
                    String aromazoneWebsiteConten = cursor.getString(aromazoneUrlContenColumnIndex);

                    // Update the views on the screen with the values from the database
                    mNameEditText.setText(nameConten);
                    mImageUri = Uri.parse(imageUriContenString);
                    mPictureImageView.setImageURI(mImageUri);
                    mQuantityTotalTextView.setText(quantityTotalConten);
                    mQuantityTotal = Integer.parseInt(quantityTotalConten);
                    mQuantityDispoTextView.setText(quantityDispoConten);
                    mQuantityDispo = Integer.parseInt(quantityDispoConten);
                    mAromazoneUrlEditText.setText(aromazoneWebsiteConten);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        if (mTableType == 7) {
            mNameEditText.setText("");
            mQuantityTotalTextView.setText("");
            mQuantityDispoTextView.setText("");
            mAromazoneUrlEditText.setText("");
        } else {
            mNameEditText.setText("");
            mDensityEditText.setText("");
            mQuantityGramEditText.setText("");
            mQuantityMlEditText.setText("");
            mAromazoneUrlEditText.setText("");
        }
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the plant.
                deletePlant();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the plant.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePlant() {
        // Only perform the delete if this is an existing plant.
        if (mCurrentPlantUri != null) {
            // Call the ContentResolver to delete the plant at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPlantUri
            // content URI already identifies the plant that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentPlantUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_plant_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_plant_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    // Method to increment the quantity when plus button clicked and display it
    public void plusTotalButtonClicked() {
        mQuantityTotal++;
        displayQuantityTotal();
    }

    // Method to decrement the quantity when plus button clicked and display it
    // Handle the case where the quantity is already at zero, prevent a negative value
    public void minusTotalButtonClicked() {
        if (mQuantityTotal == 0) {
            Toast.makeText(this, "Can't decrease quantity", Toast.LENGTH_SHORT).show();
        } else {
            mQuantityTotal--;
            displayQuantityTotal();
        }
    }

    // Method to display the quantity in the given textView
    public void displayQuantityTotal() {
        mQuantityTotalTextView.setText(String.valueOf(mQuantityTotal));
    }

    // Method to increment the quantity when plus button clicked and display it
    public void plusDispoButtonClicked() {
        if (mQuantityDispo == mQuantityTotal) {
            Toast.makeText(this, "Can't have more Dispo than Total", Toast.LENGTH_SHORT).show();
        } else {
            mQuantityDispo++;
            displayQuantityDispo();
        }
    }

    // Method to decrement the quantity when plus button clicked and display it
    // Handle the case where the quantity is already at zero, prevent a negative value
    public void minusDispoButtonClicked() {
        if (mQuantityDispo == 0) {
            Toast.makeText(this, "Can't decrease quantity", Toast.LENGTH_SHORT).show();
        } else {
            mQuantityDispo--;
            displayQuantityDispo();
        }
    }

    // Method to display the quantity in the given textView
    public void displayQuantityDispo() {
        mQuantityDispoTextView.setText(String.valueOf(mQuantityDispo));
    }
}