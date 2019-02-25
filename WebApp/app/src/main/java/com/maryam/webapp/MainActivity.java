package com.maryam.webapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity {

    ImageView imageView;
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream=connection.getInputStream();
                Bitmap image= BitmapFactory.decodeStream(inputStream);

                return image;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }
    public void downloadImage(View view){
        // https://www.irishtimes.com/polopoly_fs/1.3170107.1501253408!/image/image.jpg_gen/derivatives/box_620_330/image.jpg
        try {
            ImageDownloader task=new ImageDownloader();
            Bitmap image=task.execute("https://www.irishtimes.com/polopoly_fs/1.3170107.1501253408!/image/image.jpg_gen/derivatives/box_620_330/image.jpg").get();
            imageView.setImageBitmap(image);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
    }
}
