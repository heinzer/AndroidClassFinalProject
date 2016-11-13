package com.example.heinzer.finalproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by duchesneaur on 11/12/2016.
 */
public class OverlayView extends View implements SensorEventListener{

    public static final String DEBUG_TAG = "OverlayView Log";
    String accelData = "Accelerometer Data";
    String compassData = "Compass Data";
    String gyroData = "Gyro Data";

    public OverlayView(Context context) {
        super(context);
    }

        @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Paint contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        contentPaint.setTextAlign(Paint.Align.CENTER);
//        contentPaint.setTextSize(50);
//        contentPaint.setColor(Color.RED);
//        canvas.drawText(accelData, canvas.getWidth()/2, canvas.getHeight()/4, contentPaint);
//        canvas.drawText(compassData, canvas.getWidth()/2, canvas.getHeight()/2, contentPaint);
//        canvas.drawText(gyroData, canvas.getWidth()/2, (canvas.getHeight()*3)/4, contentPaint);
    }

    public static void updateText(String text){
        System.out.println(text);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
