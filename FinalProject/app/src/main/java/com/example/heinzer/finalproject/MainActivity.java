package com.example.heinzer.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    PlacesRetriever pr;
    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;

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
                pr.choosePlaces();
                startGameButton.setText(getResources().getString(R.string.loading));
                startGameIntent.putExtra("chosenPlace", pr.getChosenPlace());
                List<Place> places = pr.getPlacesForGame();
                System.out.println("SIZE: " + places.size());
                for(int i = 0; i < places.size(); i++){
                    String name = "placeList" + i;
                    System.out.println(name + " : " + places.get(i));
                    startGameIntent.putExtra(name, places.get(i));
                }
                startActivity(startGameIntent);
            }
        });

        Button placesButton = (Button) findViewById(R.id.placeslist);
        placesButton.getBackground().setColorFilter(0xFF5db0ba, PorterDuff.Mode.MULTIPLY);
        final Intent placesIntent = new Intent(this, PlacesListActivity.class);
        Bundle data = new Bundle();

        placesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(placesIntent);
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(MainActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reset the text on the button to say 'start game'
        final Button startGameButton = (Button) findViewById(R.id.startgame);
        startGameButton.setText(getResources().getString(R.string.start));

        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }


//    private void printLocations(){
//        List<HashMap<String, String>> nearbyPlacesList =  pr.getplaces();
//        for (int i = 0; i < nearbyPlacesList.size(); i++) {
//            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
//            double lat = Double.parseDouble(googlePlace.get("lat"));
//            double lng = Double.parseDouble(googlePlace.get("lng"));
//            String placeName = googlePlace.get("place_name");
//            String vicinity = googlePlace.get("vicinity");
//            LatLng latLng = new LatLng(lat, lng);
//            System.out.println("placeName: " + placeName);
//        }
//    }
}
