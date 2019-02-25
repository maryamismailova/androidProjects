package com.maryam.testappsharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String name;//="Maryam";
        String surname;//="Ismayilova";
        SharedPreferences sp=getSharedPreferences("com.maryam.testappsharedpref", Context.MODE_PRIVATE);
//        sp.edit().putString("name", name).apply();
//        sp.edit().putString("surname", surname).apply();
        name="";
        surname="";
        name=sp.getString("name", "");
        surname=sp.getString("surname", "");
        Log.i("name", name);
        Log.i("surname", surname);
    }
}
