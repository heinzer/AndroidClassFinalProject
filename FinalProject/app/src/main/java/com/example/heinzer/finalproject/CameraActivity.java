package com.example.heinzer.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

/**
 * Created by duchesneaur on 11/10/2016.
 */
public class CameraActivity extends Activity {
    PlacesRetriever pr;

    private Camera camera;
    private CameraPreview preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        pr = new PlacesRetriever();
        pr.askPermission(this);

        // retrieve the camera
        camera = getCameraInstance();

        // create preview view
        preview = new CameraPreview(this, camera);
        FrameLayout prevLayout = (FrameLayout)findViewById(R.id.camera_preview);
        prevLayout.addView(preview);

        Button backButton = (Button) findViewById(R.id.camera_back);
        final Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(backIntent);
            }
        });


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
