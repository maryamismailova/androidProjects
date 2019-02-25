package com.maryam.guesscelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CelebrityClass {
    private String name;
    private String url;
    private Bitmap image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public CelebrityClass(String name, String url) {
        this.name=name;
        this.url=url;
/*        try {
            URL imgURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imgURL.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(stream);
            this.image=bmp;
        }catch(Exception e){
            e.printStackTrace();
        }*/
    }
    public String toString() {
        return this.name+" - "+this.url ;

    }
}