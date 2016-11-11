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

import java.util.HashMap;
import java.util.List;

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
            Toast toast = Toast.makeText(a.getApplicationContext(), "Location based services not available", Toast.LENGTH_SHORT);

            String url = getUrl(latLocation.getLatitude(), latLocation.getLongitude(), locationsTypes);
            Object[] DataTransfer = new Object[1];
            DataTransfer[0] = url;
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            getNearbyPlacesData.setretriever(this);
            getNearbyPlacesData.execute(DataTransfer);

            //updateWithNewLocation(l);
            //add automated locaton updates later
           // locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
        } else {
            Toast toast = Toast.makeText(a.getApplicationContext(), "Location based services not available", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public Location getLocation(){
        return latLocation;
    }


    protected void setNearbyPlaces(List<HashMap<String, String>> npl){
        nearbyPlacesList = npl;
    }


    /**
     * Returns the places! parse method:
     * for (int i = 0; i < nearbyPlacesList.size(); i++) {
         HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
         double lat = Double.parseDouble(googlePlace.get("lat"));
         double lng = Double.parseDouble(googlePlace.get("lng"));
         String placeName = googlePlace.get("place_name");
         String vicinity = googlePlace.get("vicinity");
         LatLng latLng = new LatLng(lat, lng);
     }
     * @return
     */
    public List<HashMap<String, String>> getplaces(){
        return nearbyPlacesList;
    }

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
