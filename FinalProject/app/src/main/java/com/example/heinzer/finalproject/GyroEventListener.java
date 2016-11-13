package com.example.heinzer.finalproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by duchesneaur on 11/12/2016.
 */
public class GyroEventListener implements SensorEventListener {

    /** The last x position. */
    private float lastX = 0;

    /** The last y position. */
    private float lastY = 0;

    /** The last z position. */
    private float lastZ = 0;

    /** How many movements are considered so far. */
    private int mDirectionChangeCount = 0;

    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     */
    private static final int MIN_DIRECTION_CHANGE = 3;


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[SensorManager.DATA_X];
        float y = event.values[SensorManager.DATA_Y];
        float z = event.values[SensorManager.DATA_Z];

        System.out.println("----------");

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        System.out.println(degree);
        OverlayView.updateText(""+degree);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("BEARING CHANGED");

    }
}
