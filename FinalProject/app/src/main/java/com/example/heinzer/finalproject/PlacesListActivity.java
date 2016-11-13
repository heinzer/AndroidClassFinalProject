package com.example.heinzer.finalproject;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class PlacesListActivity extends ListActivity {
    private PlaceDataSource dataSource;
    private List<Place> placeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        //Check to see if toast message exists
        if(this.getIntent().getExtras() != null){
            Toast.makeText(this, this.getIntent().getExtras().getString("value"), Toast.LENGTH_SHORT).show();
        }

        Button mainMenuButton = (Button) findViewById(R.id.mainmenu);
        mainMenuButton.getBackground().setColorFilter(0xFFcc4b69, PorterDuff.Mode.MULTIPLY);
        final Intent mainMenuIntent = new Intent(this, MainActivity.class);
        mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(mainMenuIntent);
            }
        });

        //Get a list of places
        ListView listView = getListView();
        //set onclick for each list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent placeDetailsIntent = new Intent(getBaseContext(), PlaceDetailsActivity.class);
                placeDetailsIntent.putExtra("place", placeList.get(position));
                //redirect to new activity with info/ id
                startActivity(placeDetailsIntent);
            }
        });

        dataSource = new PlaceDataSource(this);
        dataSource.open();
        placeList = dataSource.getAllPlaces();
        ArrayAdapter<Place> adapter = new ArrayAdapter<Place>(this, android.R.layout.simple_list_item_1, placeList);
        setListAdapter(adapter);
        dataSource.close();
    }
}
