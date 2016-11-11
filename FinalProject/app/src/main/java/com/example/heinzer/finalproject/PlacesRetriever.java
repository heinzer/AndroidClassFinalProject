package com.example.heinzer.finalproject;

import android.util.Log;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by hornl on 11/10/2016.
 */
public class PlacesRetriever {
    final static int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    private LocationManager locationManager;
    private Criteria criteria;

    private int PROXIMITY_RADIUS = 10000;
    private Location latLocation;

    private String locationsTypes = "restaurant";
    List<HashMap<String, String>> nearbyPlacesList = null;
    private List<HashMap<String, String>> chosenPlaces = null;
    private HashMap<String, String> chosenPlace = null;

    /**
     * Returns the User's current Location
     * @return user's location
     */
    public Location getLocation(){
        return latLocation;
    }

    /**
     * Returns the List of places within the specified radius! parse method:
     * for (int i = 0; i < nearbyPlacesList.size(); i++) {
     HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
     double lat = Double.parseDouble(googlePlace.get("lat"));
     double lng = Double.parseDouble(googlePlace.get("lng"));
     String placeName = googlePlace.get("place_name");
     String vicinity = googlePlace.get("vicinity");
     LatLng latLng = new LatLng(lat, lng);
     }
     * @return The list
     */
    public List<HashMap<String, String>> getplaces(){
        return nearbyPlacesList;
    }

    public void choosePlaces(){
        Random rand = new Random();
        chosenPlaces = new ArrayList<>();
        ArrayList<Integer> nums = new ArrayList<Integer>();

        if(nearbyPlacesList.size()>4) {
            int randomNum = rand.nextInt(nearbyPlacesList.size()-1);
            nums.add(randomNum);
            chosenPlaces.add(nearbyPlacesList.get(randomNum));

            for (int i = 0; i <= 2; i++) {
                randomNum = rand.nextInt(nearbyPlacesList.size() - 1);
                while (nums.contains(randomNum)) {
                    randomNum = rand.nextInt(nearbyPlacesList.size() - 1);
                }
                nums.add(randomNum);
                chosenPlaces.add(nearbyPlacesList.get(randomNum));
            }
        }else{
            chosenPlaces = nearbyPlacesList;
        }

        int randomN = rand.nextInt(chosenPlaces.size()-1);
        chosenPlace = chosenPlaces.get(randomN);

    }

    public List<HashMap<String, String>> getPlacesForGame(){
        return chosenPlaces;
    }

    public HashMap<String, String> getChosenPlace(){
        return chosenPlace;
    }


/***************************You should not have to deal with anything under this line. *******************
    /**
     * Asks permission for location usage
     * @param a The activity using it
     */
    public void askPermission(Activity a){
        if(ContextCompat.checkSelfPermission(a.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(a, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                String str = "Explanation needed: PLEASE I NEED TO KNOW WHERE YOU ARE!!!!!";
                Toast.makeText(a.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(a, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                String str = "No, I don't need your crappy explanation. Good Day to you sir.";
                Toast.makeText(a.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            }
        }


        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) a.getSystemService(svcName);

        //Set Location Criteria
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);

        //String provider = LocationManager.NETWORK_PROVIDER;
        String  provider   =  locationManager.getBestProvider(criteria,  true);
        if (provider != null) {
            latLocation = locationManager.getLastKnownLocation(provider);
            String s = "My Location is: \nLat:" +latLocation.getLatitude() + "\nLong: " + latLocation.getLongitude();
            Toast toast = Toast.makeText(a.getApplicationContext(), s, Toast.LENGTH_SHORT);
            toast.show();

            String url = getUrl(latLocation.getLatitude(), latLocation.getLongitude(), locationsTypes);
            Object[] DataTransfer = new Object[1];
            DataTransfer[0] = url;
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            getNearbyPlacesData.setretriever(this);
            getNearbyPlacesData.execute(DataTransfer);

        } else {
            Toast toast = Toast.makeText(a.getApplicationContext(), "Location based services not available", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    /**
     * Sets the places list. Called by the background thread. You should not call.
     * @param npl list of places
     */
    protected void setNearbyPlaces(List<HashMap<String, String>> npl){
        nearbyPlacesList = npl;
    }


    /**
     * Makes the convoluted url for the places request
     * @param latitude    place to look near
     * @param longitude   place to look near
     * @param nearbyPlace Kind of places to look for
     * @return the request url
     */
    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAYXpuEkh14deoc_ELfoHmQiCGUROT1py4");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }
}
