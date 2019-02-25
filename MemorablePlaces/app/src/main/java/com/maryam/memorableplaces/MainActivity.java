package com.maryam.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ListView locationsView;
    ArrayList<String> addresses=new ArrayList<>();
    ArrayList<LatLng> locations=new ArrayList<>();
    SharedPreferences sharedPreferences;

    public ArrayList<LatLng> locationToLatLng(ArrayList<LocationSerializable> locs) {
        ArrayList<LatLng> latLngs=new ArrayList<>();
        for(int i=0;i<locs.size();i++){
            latLngs.add(new LatLng(locs.get(i).latitude, locs.get(i).longitude));
        }
        return latLngs;
    }

    public ArrayList<LocationSerializable> latlngToLocation(ArrayList<LatLng> latLngs){
        ArrayList<LocationSerializable> locs=new ArrayList<>();
        for(int i=0;i<latLngs.size();i++){
            locs.add(new LocationSerializable(latLngs.get(i).latitude, latLngs.get(i).longitude));
        }
        return locs;
    }

    public static String coordinateToAdress(LatLng latLng, Geocoder geocoder){
        String newAddress="";
//        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addresses.get(0)!=null){
                if(addresses.get(0).getSubThoroughfare()!=null){
                    newAddress+=addresses.get(0).getSubThoroughfare()+" ";
                }
                if(addresses.get(0).getThoroughfare()!=null){
                    newAddress+=addresses.get(0).getThoroughfare()+", ";
                }
                if(addresses.get(0).getLocality()!=null){
                    newAddress+=addresses.get(0).getLocality()+", ";
                }
                if(addresses.get(0).getAdminArea()!=null){
                    newAddress+=addresses.get(0).getAdminArea()+", ";
                }
                if(addresses.get(0).getCountryName()!=null){
                    newAddress+=addresses.get(0).getCountryName();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newAddress;
    }

    public void updateListView(){
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addresses);
        locationsView.setAdapter(arrayAdapter);
    }
    public void addNewLocation(View view){
        Intent toMap=new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(toMap, 1);
    }

    public void clearMemory(View view){
        sharedPreferences.edit().clear();
        locations=new ArrayList<>();
        addresses=new ArrayList<>();
        updateListView();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            ArrayList<LatLng> newLocations;
            if ((newLocations=data.getParcelableArrayListExtra("newLocations")) != null) {
                Log.i("New Locations", newLocations.toString());
                //Toast.makeText(getApplicationContext(), "get new loc "+newLocation, Toast.LENGTH_LONG).show();
                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
                for(int i=0;i<newLocations.size();i++){
                    locations.add(newLocations.get(i));
                    addresses.add(coordinateToAdress(newLocations.get(i), geocoder));
                }
                updateListView();
                try {
                    sharedPreferences.edit().putString("addresses", ObjectSerializer.serialize(addresses)).apply();
                    Log.i("Addresses", "updated");
                    Log.i("Addresses", addresses.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    sharedPreferences.edit().putString("locations", ObjectSerializer.serialize(latlngToLocation(locations))).apply();
                    Log.i("Locations", "updated");
                    Log.i("Locations", locations.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=this.getSharedPreferences("com.maryam.memorableplaces", Context.MODE_PRIVATE);
        String s=sharedPreferences.getString("locations", "");
        if(!s.equals("")){
            try {
                locations=locationToLatLng((ArrayList<LocationSerializable>)ObjectSerializer.deserialize(s));
                Log.i("Locations", "extracted");
                Log.i("Locations", locations.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            Log.i("Locations not found", "error in sharedp");
        }

        s=sharedPreferences.getString("addresses", "");
        if(!s.equals("")){
            try {
                addresses=(ArrayList<String>)ObjectSerializer.deserialize(s);
                Log.i("Addresses", "extracted");
                Log.i("Addresses", addresses.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }else{
            Log.i("Addresses not found", "error in sharedp");
        }

        locationsView=findViewById(R.id.locations);
        locationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toMap=new Intent(getApplicationContext(), MapsActivity.class);
                toMap.putExtra("location", locations.get(position));
                startActivityForResult(toMap, 1);
            }
        });
        updateListView();
    }
}
