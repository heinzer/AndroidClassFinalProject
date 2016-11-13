package com.example.heinzer.finalproject;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hornl on 11/10/2016.
 */
public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    String url;
    PlacesRetriever pr = null;

    protected void setretriever(PlacesRetriever pr){
        this.pr = pr;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            url = (String) params[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (Exception e) {
            System.out.println("GooglePlacesReadTask: "+ e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        List<Place> nearbyPlacesList = null;
        PlaceDataParser dataParser = new PlaceDataParser();
        nearbyPlacesList =  dataParser.parse(result);
        pr.setNearbyPlaces(nearbyPlacesList);
    }


}
