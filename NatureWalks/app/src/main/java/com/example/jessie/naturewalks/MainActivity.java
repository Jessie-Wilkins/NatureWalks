/* ========================================================================== */
/*	NatureWalks

    AUTHOR: Jessie Wilkins
    COURSE NUMBER: CIS 472
    COURSE SECTION NUMBER: 01
    INSTRUCTOR NAME: Dr. Tian
    Individual Project
    DUE DATE: 5/5/2017

SUMMARY

	NatureWalks is a nature oriented app used to search for parks, look at current locations, and take pictures with notes.
	It can save parks and pictures with notes; these can be looked at within the app.
INPUT

The input for this program will be user input (onClick, listOnClick, EditText, and map interaction).


OUTPUT

Output is mainly in the form of showing maps and pictures.

ASSUMPTIONS
- No one wants to edit their note entries.

*/

package com.example.jessie.naturewalks;

//Import the following
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets Button used to go to the main menu
        Button welcome_btn = (Button) findViewById(R.id.welcome_btn);

        //Sets the button listener
        welcome_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sets the intent to the main menu and starts that activity
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
            }
        });
    }
}
