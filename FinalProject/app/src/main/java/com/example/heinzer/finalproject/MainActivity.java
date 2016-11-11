package com.example.heinzer.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    PlacesRetriever pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pr = new PlacesRetriever();
        pr.askPermission(this);

        final Button startGameButton = (Button) findViewById(R.id.startgame);
        startGameButton.getBackground().setColorFilter(0xFF5db0ba, PorterDuff.Mode.MULTIPLY);
        final Intent startGameIntent = new Intent(this, CameraActivity.class);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameButton.setText(getResources().getString(R.string.loading));
                startActivity(startGameIntent);
            }
        });

        Button placesButton = (Button) findViewById(R.id.placeslist);
        placesButton.getBackground().setColorFilter(0xFF5db0ba, PorterDuff.Mode.MULTIPLY);
        final Intent placesIntent = new Intent(this, PlacesListActivity.class);
        placesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(placesIntent);
            }
        });

    }


    private void printLocations(){
        List<HashMap<String, String>> nearbyPlacesList =  pr.getplaces();
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            System.out.println("placeName: " + placeName);
        }
    }
}
