package com.example.heinzer.finalproject;

import android.location.Address;
import android.location.Geocoder;
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

import com.google.android.gms.location.places.Places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by hornl on 11/10/2016.
 */
public class PlacesRetriever {
    final static int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    private Activity a;
    private LocationManager locationManager;
    private Criteria criteria;

    private int PROXIMITY_RADIUS = 8046;
    private static Location latLocation;

    private String locationsTypes = "restaurant";
    List<Place> nearbyPlacesList = null;
    private List<Place> chosenPlaces = null;
    private Place chosenPlace = null;
    private boolean isReady = false;

    private String[] placeholders= {"Kitty", "Tree", "TacoBell", "Taylor"};

    /**
     * Returns the User's current Location
     * @return user's location
     */
    public static Location getLocation(){
        return latLocation;
    }

    /**
     * Returns the List of places within the specified radius!
     * @return The list
     */
    public List<Place> getplaces(){
        return nearbyPlacesList;
    }

    public boolean isDataReady(){
        return isReady;
    }

    public void choosePlaces(){
        Random rand = new Random();
        chosenPlaces = new ArrayList<>();
        ArrayList<Integer> nums = new ArrayList<Integer>();

        if(!isDataReady()){
            System.out.println("The data is not ready yet");
        }

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
            if(chosenPlaces.size() < 4){
                for(int i = 0; chosenPlaces.size()<4; i++){
                    Place p = new Place();
                    p.setName(placeholders[i]);
                    p.setLongitude(0);
                    p.setLatitude(0);
                    chosenPlaces.add(p);

                }
            }
        }

        int randomN = rand.nextInt(chosenPlaces.size()-1);
        chosenPlace = chosenPlaces.get(randomN);
        setPlaceAddress(chosenPlace);
        chosenPlaces.remove(randomN);
        System.out.println("Here is where it is set: " + chosenPlaces);

    }

    public List<Place> getPlacesForGame(){
        System.out.println("Places are here: " + chosenPlaces);
        return chosenPlaces;
    }

    public Place getChosenPlace(){
        return chosenPlace;
    }


/***************************You should not have to deal with anything under this line. *******************
    /**
     * Asks permission for location usage
     * @param a The activity using it
     */
    public void askPermission(Activity a){
        this.a = a;
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

    /**
     * Sets the places list. Called by the background thread. You should not call.
     * @param npl list of places
     */
    protected void setNearbyPlaces(List<Place> npl){

        nearbyPlacesList = npl;
        if(nearbyPlacesList == null){
            System.out.println("Um, the list is null when setting");
            isReady = false;
        }else{
            isReady = true;
        }
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
        // googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAYXpuEkh14deoc_ELfoHmQiCGUROT1py4");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private void setPlaceAddress(Place p){
        String addressString = "";

        if(Geocoder.isPresent()) {
            Geocoder g = new Geocoder(a.getApplicationContext(), Locale.US);

            try {
                List<Address> a = g.getFromLocation(p.getLatitude(), p.getLongitude(), 1);

                if (a != null && a.size() > 0) {
                    String address = a.get(0).getAddressLine(0);
                    String address2 = a.get(0).getAddressLine(1);
                    String postalCode = a.get(0).getPostalCode();
                    String country = a.get(0).getCountryName();
                    addressString = address + " \n" + address2 + " \n" + postalCode + " \n" +country;

                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }else{
            Toast toast = Toast.makeText(a.getApplicationContext(), "Geocoder is Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        p.setAddress(addressString);
    }

}
