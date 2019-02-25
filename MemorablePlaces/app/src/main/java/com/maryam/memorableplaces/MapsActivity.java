package com.maryam.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    LocationListener locationListener;

    ArrayList<LatLng> newLocations=new ArrayList<>();
    LatLng newLocation = null;
    LatLng startLocation = null;
    private GoogleMap mMap;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
            startListening();
        }
    }

    public void startListening(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location loc;
            if((loc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER))!=null){
                startLocation=new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 10));
                Log.i("Location", loc.toString());
            }else if((loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER))!=null) {
                startLocation=new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 10));
                Log.i("Location", loc.toString());
            }else if((loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER))!=null) {
                startLocation=new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 10));
                Log.i("Location", loc.toString());
            }
            else{
                Log.i("Location", "unable to find");
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void addNewMarker(LatLng loc) {
//        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(loc).title(MainActivity.coordinateToAdress(loc, geocoder)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.clear();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                newLocation = latLng;
                newLocations.add(latLng);
                Toast.makeText(getApplicationContext(), "new location:" + latLng, Toast.LENGTH_LONG).show();
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                mainActivity.putParcelableArrayListExtra("newLocations", newLocations);
                setResult(1, mainActivity);
                addNewMarker(newLocation);
            }
        });

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        Intent fromMain = getIntent();
        newLocation = fromMain.getParcelableExtra("location");

        if(newLocation==null){
            Log.i("Location", "no location provided");
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.i("User location changed", location.toString());
                    Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_SHORT).show();
                    startLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 10));
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
            if (Build.VERSION.SDK_INT < 23) {
                startListening();
                Log.i("Location", "version <23");
            }else{
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    Log.i("Location", "ask for permission");
                }else {
                    Log.i("Location", "version>23, listen");
                    startListening();
                }
            }
        }
        // Add a marker in Sydney and move the camera
        if(newLocation==null){
            if(startLocation==null)startLocation=new LatLng(-34, 151);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 10));
        }
        else{
            addNewMarker(newLocation);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("NewLocs", newLocations.toString());
        Log.i("Back pressed", "ok");
    }
}
