package com.example.jessie.naturewalks;

//Import the following
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Declare the GoogleMap
    private GoogleMap mMap;
    //Declare intent
    Intent intent;
    //Declare button for saving parks
    Button save_map;
    //Declare button for going back
    Button back_map;
    //Declare shared preferences
    SharedPreferences saves;
    //Declare latitude-longitude object
    LatLng latlng;
    //Declare park name
    String park_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Initialize the shared preference
        saves = PreferenceManager.getDefaultSharedPreferences(this);
        //Initialize intent
        intent = getIntent();
        //Declare and initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //Get the async for this map
        mapFragment.getMapAsync(this);
        //Initialize the buttons
        save_map = (Button) findViewById(R.id.save_map);
        back_map = (Button) findViewById(R.id.back_maps_activity);
        //Sets onClickListener for button
        back_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Declares and initializes the intent
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                //Add flag that signals to remove this activity from the stack
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Start the activity
                startActivity(i);
            }
        });
    }


    /**
     * When the map is ready, the following actions will occur
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Set the map to the given google map
        mMap = googleMap;
        //Initialize the latitude-longitude object to the intent latitude and longitude
        latlng = new LatLng(intent.getDoubleExtra("latitude", 0), intent.getDoubleExtra("longitude",0));
        park_name = intent.getStringExtra("place_name");
        //Set the button listener
        save_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Declare and initialize the shared preference editor
                SharedPreferences.Editor editor = saves.edit();
                //Store the latitude, longitude, the name of the park, and the counter
                editor.putString("lat"+saves.getInt("counter", 0), Double.toString(intent.getDoubleExtra("latitude",0)));
                editor.putString("lng"+saves.getInt("counter", 0), Double.toString(intent.getDoubleExtra("longitude",0)));
                editor.putString("park_name"+saves.getInt("counter", 0), park_name);
                editor.putInt("counter", saves.getInt("counter", 0)+1);
                //Commit changes
                editor.commit();
                //Set the save map to disabled
                save_map.setEnabled(false);

            }
        });
        //Add marker to the coordinates and move the camera to those coordinates
        mMap.addMarker(new MarkerOptions().position(latlng).title("Park Chosen"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
        //Checks if the permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //Set button for finding the user to enabled
        mMap.setMyLocationEnabled(true);
    }
}