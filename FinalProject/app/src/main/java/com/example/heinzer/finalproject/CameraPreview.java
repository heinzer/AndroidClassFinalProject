package com.example.heinzer.finalproject;

import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by duchesneaur on 11/10/2016.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    // Holds the preview
    private SurfaceHolder mHolder;
    // Camera to be used
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated but required on Android 3.0 and less
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // Tell the camera where to draw the preview on the surface.
        if(mCamera != null){
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
            } catch (IOException e) {
                System.out.println("Error setting camera preview: " + e.getMessage());
            }
        } else{
            String str = "Unable to get camera preview";
            Toast.makeText(this.getContext(), str, Toast.LENGTH_SHORT).show();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Make sure to stop the preview and release the camera
        // or else itll crash when you try to open the camera again
        mCamera.stopPreview();
        mCamera.setPreviewCallback(null);
        mCamera.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview doesn't exist. there's nothing to do
        }
        else {

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e) {
                System.out.println("Error starting camera preview: " + e.getMessage());
            }
        }
    }
}
