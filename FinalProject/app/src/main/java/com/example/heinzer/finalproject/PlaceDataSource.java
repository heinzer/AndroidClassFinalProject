package com.example.heinzer.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.heinzer.finalproject.MySQLiteHelper;
import com.example.heinzer.finalproject.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinzer on 10/14/2016.
 */

public class PlaceDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_ADDRESS,
            MySQLiteHelper.COLUMN_LATITUDE, MySQLiteHelper.COLUMN_LONGITUDE
    };

    public PlaceDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Place createPlace(String name, String address, double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_ADDRESS, address);
        values.put(MySQLiteHelper.COLUMN_LATITUDE, latitude);
        values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);

        long insertId = database.insert(MySQLiteHelper.TABLE_PLACES, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Place newPlace = cursorToPlace(cursor);
        cursor.close();
        return newPlace;
    }

    public void deletePlace(Place place) {
        long id = place.getId();
        System.out.println("Place deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PLACES, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<Place>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACES, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Place place = cursorToPlace(cursor);
            places.add(place);
            cursor.moveToNext();
        }
        cursor.close();
        return places;
    }

    private Place cursorToPlace(Cursor cursor){
        Place place = new Place();
        place.setId(cursor.getLong(0));
        place.setName(cursor.getString(1));
        place.setAddress(cursor.getString(2));
        place.setLatitude(cursor.getDouble(4));
        place.setLongitude(cursor.getDouble(5));
        return place;
    }
}
