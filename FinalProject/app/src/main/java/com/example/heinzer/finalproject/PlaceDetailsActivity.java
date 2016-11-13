package com.example.heinzer.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceDetailsActivity extends AppCompatActivity {

    private PlaceDataSource dataSource;
    private Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        dataSource = new PlaceDataSource(this);


        Button backButton = (Button) findViewById(R.id.backbutton);
        backButton.getBackground().setColorFilter(0xFFcc4b69, PorterDuff.Mode.MULTIPLY);
        final Intent backIntent = new Intent(this, PlacesListActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(backIntent);
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deletebutton);
        deleteButton.getBackground().setColorFilter(0xFFE32636, PorterDuff.Mode.MULTIPLY);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteItem();
                startActivity(backIntent);
            }
        });


        //pull the extra data from the intent
        Intent i = getIntent();
        place = (Place) i.getSerializableExtra("place");

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(place.getName());

        TextView address = (TextView) findViewById(R.id.address);
        address.setText(place.getAddress());
        System.out.println(place.getAddress());

        TextView latitude = (TextView) findViewById(R.id.latitude);
        latitude.setText("Latitude: " + place.getLatitude());

        TextView longitude = (TextView) findViewById(R.id.longitude);
        longitude.setText("Longitude: " + place.getLongitude());

        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+place.getPhotoReference()+"&key=AIzaSyAYXpuEkh14deoc_ELfoHmQiCGUROT1py4";
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

    public String getPlaceDetail(){
        //Make the call for the place's detail
        return "";
    }

    private void deleteItem(){
        dataSource.open();
        dataSource.deletePlace(place);
        dataSource.close();
    }
}
