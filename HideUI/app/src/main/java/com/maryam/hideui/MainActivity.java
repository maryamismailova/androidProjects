package com.maryam.hideui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    public void show(View view){
        findViewById(R.id.textToHide).setVisibility(View.VISIBLE);
    }
    public void hide(View view){
        findViewById(R.id.textToHide).setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
