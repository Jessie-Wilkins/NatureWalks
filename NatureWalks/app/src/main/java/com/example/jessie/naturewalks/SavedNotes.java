package com.example.jessie.naturewalks;

//Import the following
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class SavedNotes extends AppCompatActivity {
    //Declare listview object
    ListView list;
    //Declare shared preferences
    SharedPreferences saves;
    //Declares the file array list statically
    static ArrayList<File> picture_files_arraylist;
    //Declares the String array lists for the notes,
    //latitudes, and longitudes.
    ArrayList<String> note_arraylist ;
    ArrayList<String> lat_arraylist;
    ArrayList<String> lng_arraylist;
    //Declares string array for notes
    String [] note_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_notes);

        //Initialize shared preferences
        saves = PreferenceManager.getDefaultSharedPreferences(this);
        //Initialize list view object
        list = (ListView) findViewById(R.id.listview2);

        //Initialize the file arraylist and the string array lists
        picture_files_arraylist = new ArrayList<>();
        note_arraylist = new ArrayList<>();
        lat_arraylist = new ArrayList<>();
        lng_arraylist = new ArrayList<>();

        //Declare and initialize a string of the file path to the
        //given path file
        String pth_strng = Environment.getExternalStorageDirectory().toString()+"/Android/data/com.example.jessie.naturewalks/files";
        //Declare and initialize the file path using the string of the file path
        File path = new File(pth_strng);
        //Declare and initialize the file array to the file path's
        //list of files within the given directory.
        File[] files = path.listFiles();
        //Show the list of files
        showFiles(files);
        //While i is less than the length of the file array list,
        //the following actions will occur. i will increment.
        for(int i= 0; i<picture_files_arraylist.size(); i++) {
            //if the shared preference note is not null, then it will be added to the
            //note arraylist
            if(saves.getString(picture_files_arraylist.get(i).getAbsolutePath(), null) != null) {
                note_arraylist.add(saves.getString(picture_files_arraylist.get(i).getAbsolutePath(), null));
            }
            //if the shared preference latitude is not null, then it will be added to the
            //latitude array list
            if(saves.getString("Pic_Lat_for"+picture_files_arraylist.get(i).getAbsolutePath(), null) != null) {
                lat_arraylist.add(saves.getString("Pic_Lat_for"+picture_files_arraylist.get(i).getAbsolutePath(), null));
            }
            //if the shared preference longitude is not null, then it will be added to the
            //longitude array list
            if(saves.getString("Pic_Lng_for"+picture_files_arraylist.get(i).getAbsolutePath(), null) != null) {
                lng_arraylist.add(saves.getString("Pic_Lng_for"+picture_files_arraylist.get(i).getAbsolutePath(), null));
            }

        }

        //Set the note array to the file arra list size
        note_array = new String[picture_files_arraylist.size()];

        //Set the note array to the contents of the note
        //array list
        note_arraylist.toArray(note_array);

        //Declare and initialize the adapter for the list
        ListAdapter2 adapter = new ListAdapter2(this, note_array, picture_files_arraylist, lat_arraylist, lng_arraylist);

        //Set the adapter for the list
        list.setAdapter(adapter);

        //Set the divider height between each object
        list.setDividerHeight(20);

        //Set the item click listener for the list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Declare and initialize the intent to the maps activity class
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                //Store the latitude and longitude into the intent
                intent.putExtra("latitude", Double.parseDouble(saves.getString("Pic_Lat_for"+picture_files_arraylist.get(position).getAbsolutePath(),null)));
                intent.putExtra("longitude", Double.parseDouble(saves.getString("Pic_Lng_for"+picture_files_arraylist.get(position).getAbsolutePath(), null)));
                //start activity
                startActivity(intent);
            }
        });//ends click method and class
    }

    /**
     * Adds the files from the given directory to the array list
     * @param files
     */
    public static void showFiles(File[] files) {
        //if the file is null
        if (files != null) {
            //while i is less than the length of the
            //file array, it will do the following actions
            for (int i = 0; i < files.length; i++) {
                //if the the file array location is a directory,
                //call the method again.
                if (files[i].isDirectory()) {

                    showFiles(files[i].listFiles());
                }
                //else add the file to the array list
                else {

                    picture_files_arraylist.add(files[i].getAbsoluteFile());
                }
            }
        }
    }
}