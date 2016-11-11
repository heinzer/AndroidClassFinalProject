package com.example.heinzer.finalproject;

import java.io.Serializable;

/**
 * Created by heinzer on 11/4/2016.
 */

public class Place implements Serializable {

    private double longitude;
    private double latitude;
    private long id;
    private String name;
    private String address;

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

    @Override
    public String toString(){
        return this.name;
    }
}
