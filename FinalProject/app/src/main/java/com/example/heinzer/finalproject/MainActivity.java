package com.example.heinzer.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGameButton = (Button) findViewById(R.id.startgame);
        startGameButton.getBackground().setColorFilter(0xFF5db0ba, PorterDuff.Mode.MULTIPLY);
        final Intent startGameIntent = new Intent(this, CameraActivity.class);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    }
}
