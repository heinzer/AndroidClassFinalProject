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
            System.out.println("GetNearbyPlacesData"+ "doInBackground entered");
            url = (String) params[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            System.out.println("GooglePlacesReadTask"+ "doInBackground Exit");
        } catch (Exception e) {
            System.out.println("GooglePlacesReadTask"+ e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("GooglePlacesReadTask"+ "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        PlaceDataParser dataParser = new PlaceDataParser();
        nearbyPlacesList =  dataParser.parse(result);
        pr.setNearbyPlaces(nearbyPlacesList);
        System.out.println("GooglePlacesReadTask"+ "onPostExecute Exit");
    }


}
