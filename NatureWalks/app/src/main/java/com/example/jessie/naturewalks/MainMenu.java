package com.example.jessie.naturewalks;

//Import the following
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainMenu extends AppCompatActivity {

    //Declares the location listener object used to find current location
    android.location.LocationListener listen;

    //Declares the location object
    Location loc;

    //Declares the location object
    LocationManager lm;

    //Declares the shared preferences object
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Initializes the location manager object to be used as a location service
        lm= (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Sets all the menu buttons
        Button searchForTrail_btn = (Button) findViewById(R.id.searchForTrail);
        Button startWalk_btn = (Button) findViewById(R.id.startWalk_btn);
        Button takePicture_btn = (Button) findViewById(R.id.TakeAPicture);
        Button savedParks_btn = (Button) findViewById(R.id.saved_parks);
        Button savedNotes_btn = (Button) findViewById(R.id.saved_notes);
        Button reset_btn = (Button) findViewById(R.id.reset_btn);

        //Initializes the preference manager object to get all the default preferences
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        //sets the onClick listener for the search for parks button
        searchForTrail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sets the intent and starts the activity
                Intent i = new Intent(getApplicationContext(), GooglePlaceAutoCompleteTest.class);
                startActivity(i);
            }
        });

        //sets the onClick listener for the start walk tracker
        startWalk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sets the intent and starts the activity
                Intent i = new Intent(getApplicationContext(), TestJSONActivity.class);
                startActivity(i);
            }
        });

        //sets the onClick listener for taking picture button
        takePicture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if there is permissions to get the location
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                //Initializes the location object to the last known user location
                loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //Sets intent to the camera class
                Intent i = new Intent(getApplicationContext(), CameraTest.class);
                //Stores the class number used to determine which class the camera should exit to
                //when it is done in the intent
                i.putExtra("Class", 1);
                //Stores the latitude of the current location in the intent.
                i.putExtra("Pic_Lat", loc.getLatitude());
                //Stores the longitude of the current location in the intent.
                i.putExtra("Pic_Lng", loc.getLongitude());
                //Starts the camera class
                startActivity(i);
            }
        });

        //sets the onClick listener for the search
        savedParks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sets the intent and starts the activity
                Intent i =new Intent(getApplicationContext(), SavedParks.class);
                startActivity(i);
            }
        });

        //sets the onClick listener for the search
        savedNotes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sets the intent and starts the activity
                Intent i = new Intent(getApplicationContext(), SavedNotes.class);
                startActivity(i);
            }
        });

        //sets the onClick listener for the reset button
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creates editor object to clear the entire shared preferences
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
            }
        });

        //Initializes the location listener object
        listen = new LocationListener() {

            /**
             * Sets the location object when location changes
             * @param location
             */
            @Override
            public void onLocationChanged(Location location) {
                loc = location;
            }

            /**
             * Does nothing.
             * @param provider
             * @param status
             * @param extras
             */
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            /**
             * Does Nothing.
             * @param provider
             */
            @Override
            public void onProviderEnabled(String provider) {

            }

            /**
             * Does Nothing.
             * @param provider
             */
            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }
}
