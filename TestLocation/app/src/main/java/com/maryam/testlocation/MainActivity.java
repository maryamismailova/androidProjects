package com.maryam.testlocation;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView prov;
    FusedLocationProviderClient fusedLocationProviderClient;

    public String decodeLocation(Location location){
        String address="";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addresses.get(0).getSubThoroughfare()!=null){
                address+=addresses.get(0).getSubThoroughfare()+" ";
            }
            if(addresses.get(0).getThoroughfare()!=null){
                address+=addresses.get(0).getThoroughfare()+", ";
            }
            if(addresses.get(0).getLocality()!=null){
                address+=addresses.get(0).getLocality()+", ";
            }
            if(addresses.get(0).getCountryName()!=null){
                address+=addresses.get(0).getCountryName()+" ";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  address;
    }
    public void makeToast(String msg) {
        Log.i("Location", msg);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void startListening() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 10, 1, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        makeToast("Requesting permission");
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
          /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            makeToast("GET NULL LOCATION");
                            prov.setText("NULL LOCATION");
                        } else {
                            makeToast("Location: " + location.toString());
                            prov.setText("Location: " + location.toString());
                        }
                    }
                });
            }else{
                makeToast("Permission not granted!");
            }*/
        }else{
            makeToast("Error occured!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prov = findViewById(R.id.providerView);
        //EITHER USE GOOGLE'S API FusedLocationProviderClient OR LocationManager
        //OPEN GOOGLE MAPS BEFORE TO STORE SOME LOCATION!!!!

        /*fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            makeToast("I am here!");
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null) {
                        makeToast("GET NULL LOCATION");
                        prov.setText("NULL LOCATION");
                    } else {
                        makeToast("Location: " + location.toString());
                        prov.setText("Location: " + location.toString());
                    }
                }
            });
        }else{
            makeToast("NO PERMISSIONS!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }*/
                locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                makeToast("new location: "+location.toString());
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
        if(Build.VERSION.SDK_INT>=23){
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                startListening();
            }
        }else{
            startListening();
        }

        List<String> providers=locationManager.getProviders(true);
        String names="providers number "+providers.size()+"\n";
        for(int i=0;i<providers.size();i++){
            if(providers.get(i)==null)names+="NULL FOR PROVIDER\n";
            else names+="provider: "+providers.get(i)+"\n";

        }
        makeToast(names);
        prov.setText(names);

        names+="Locations:\n";
        for(int i=0;i<providers.size();i++){
            if(locationManager.getLastKnownLocation(providers.get(i))!=null) {
                names += "Location from "+providers.get(i)+" : "+decodeLocation(locationManager.getLastKnownLocation(providers.get(i))) + "\n";
            }else{
                names+="NULL location from "+providers.get(i)+" provider\n";
            }
        }
        makeToast(names);
        prov.setText(names);

    }
}
