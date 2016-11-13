package com.example.heinzer.finalproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hornl on 11/10/2016.
 */
public class PlaceDataParser {
    public List<Place> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<Place> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<Place> placesList = new ArrayList<>();
        Place newPlace = null;

        for (int i = 0; i < placesCount; i++) {
            try {
                newPlace = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(newPlace);

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private Place getPlace(JSONObject googlePlaceJson) {
        Place googlePlaceMap = new Place();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String placeId = "";
        String photo_Reference ="";

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if (!googlePlaceJson.isNull("id")) {
                placeId= googlePlaceJson.getString("id");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            if (!googlePlaceJson.isNull("photos")) {
                photo_Reference = googlePlaceJson.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            }
            googlePlaceMap.setName(placeName);
            googlePlaceMap.setVicinity(vicinity);
            googlePlaceMap.setPlaceId(placeId);
            googlePlaceMap.setPhotoReference(photo_Reference);
            googlePlaceMap.setLatitude(Double.parseDouble(latitude));
            googlePlaceMap.setLongitude(Double.parseDouble(longitude));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}
