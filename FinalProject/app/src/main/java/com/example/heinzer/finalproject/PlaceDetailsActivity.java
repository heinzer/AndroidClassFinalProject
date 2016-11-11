package com.example.heinzer.finalproject;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlaceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        Button backButton = (Button) findViewById(R.id.backbutton);
        backButton.getBackground().setColorFilter(0xFFcc4b69, PorterDuff.Mode.MULTIPLY);
        final Intent backIntent = new Intent(this, PlacesListActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(backIntent);
            }
        });


        //pull the extra data from the intent
        Intent i = getIntent();
        Place place = (Place) i.getSerializableExtra("place");

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(place.getName());

        TextView address = (TextView) findViewById(R.id.address);
        address.setText(place.getAddress());
        System.out.println(place.getAddress());

        TextView latitude = (TextView) findViewById(R.id.latitude);
        latitude.setText("" + place.getLatitude());

        TextView longitude = (TextView) findViewById(R.id.longitude);
        longitude.setText("" + place.getLongitude());
    }

    public String getPlaceDetail(){
        //Make the call for the place's detail
        return "";
    }
}
