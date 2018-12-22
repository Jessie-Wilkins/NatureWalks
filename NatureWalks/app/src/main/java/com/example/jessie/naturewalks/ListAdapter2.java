package com.example.jessie.naturewalks;

//Import the following
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListAdapter2 extends ArrayAdapter {

        //Creates an activity and to strings to be used for the list adapter and View
        private Activity context_;
        private String[] notes_;
        private ArrayList<File> pictures_;
        private ArrayList<String> lat_;
        private ArrayList<String> lng_;

        /**
         * ListAdapter that implements the pictures and names of countries
         * @param context
         */
        public ListAdapter2(Activity context, String[] notes, ArrayList<File> pictures, ArrayList<String> lat, ArrayList<String> lng) {
            //Sets constructor
            super(context, R.layout.linear_row, notes);

            //Sets the strings and activity equal to the given variables
            context_ = context;
            this.notes_ = notes;
            pictures_ = pictures;
            lat_ = lat;
            lng_ = lng;

        }

        /**
         * Sets view of the list
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            //if the view is null
            if (convertView == null) {
                //Creates and sets inflater equal to the activity's layout inflater
                LayoutInflater inflater = context_.getLayoutInflater();
                //Inflates the view with the linear_row xml
                convertView = inflater.inflate(R.layout.linear_row, null);
            }

            //Creates and sets the holder to the tag of the convertView
            ViewHolder holder = (ViewHolder) convertView.getTag();

            //if the holder is null
            if(holder == null) {
                //Sets the holder to the convertView's ViewHolder
                holder = new ViewHolder(convertView);
                //Sets the text of the holder's text view
                holder.tv_link.setText(notes_[position]);

                //Declare and initialize the geocoder used for getting the address
                Geocoder geocoder = new Geocoder(context_, Locale.getDefault());
                //Declare and initialize the address list
                List<Address> addresses;
                //Set the addresses to null
                addresses = null;
                //Tries the address
                try {
                    //if the position is less than the latitude arraylist size
                    //and the position is less than the longitude arraylist size
                    //Set the address to the address with the given latitude and longitude
                    if(position <lat_.size() && position< lng_.size()) {
                        addresses = geocoder.getFromLocation(Double.parseDouble(lat_.get(position).toString()), Double.parseDouble(lng_.get(position).toString()), 1);
                    }

                }
                //Catches exception and prints out the stack trace
                catch (IOException e) {
                    e.printStackTrace();
                }

                //Declare known name string
                String knownName;
                //if the address list is null
                if(addresses != null) {
                    //Set the known name variable to the address
                    knownName = addresses.get(0).getAddressLine(0);
                    //Set the holder location text view to the
                    //known name.
                    holder.loc_link.setText(knownName);
                }



                //Sets the image view to the given picture
                holder.imageView.setImageBitmap(setPic(holder.imageView, pictures_.get(position).getAbsolutePath()));

                //Sets the tag of the convert view
                convertView.setTag(holder);

            }//ends if statement

            //if no other condition is satisfied
            else {
                //Sets holder to text of the current position of the string array
                holder.tv_link.setText(notes_[position]);

                //Sets the image view to the given picture
                holder.imageView.setImageBitmap(setPic(holder.imageView, pictures_.get(position).getAbsolutePath()));

            }

            //Returns the view
            return convertView;
        }

        /**
         * Class that uses TextView and Image View
         */
        private class ViewHolder{
            //Creates TextView and ImageView objects
            TextView tv_link;
            TextView loc_link;
            ImageView imageView;

            /**
             * Sets the text views and image view objects to the corresponding linear_row xml views
             * @param row
             */
            public ViewHolder(View row){
                tv_link = (TextView) row.findViewById(R.id.text_link);
                loc_link = (TextView) row.findViewById(R.id.loc_link);
                imageView = (ImageView) row.findViewById(R.id.img);
            }
        }

    /**
     * Creates the optimized photo
     * @param mImageView
     * @param mCurrentPhotoPath
     * @return
     */
        private Bitmap setPic(ImageView mImageView, String mCurrentPhotoPath) {
            //Declares the target width and target height
            int targetW;
            int targetH;

            //if the image width and height are 0,
            //set the target width and height both to 1
            if(mImageView.getWidth() ==0 || mImageView.getHeight() ==0) {
                targetW =1;
                targetH =1;
            }
            //else set the target width and height
            //to the imageview width and height
            else{
                targetW = mImageView.getWidth();
                targetH = mImageView.getHeight();
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

            //Sets the bounds to false, the size to the scaled
            //size, and the purgeable option to true
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            //Declare and initialize the bit map to
            //the bitmap factory's decoded file and
            //returns the bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            return bitmap;
        }

    }

