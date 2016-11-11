package com.example.heinzer.finalproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.hardware.Camera;
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

import java.util.HashMap;
import java.util.List;

/**
 * Created by duchesneaur on 11/10/2016.
 */
public class CameraActivity extends Activity {
    PlacesRetriever pr;

    private Camera camera;
    private CameraPreview preview;
    final static int MY_PERMISSIONS_REQUEST_ACCESS_CAMERA = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Place chosenPlace = (Place)getIntent().getSerializableExtra("placeChosen");
        System.out.println(chosenPlace);

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

//    private void printLocations(){
//        List<Place> nearbyPlacesList =  pr.getplaces();
//        for (int i = 0; i < nearbyPlacesList.size(); i++) {
//            Place googlePlace = nearbyPlacesList.get(i);
//            double lat = googlePlace.getLatitude();
//            double lng = googlePlace.getLongitude();
//            String placeName = googlePlace.getName();
//            String vicinity = googlePlace.getVicinity();
//            LatLng latLng = new LatLng(lat, lng);
//            System.out.println("placeName: " + placeName);
//        }
//    }
}
