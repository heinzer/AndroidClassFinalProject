package com.example.heinzer.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by duchesneaur on 11/10/2016.
 */
public class CameraActivity extends Activity {

    private Camera camera;
    private CameraPreview preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

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
}
