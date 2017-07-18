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
import android.support.v4.app.NavUtils;
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

import com.example.android.herbalsupply.data.HerbContract.PlantEntry;

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

    // EditTextView field to enter the plant's price
    private EditText mPriceEditText;

    // EditTextView field to enter the plant's supplier info
    private EditText mSupplierInfoEditText;

    // EditTextView field to enter the plant's supplier mail
    private EditText mSupplierMailEditText;

    // TextView to display the quantity
    private TextView mQuantityTextView;

    // Quantity global variable
    private int mQuantity = 0;

    // Plus button to increment the quantity
    private Button mPlusButton;

    // Minus button to decrement the quantity
    private Button mMinusButton;

    // Order button to send an email to the supplier mail
    private Button mOrderButton;

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

        mPriceEditText = (EditText) findViewById(R.id.edit_plant_price);
        mSupplierInfoEditText = (EditText) findViewById(R.id.edit_supplier_info);
        mSupplierMailEditText = (EditText) findViewById(R.id.edit_supplier_mail);
        mQuantityTextView = (TextView) findViewById(R.id.edit_quantity_text_view);

        // Find the plus button and setup the listener
        mPlusButton = (Button) findViewById(R.id.button_plus);
        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusButtonClicked();
            }
        });

        // Find the minus button and setup the listener
        mMinusButton = (Button) findViewById(R.id.button_minus);
        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusButtonClicked();
            }
        });

        // Find the order button and setup the listener
        mOrderButton = (Button) findViewById(R.id.order_button);
        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderSupplier();
            }
        });

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new plant or editing an existing one.
        Intent intent = getIntent();
        mCurrentPlantUri = intent.getData();

        // If the intent DOES NOT contain a plant content URI, then we know that we are
        // creating a new plant.
        if (mCurrentPlantUri == null) {
            // This is a new plant, so change the app bar to say "Add a Plant"
            setTitle(getString(R.string.editor_activity_title_new_plant));
            displayQuantity();
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
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierInfoEditText.setOnTouchListener(mTouchListener);
        mSupplierMailEditText.setOnTouchListener(mTouchListener);
        mQuantityTextView.setOnTouchListener(mTouchListener);
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

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String supplierInfoString = mSupplierInfoEditText.getText().toString().trim();
        String supplierMailString = mSupplierMailEditText.getText().toString().trim();
        String quantityString = mQuantityTextView.getText().toString();

        // Check if this is supposed to be a new plant
        // and check if all the fields in the editor are blank
        if (mCurrentPlantUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(supplierInfoString) && TextUtils.isEmpty(supplierMailString) &&
                mImageUri == null) {
            // Since no fields were modified, we can return early without creating a new plant.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return true;
        }

        // Create a ContentValues object where column names are the keys,
        // and plant attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(PlantEntry.COLUMN_PLANT_QUANTITY, quantityString);

        // Inform the user that the name must be filled
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, getString(R.string.plant_name_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        // Put the name value in the ContentValues
        values.put(PlantEntry.COLUMN_PLANT_NAME, nameString);

        // Inform the user that the plant must have a picture (either chosen or taken)
        if (mImageUri == null) {
            Toast.makeText(this, getString(R.string.plant_picture_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        // Put the picture uri in the ContentValues
        values.put(PlantEntry.COLUMN_PLANT_PICTURE, mImageUri.toString());

        // Inform the user that the price must be filled
        if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, getString(R.string.plant_price_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        // Put the price value in the ContentValues
        values.put(PlantEntry.COLUMN_PLANT_PRICE, priceString);

        // Inform the user that the supplier info field must be filled
        if (TextUtils.isEmpty(supplierInfoString)) {
            Toast.makeText(this, getString(R.string.plant_supplier_info_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        // Put the supplier info value in the ContentValues
        values.put(PlantEntry.COLUMN_PLANT_SUPPLIER_INFO, supplierInfoString);

        // Inform the user that the supplier mail field must be filled
        if (TextUtils.isEmpty(supplierMailString)) {
            Toast.makeText(this, getString(R.string.plant_supplier_mail_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        // Put the supplier mail value in the ContentValues
        values.put(PlantEntry.COLUMN_PLANT_SUPPLIER_MAIL, supplierMailString);

        // Determine if this is a new or existing plant by checking if mCurrentPetUri is null or not
        if (mCurrentPlantUri == null) {
            // This is a NEW plant, so insert a new plant into the provider,
            // returning the content URI for the new plant.
            Uri newUri = getContentResolver().insert(PlantEntry.CONTENT_URI, values);

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
    }

    // Method to send an email to the supplier mail, passing some content.
    public void orderSupplier(){
        Intent mailSupplier = new Intent(android.content.Intent.ACTION_SENDTO);
        mailSupplier.setType("text/plain");
        mailSupplier.setData(Uri.parse("mailto:" + mSupplierMailEditText.getText().toString().trim()));
        mailSupplier.putExtra(android.content.Intent.EXTRA_SUBJECT, "New Order");
        String mailText = "We need a new order of " + mNameEditText.getText().toString().trim();
        mailSupplier.putExtra(android.content.Intent.EXTRA_TEXT,mailText);
        if (mailSupplier.resolveActivity(getPackageManager()) != null) {
            startActivity(mailSupplier);
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
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
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
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
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
        // Since the editor shows all plant attributes, define a projection that contains
        // all columns from the plants table
        String[] projection = {
                PlantEntry._ID,
                PlantEntry.COLUMN_PLANT_NAME,
                PlantEntry.COLUMN_PLANT_PICTURE,
                PlantEntry.COLUMN_PLANT_QUANTITY,
                PlantEntry.COLUMN_PLANT_PRICE,
                PlantEntry.COLUMN_PLANT_SUPPLIER_INFO,
                PlantEntry.COLUMN_PLANT_SUPPLIER_MAIL};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentPlantUri,       // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
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
            // Find the columns of plant attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_NAME);
            int pictureColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_PICTURE);
            int quantityColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_PRICE);
            int supplierInfoColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_SUPPLIER_INFO);
            int supplierMailColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_SUPPLIER_MAIL);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String imageUriString = cursor.getString(pictureColumnIndex);
            mQuantity = cursor.getInt(quantityColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String supplierInfo = cursor.getString(supplierInfoColumnIndex);
            String supplierMail = cursor.getString(supplierMailColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mPriceEditText.setText(price);
            mSupplierInfoEditText.setText(supplierInfo);
            mSupplierMailEditText.setText(supplierMail);
            mQuantityTextView.setText(Integer.toString(mQuantity));
            mImageUri = Uri.parse(imageUriString);
            mPictureImageView.setImageURI(mImageUri);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mSupplierInfoEditText.setText("");
        mSupplierMailEditText.setText("");
        mQuantityTextView.setText("");
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
    public void plusButtonClicked() {
        mQuantity++;
        displayQuantity();
    }

    // Method to decrement the quantity when plus button clicked and display it
    // Handle the case where the quantity is already at zero, prevent a negative value
    public void minusButtonClicked() {
        if (mQuantity == 0) {
            Toast.makeText(this, "Can't decrease quantity", Toast.LENGTH_SHORT).show();
        } else {
            mQuantity--;
            displayQuantity();
        }
    }

    // Method to display the quantity in the given textView
    public void displayQuantity() {
        mQuantityTextView.setText(String.valueOf(mQuantity));
    }
}