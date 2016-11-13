package com.example.heinzer.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.heinzer.finalproject.R;


import java.util.List;

public class MainActivity extends Activity {
    PlacesRetriever pr;
    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;

    private Place currentPlace;

    private PlaceDataSource dataSource;

    private boolean isPlaceAdded;

    private Handler h = new Handler();
    private int delay = 3000;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isNetworkAvailable()){
            Toast.makeText(MainActivity.this, "An internet connection is required", Toast.LENGTH_SHORT).show();
            return;
        }

        pr = new PlacesRetriever();
        pr.askPermission(this);
        isPlaceAdded = false;


/*        Place place = new Place();
        place.setLatitude(2.2);
        place.setLongitude(2.3);
        place.setName("Derp Place");
        place.setAddress("123 Derp Lane");

        this.getIntent().putExtra("winningStatus", "You Won!");
        this.getIntent().putExtra("placeDetails", place);*/
        dataSource = new PlaceDataSource(this);

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

            final Button startGameButton = (Button) findViewById(R.id.startgame);

            startGameButton.setText("Start New Game");

            TextView gestureNote = (TextView) findViewById(R.id.gestureNote);
            gestureNote.setText("Shake to add location to saved places");
        }


        final Button startGameButton = (Button) findViewById(R.id.startgame);

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
                dataSource.open();
                if(currentPlace != null && !isPlaceAdded) {
                    dataSource.addPlaceToDatabase(currentPlace);
                    isPlaceAdded = true;
                    Toast.makeText(MainActivity.this, "Place Added", Toast.LENGTH_SHORT).show();
                }
                dataSource.close();
            }
        });

        checkforReady();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reset the text on the button to say 'start game'
        final Button startGameButton = (Button) findViewById(R.id.startgame);
        startGameButton.setText(getResources().getString(R.string.start));
        if(isNetworkAvailable()) {
            pr.askPermission(this);
            checkforReady();

            mSensorManager.registerListener(mSensorListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        try {
            h.removeCallbacks(runnable);
        }catch(Exception e){
            //Do nothing?
        }
        super.onPause();
    }

    private void getImage(String placeId){
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+placeId+"&key=AIzaSyAYXpuEkh14deoc_ELfoHmQiCGUROT1py4";
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkforReady(){
        final Button startGameButton = (Button) findViewById(R.id.startgame);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable = this;
                System.out.println("The runnable is going. Checking the pr status");

                if(!pr.isDataReady()){
                    startGameButton.setClickable(false);
                    System.out.println("It is not ready");
                    h.removeCallbacks(runnable);
                }else{
                    startGameButton.setClickable(true);
                    System.out.println("It is ready!!!!");

                }
            }
        }, delay);
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
