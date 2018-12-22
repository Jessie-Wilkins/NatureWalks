package com.example.jessie.naturewalks;

//Import the following
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SavedParks extends AppCompatActivity {
    //Declare shared preferences
    SharedPreferences saves;
    //Declare list view object
    ListView list;
    //Declare the string array lists for the park name,
    //latitude, and the longitude
    ArrayList<String> parks_arraylist;
    ArrayList<String> lat_arraylist;
    ArrayList<String> lng_arraylist;
    //Declare the string arrays for the park name,
    //latitude, and the longitude
    String [] parks_array;
    String [] lat_array;
    String [] lng_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_parks);

        //Initialize the array lists
        parks_arraylist = new ArrayList<>();
        lat_arraylist = new ArrayList<>();
        lng_arraylist = new ArrayList<>();

        //Initialize the shared preference
        saves = PreferenceManager.getDefaultSharedPreferences(this);

        //Sets list equal to the ListView xml of the given name
        list = (ListView) findViewById(R.id.listview);

        //While i is less than or equal to the shared preference
        //integer, do the following actions. increments i every time.
        for(int i = 0; i<=saves.getInt("counter",0); i++) {
            //Add the the park name, latitude, and longitude
            // to the corresponding array lists
            parks_arraylist.add(saves.getString("park_name"+i, null));
            lat_arraylist.add(saves.getString("lat"+i, null));
            lng_arraylist.add(saves.getString("lng"+i, null));
        }
        //Set the arrays to the corresponding array list's size
        parks_array = new String[parks_arraylist.size()];
        lat_array = new String[lat_arraylist.size()];
        lng_array = new String[lng_arraylist.size()];

        //Set the arrays to the corresponding array list
        parks_arraylist.toArray(parks_array);
        lat_arraylist.toArray(lat_array);
        lng_arraylist.toArray(lng_array);
        //Declare and initialize the adapter
        ListAdapter adapter = new ListAdapter(this, parks_array);


        //Sets the list adapter and sets a division between each item in the list
        list.setAdapter(adapter);
        list.setDividerHeight(20);

        //Produces an event when the list is clicked
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Declares and initializes the intent to the maps activity class
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                //Stores the latitude and longitude into the intent.
                intent.putExtra("latitude", Double.parseDouble(saves.getString("lat"+position,null)));
                intent.putExtra("longitude", Double.parseDouble(saves.getString("lng"+position, null)));
                //start activity
                startActivity(intent);
            }
        });//ends click method and class

    }
}
