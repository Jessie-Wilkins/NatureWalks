package com.example.jessie.naturewalks;

//Import the following
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;


public class TestJSONActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    //Variable used to track the speed
    float speed;

    //Declares the location listener
    android.location.LocationListener listen;
    //Declares the location manager object
    LocationManager l;
    //Declares the textview object used to display the speed measurement
    TextView speed_measure;
    //Declares the google map
    GoogleMap map;

    //Declares button for taking picture
    Button take_picture;
    //Declares the location object
    Location loc;

    //TAG used for activity logs
    private static final String TAG = TestJSONActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_json);

        //Initialize the TextView object
        speed_measure = (TextView) findViewById(R.id.speed_measure);

        //Sets speed to 0;
        speed = 0;

        //Initializes the location manager object to location service
        l = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Initializes the take picture button
        take_picture = (Button) findViewById(R.id.take_picture);

        //Sets on click listener of take picture button
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates and sets intent to camera class
                Intent intent = new Intent(getApplicationContext(), CameraTest.class);

                //if the location object equals null
                if (loc == null) {
                    //Checks if the permissions for the location are granted
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    //Sets the location object to the last location
                    loc = l.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                //Puts latitude into the intent
                intent.putExtra("Pic_Lat", loc.getLatitude());
                //Puts longitude into the intent
                intent.putExtra("Pic_Lng", loc.getLongitude());
                //Puts class number into the intent
                intent.putExtra("Class", 2);
                //Starts the activity
                startActivity(intent);
            }
        });

        //Declares and initializes the mapFragment
        final SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        //Gets the map async
        mapFragment.getMapAsync(this);

        //Checks if their is permission to use location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    /**
     * When ready to use, the map will be changed or used to do certain actions.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //Google Map object is set is set to google map
        map = googleMap;

        //Checks whether there is permission for using location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //Sets find my loctation button to visible or enabled
        map.setMyLocationEnabled(true);

        //Tries the google maps style json file
        try {
            //Boolean used to see if the file can be  loaded
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.google_maps_json));

            //If it is not successful, give the following message
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        }
        //Catches the exception and gives the message
        catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        //Sets the location listener object
        listen = new android.location.LocationListener() {
            /**
             * Does the following actions after the location is changed
             * @param location
             */
            @Override
            public void onLocationChanged(Location location) {
                //Sets speed variable to the speed
                speed = location.getSpeed();
                //Sets text to that speed
                speed_measure.setText(Float.toString(speed)+ " m/s");
                //Declares and initializes the latitude-longitude to the given latitude and longitude
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //Declares and initializes the camera position object and builds it according to the given
                //attributes
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(18)
                        .bearing(90)
                        .tilt(10)
                        .build();
                        //Moves map camera according to the given camera position
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                //Sets location object to the location
                loc = location;
            }

            /**
             * Does Nothing.
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
        //Requests the location to be updated
        l.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, (long) 0, (float) 0, listen);
    }

    /**
     *When the activity is paused, it does these actions.
     */
    @Override
    public void onPause() {
        //Calls super
        super.onPause();

        //Checks if the location permissions are given
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Main", "Permission NOT granted");
            return;
        }
        //else if the listener is not null, remove the listener updates.
        else if(listen != null) {
            l.removeUpdates(listen);
        }
    }
}
