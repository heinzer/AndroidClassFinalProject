package com.example.heinzer.finalproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hornl on 11/11/2016.
 */
abstract class GetImage extends AsyncTask<String, Void, Bitmap> {

    Activity a = null;

    public GetImage(Activity a){
        this.a = a;
    }
    /**
     * Takes in a list of image URLs and calls the download method
     *
     * @param urls
     * @return
     */
    protected Bitmap doInBackground(String... urls) {
        Bitmap imageDownloaded =null;
        for (int i = 0; i < urls.length; i++) {
            // Download the image
            imageDownloaded = downloadImage(urls[i]);
        }
        return imageDownloaded;
    }




//*******************************Methods to call *******************************************
    private Bitmap downloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = openHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            Log.d("NetworkingActivity", e1.getLocalizedMessage());
        }
        return bitmap;
    }


    private InputStream openHttpConnection(String urlString)throws IOException{
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection)){
            throw new IOException("Not an HTTP connection");
        }
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK){
                in = httpConn.getInputStream();
            }
        }catch (Exception ex){
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

}