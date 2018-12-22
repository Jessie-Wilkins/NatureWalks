package com.example.jessie.naturewalks;

//Import the following
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraTest extends AppCompatActivity {
    //Declares the image view object
    ImageView mImageView;
    //Declares and sets initial request to 1
    static final int REQUEST_TAKE_PHOTO = 1;

    //Declares the current photo path variable
    String mCurrentPhotoPath;

    //Declares the start camera button
    Button startCamera;

    //Declares the save entry button
    Button saveEntry;

    //Declares edit text object
    EditText text_entry;

    //Declares the shared preferences
    SharedPreferences entrySaves;

    //Declares intent
    Intent classIntent;

    //Sets class object
    Class aClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_places_test);

        //Initializes intent with former intent content
        classIntent = getIntent();

        //Initializes the shared preference
        entrySaves = PreferenceManager.getDefaultSharedPreferences(this);

        //If the class number is 1, set class object to main menu class
        if(classIntent.getIntExtra("Class", 0) == 1) {
            aClass = MainMenu.class;
        }
        //If the class number is 2, set class object to user tracking class
        else if(classIntent.getIntExtra("Class", 0) == 2){
            aClass = TestJSONActivity.class;
        }
        //Initializes the buttons
        saveEntry = (Button) findViewById(R.id.save_entry);
        //Initializes the edit text
        text_entry = (EditText) findViewById(R.id.text_entry);
        //Enables the the button and edit text
        saveEntry.setEnabled(false);
        text_entry.setEnabled(false);

        //If the savedInstanceState is not null
        if(savedInstanceState != null) {
            //Enables the button adn edit text
            saveEntry.setEnabled(true);
            text_entry.setEnabled(true);
        }

        //Sets the button listener for the save entry button
        saveEntry.setOnClickListener(new View.OnClickListener() {

            /**
             * If the button is clicked.
             * @param v
             */
            @Override
            public void onClick(View v) {
                //Declares and initializes the editor for the shared preferences
                SharedPreferences.Editor editor = entrySaves.edit();
                //Puts the file path, latitude, and longitude to the editor
                editor.putString(mCurrentPhotoPath.toString(), text_entry.getText().toString());
                editor.putString("Pic_Lat_for"+mCurrentPhotoPath, Double.toString(classIntent.getDoubleExtra("Pic_Lat", 0)));
                editor.putString("Pic_Lng_for"+mCurrentPhotoPath, Double.toString(classIntent.getDoubleExtra("Pic_Lng",0)));
                //Commits changes
                editor.commit();
                //Declares and sets the intent to the given class
                Intent intent = new Intent(getApplicationContext(), aClass);
                //Adds flag that signals to remove the activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Start activity
                startActivity(intent);
            }
        });
        //Initializes start camera button
        startCamera = (Button) findViewById(R.id.start_camera);

        //Sets the  button listener
        startCamera.setOnClickListener(new View.OnClickListener() {
            /**
             * If the button is clicked
             * @param v
             */
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    /**
     * Sets the picture file locations and start the camera
     */
    private void dispatchTakePictureIntent() {
        //Declare and initialize the intent to the camera app
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //if the intent activity package manager is not null
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {

            //Declare and set the File object to null
            File photoFile = null;

            //Tries the file by setting it to the created picture file
            try {
                photoFile = createImageFile();
            }
            //Catches the exception and gives the message
            catch(IOException ex) {
                Log.d("FILE_ERROR", ex.toString());
            }
            //If the file is not null
            if(photoFile != null) {
                //Sets the uri object to the file's uri
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);

                //Stores the uri in the intent
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //starts activity with the expectation of a result
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }//end if statement
    }

    private File createImageFile() throws IOException {
        //Declares and sets the time stamp to the date
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //Declares and sets the file name to the given file name with the time stamp
        String imageFileName = "JPEG_" +timeStamp + "_";
        //Declares and sets the file object to the picture directory
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //Declares and sets the file object to a temporary file name
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        //Sets the file path to the photo file path
        mCurrentPhotoPath = image.getAbsolutePath();
        //Returns the image
        return image;

    }

    /**
     * After the camera app has a result, it does certain actions
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the photo is taken and it is saved, the actions will occur
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //Initialize the image view object
            mImageView = (ImageView) findViewById(R.id.captured_images);
            //Enables the button and text view
            saveEntry.setEnabled(true);
            text_entry.setEnabled(true);
            //Sets the optimized image
            setPic();
        }

    }

    /**
     * Sets the image view to the optimized image
     */
    private void setPic() {
        //Declares and initializes the target width and target height
        //to the image view's width and height
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        //if the target width and height are 0,
        //set them both to 1
        if(targetW == 0 || targetH == 0) {
            targetW = 1;
            targetH = 1;
        }

        //Declare and initializes the options for the Bitmap factory
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //Sets the bounds of the decoded bitmap factory to true
        bmOptions.inJustDecodeBounds = true;
        //Decodes file with given options
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        //Declare and set the photo width and photo height
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //Declares and initializes the scaled down pictures
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        //Sets the bounds to false, the size to the scaled size, and the purgeable option to true
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        //Declare and initialize the bit map to the bitmap factory's decoded file
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        //Set the image view to the bit map
        mImageView.setImageBitmap(bitmap);
    }

    /**
     * When the instance state is restored
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //Sets the current photo path to the saved instance state's file path
        mCurrentPhotoPath = savedInstanceState.getString("file_path");
    }

    /**
     * When the instance state is saved
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Put the file path into the bundle
        outState.putString("file_path",mCurrentPhotoPath);
        //Call super with bundle
        super.onSaveInstanceState(outState);
    }
}
