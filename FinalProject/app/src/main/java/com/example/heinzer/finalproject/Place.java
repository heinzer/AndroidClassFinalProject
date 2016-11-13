package com.example.heinzer.finalproject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by heinzer on 11/4/2016.
 */

public class Place implements Serializable {

    private double longitude;
    private double latitude;
    private long id;
    private String name;
    private String address;
    private String vicinity;
    private String photoReference;
    private String placeId;

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setVicinity(String vicinity){this.vicinity = vicinity;}

    public String getVicinity(){return vicinity;}

    public String getFullDetails(){
        return name + "\n\n" + address;
    }

    public void setPhotoReference(String photoReference){
        this.photoReference = photoReference;
    }

    public String getPhotoReference(){
        return photoReference;
    }

    public void setPlaceId(String placeId){
        this.placeId = placeId;
    }
    public String getPlaceId(){
        return placeId;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
