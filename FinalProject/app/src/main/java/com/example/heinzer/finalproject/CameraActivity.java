package com.example.heinzer.finalproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by duchesneaur on 11/10/2016.
 */
public class CameraActivity extends Activity {
    PlacesRetriever pr;
    SensorManager sManager;

    private Camera camera;
    private CameraPreview preview;
    final static int MY_PERMISSIONS_REQUEST_ACCESS_CAMERA = 1;

    private Place chosenPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Retrieve the places from the bundle
        chosenPlace = (Place)getIntent().getSerializableExtra("chosenPlace");
        Place place1 = (Place)getIntent().getSerializableExtra("placeList0");
        Place place2 = (Place)getIntent().getSerializableExtra("placeList1");
        Place place3 = (Place)getIntent().getSerializableExtra("placeList2");

        System.out.println("CHOSEN: " + chosenPlace);

        List<Place> places = new ArrayList<Place>();
        places.add(chosenPlace);
        places.add(place1);
        places.add(place2);
        places.add(place3);

        Collections.shuffle(places);

        Button firstChoice = (Button) findViewById(R.id.firstLoc);
        Button secondChoice = (Button) findViewById(R.id.secondLoc);
        Button thirdChoice = (Button) findViewById(R.id.thirdLoc);
        Button fourthChoice = (Button) findViewById(R.id.fourthLoc);

        firstChoice.setText(places.get(0).getName());
        secondChoice.setText(places.get(1).getName());
        thirdChoice.setText(places.get(2).getName());
        fourthChoice.setText(places.get(3).getName());

        firstChoice.setOnClickListener(new buttonListener());
        secondChoice.setOnClickListener(new buttonListener());
        thirdChoice.setOnClickListener(new buttonListener());
        fourthChoice.setOnClickListener(new buttonListener());



        for(int i = 0; i < places.size(); i++){
            System.out.println(places.get(i));
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                String str = "Explanation needed: Please I need to access your camera";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_ACCESS_CAMERA);
                String str = "No explanation needed: thanks.";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        }

        // retrieve the camera
        camera = getCameraInstance();

        // create preview view
        preview = new CameraPreview(this, camera);
        FrameLayout prevLayout = (FrameLayout)findViewById(R.id.camera_preview);
        prevLayout.addView(preview);
        OverlayView overlayView = new OverlayView(getApplicationContext());
        prevLayout.addView(overlayView);

        sManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor gyroSensor = sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sManager.registerListener(new GyroEventListener(), gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Button backButton = (Button) findViewById(R.id.camera_back);
        final Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(backIntent);
            }
        });

        Location myLocation = PlacesRetriever.getLocation();
        Location destination = new Location("");
        destination.setLatitude(chosenPlace.getLatitude());
        destination.setLongitude(chosenPlace.getLongitude());
        System.out.println(myLocation.bearingTo(destination));

    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        System.out.println(c);
        return c; // returns null if camera is unavailable
    }



    /** Check if the device has a camera */
    private boolean cameraHardwarePresent(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // camera is present
            return true;
        } else {
            // no camera
            return false;
        }
    }

    private class buttonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            final Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            Button clicked  = (Button)v;
            String chosen = (String)clicked.getText();
            System.out.println("Chose:" + chosen);
            System.out.println("Correct: " + chosenPlace.getName());

            if(chosen.equals(chosenPlace.getName())){
                clicked.setBackgroundColor(Color.GREEN);
                backIntent.putExtra("winningMessage", "Congrats You Won!\nThe place was indeed " + chosenPlace.getName());
                backIntent.putExtra("won", true);
            }
            else{
                clicked.setBackgroundColor(Color.RED);
                backIntent.putExtra("winningMessage", "Oh No! You lost! \n The correct place was " + chosenPlace.getName());
                backIntent.putExtra("won", false);
            }
            backIntent.putExtra("place", chosenPlace);

            startActivity(backIntent);
        }
    }

}
