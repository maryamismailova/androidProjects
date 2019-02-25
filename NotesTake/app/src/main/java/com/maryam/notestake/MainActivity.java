package com.maryam.notestake;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView languageView;

    public void storeNewLanguagePreference(String language){
        sharedPreferences.edit().putString("language", language).apply();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        storeNewLanguagePreference(item.getTitle().toString());
        languageView.setText(item.getTitle().toString());
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("com.maryam.notestake", Context.MODE_PRIVATE);
        languageView=findViewById(R.id.language);
        final String language=sharedPreferences.getString("language", null);
        if(language!=null){
            languageView.setText(language);
        }else{
            languageView.setText("Choose a language!");
        }

        if(sharedPreferences.getString("languageSet", null)==null) {

            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Language")
                    .setMessage("Choose application language")
                    .setPositiveButton("Spanish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            storeNewLanguagePreference("Spanish");
                            languageView.setText("Spanish");
                            sharedPreferences.edit().putString("languageSet", "true").apply();
                        }
                    })
                    .setNegativeButton("French", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            storeNewLanguagePreference("French");
                            languageView.setText("French");
                            sharedPreferences.edit().putString("languageSet", "true").apply();
                        }
                    }).show();
        }
    }
}
