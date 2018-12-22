package com.example.jessie.naturewalks;

//Import the following
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

public class GooglePlaceAutoCompleteTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_place_auto_complete_test);

        //Declare and initialize the auto complete fragment used for the search engine
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        //Declare and initialize the autocomplete filter
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();

        //Set the autocompletefragment filter
        autocompleteFragment.setFilter(typeFilter);
        //Set hint
        autocompleteFragment.setHint("Please select a Park");
        //Set on place selected listener
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            /**
             * When the place is selected
             * @param place
             */
            @Override
            public void onPlaceSelected(Place place) {

                //while the created counter is less than the places arraylist
                //the following actions will occur. Increment i after the action.
                for(int i =0; i<place.getPlaceTypes().size(); i++) {
                    //if the place type id equals the place type iid for park,
                    if(place.getPlaceTypes().get(i) == place.TYPE_PARK) {
                        //Declare and initialize intent to the maps activity class
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        //Set the latitude-longitude object to the place's latitude and longitude
                        LatLng latlng = place.getLatLng();
                        //Declare and initialize the place's name
                       String place_name = place.getName().toString();
                        //Store the place's name, latitude, and longitude
                        intent.putExtra("place_name", place_name);
                        intent.putExtra("latitude", latlng.latitude);
                        intent.putExtra("longitude", latlng.longitude);
                        //Start the activity
                        startActivity(intent);
                    }
                    //else if we have gone through the identifiers and 'park' is not one of those types
                    else if(i==place.getPlaceTypes().size()-1 && place.getPlaceTypes().get(i) != place.TYPE_PARK) {
                        //Give a message to choose a park
                        Toast.makeText(getApplicationContext(), "Please Choose A Park!", Toast.LENGTH_LONG).show();
                    }
                }

            }

            /**
             * When there is an error, the following actions will occur
             * @param status
             */
            @Override
            public void onError(Status status) {
                //Give message on the error
                Log.i("ERROR", "An error occurred: " + status);
            }
        });
    }
}
