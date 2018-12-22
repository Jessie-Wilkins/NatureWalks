package com.example.jessie.naturewalks;

//Import the following
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter {

    //Creates an activity and to strings to be used for the list adapter and View
    private Activity context_;
    private String[] parks_;

    /**
     * ListAdapter that implements the pictures and names of countries
     * @param context
     * @param parks
     */
    public ListAdapter(Activity context,  String[] parks) {
        //Sets constructor
        super(context, R.layout.linear_row, parks);

        //Sets the strings and activity equal to the given variables
        context_ = context;
        this.parks_ = parks;

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
            holder.tv_link.setText(parks_[position]);

            //Sets the tag of the convert view
            convertView.setTag(holder);

        }//ends if statement

        //if no other condition is satisfied
        else {
            //Sets holder to text of the current position of the string array
            holder.tv_link.setText(parks_[position]);

        }

        //Returns the view
        return convertView;
    }

    /**
     * Class that uses TextView and Image View
     */
    private class ViewHolder{
        //Creates TextView
        TextView tv_link;

        /**
         * Sets the text view and image view objects to the corresponding linear_row xml views
         * @param row
         */
        public ViewHolder(View row){
            tv_link = (TextView) row.findViewById(R.id.text_link);
        }
    }
}
