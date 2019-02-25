package com.maryam.hikerslocationapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
//TO CHECK HOW IT WORKS!!!!

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    Location location=null;
    TextView positionView;
    public void updatePositioning(){
        Log.i("Working", "1");
        if(location!=null) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String address = "";
            address += "Latitude: " + location.getLatitude() + "\n\n";
            address += "Longitude: " + location.getLongitude() + "\n\n";
            address += "Accuracy: " + location.getAccuracy() + "\n\n";
            address += "Altitude: " + location.getAltitude() + "\n\n";
            try {
                List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (list != null) {
                    address += "Address:\n";
                    if (list.get(0).getSubThoroughfare() != null) {
                        address += list.get(0).getSubThoroughfare() + " ";
                    }
                    if (list.get(0).getThoroughfare() != null) {
                        address += list.get(0).getThoroughfare() + "\n";
                    }
                    if (list.get(0).getLocality() != null) {
                        address += list.get(0).getLocality() + "\n";
                    }
                    if (list.get(0).getPostalCode() != null) {
                        address += list.get(0).getPostalCode() + "\n";
                    }
                    if (list.get(0).getCountryName() != null) {
                        address += list.get(0).getCountryName() + "\n";
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            positionView.setText(address);
        }else {
            Log.i("Location", "null");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location!=null){
                    updatePositioning();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        positionView=findViewById(R.id.hikerPositionText);
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                location=loc;
                Log.i("Location", location.toString());
                updatePositioning();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(Build.VERSION.SDK_INT>=23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i("i am here ", "1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                Log.i("i am here ", "2");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
                location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                updatePositioning();
            }
        }else{
            Log.i("i am here ", "3");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updatePositioning();
        }
        /*
        if(location!=null){
            Log.i("Location", location.getLatitude()+" "+location.getLongitude());
            updatePositioning();
        }else{
            Log.i("Location found", "false");
        }*/
    }
}
