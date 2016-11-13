package com.example.heinzer.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class MainActivity extends Activity {
    PlacesRetriever pr;
    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;

    private Place currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        Place place = new Place();
        place.setLatitude(2.2);
        place.setLongitude(2.3);
        place.setName("Derp Place");
        place.setAddress("123 Derp Lane");

        this.getIntent().putExtra("winningStatus", "You Won!");
        this.getIntent().putExtra("placeDetails", place);*/

        final Button startGameButton = (Button) findViewById(R.id.startgame);
        //Check to see if any extras are sent back from the game
        if(this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();

            LinearLayout ll = (LinearLayout) findViewById(R.id.test);
            ll.setGravity(Gravity.BOTTOM);

            TextView winningStatus = (TextView) findViewById(R.id.winningStatus);
            winningStatus.setText(bundle.getString("winningMessage"));

            currentPlace = (Place) this.getIntent().getSerializableExtra("place");

            TextView placeDetails = (TextView) findViewById(R.id.placeDetails);
            placeDetails.setText(currentPlace.getFullDetails());

            if(this.getIntent().getBooleanExtra("won", false)){
                winningStatus.setTextColor(Color.GREEN);
            }else{
                winningStatus.setTextColor(Color.RED);
            }
            if(currentPlace.getPhotoReference()!= null){
                getImage(currentPlace.getPhotoReference());
            }


            startGameButton.setText("Start New Game");
            TextView gestureNote = (TextView) findViewById(R.id.gestureNote);
            gestureNote.setText("Shake to add location to saved places");
        }


        startGameButton.getBackground().setColorFilter(0xFF5db0ba, PorterDuff.Mode.MULTIPLY);
        final Intent startGameIntent = new Intent(this, CameraActivity.class);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameButton.setText(getResources().getString(R.string.loading));
                pr.choosePlaces();
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

    private void getImage(String placeId){
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+placeId+"&key=AIzaSyAYXpuEkh14deoc_ELfoHmQiCGUROT1py4";
        System.out.println("Finding Image: " + url);
        final ImageView img = (ImageView) findViewById(R.id.img);
        new GetImage(this){
            @Override
            protected void onPostExecute(Bitmap bitmap){
                System.out.println("In post execute");
                if(bitmap != null){
                    System.out.println("Not Null");

                    img.setImageBitmap(bitmap);
                    System.out.println("Image should be set");

                }
            }

        }.execute(url);
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
