package com.maryam.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends Activity {
    EditText cityName;
    String weatherMessage;
    TextView cityWeather;
    public void findWeather(View view){
        InputMethodManager manager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        String city= null;
        try {
            city = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            String apikey="&appid=e28449b821ace73ebec7cf64f4fd7fe2";
            String url="https://api.openweathermap.org/data/2.5/weather?q=";
            Log.i("City", city);
            WeatherDownloadTask weatherDownloadTask=new WeatherDownloadTask();
            weatherDownloadTask.execute(url+city+apikey);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public class WeatherDownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result="";
            URL url;
            URLConnection connection;
            try {
                url=new URL(strings[0]);
                connection=url.openConnection();
                connection.connect();
                BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line=reader.readLine();
                while(line!=null){
                    Log.i("readLine", line);
                    result+=line;
                    line=reader.readLine();
                }
                return result;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                try {
                    weatherMessage="";
                    Log.i("i am here", "JSON");
                    JSONObject object = new JSONObject(s);
                    String weather = object.getString("weather");
                    JSONArray jsonArray = new JSONArray(weather);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        weatherMessage+=object1.getString("main")+": ";
                        weatherMessage+=object1.getString("description")+"\n";
                        Log.i("main", object1.getString("main"));
                        Log.i("descpription", object1.getString("description"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                weatherMessage="Incorrect city name provided!";
                Log.i("Incorrect", "no data");
            }
            cityWeather.setText(weatherMessage);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=findViewById(R.id.cityName);
        cityWeather=findViewById(R.id.weatherInfo);
    }
}
