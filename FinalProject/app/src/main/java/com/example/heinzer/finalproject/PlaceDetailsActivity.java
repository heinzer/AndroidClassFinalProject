package com.example.heinzer.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PlaceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        //pull the extra data from the intent
        Intent i = getIntent();
        Place place = (Place) i.getSerializableExtra("place");

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(place.getName());
        TextView address = (TextView) findViewById(R.id.address);
        address.setText(place.getAddress());
        TextView latitude = (TextView) findViewById(R.id.latitude);
        latitude.setText("" + place.getLatitude());
        TextView longitude = (TextView) findViewById(R.id.longitude);
        longitude.setText("" + place.getLongitude());

        TextView detail = (TextView) findViewById(R.id.detail);
        String detailText = getPlaceDetail();
        detail.setText(detailText);
    }

    public String getPlaceDetail(){
        //Make the call for the place's detail
        return "";
    }
}
