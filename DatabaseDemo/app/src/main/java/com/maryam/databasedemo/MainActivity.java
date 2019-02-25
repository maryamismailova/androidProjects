package com.maryam.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("https://www.google.com/");
        webView.loadData("<html><body><h1>Hi there!</h1><p>This is a paragraph in mobile app</p></body></html>", "text/html", "UTF-8");

        /*
        try {
            SQLiteDatabase userDB=this.openOrCreateDatabase("NewUsers",MODE_PRIVATE, null);
            userDB.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR, age INTEGER(3), id INTEGER PRIMARY KEY)");
//            userDB.execSQL("INSERT INTO users (name, age) VALUES ('Maryam', 19)");
//            userDB.execSQL("INSERT INTO users (name, age) VALUES ('Farkhad', 21)");
//            userDB.execSQL("INSERT INTO users (name, age) VALUES ('Nadejda', 28)");
//            userDB.execSQL("DELETE FROM users WHERE age>21");
            userDB.execSQL("UPDATE users SET age=age+1 WHERE name = 'Farkhad'");

            Cursor c=userDB.rawQuery("SELECT * FROM users", null);
            int nameIndex=c.getColumnIndex("name");
            int ageIndex=c.getColumnIndex("age");
            int idIndex=c.getColumnIndex("id");
            c.moveToFirst();
            do{
                String user="";
                user+="id "+c.getInt(idIndex)+", name "+c.getString(nameIndex)+", age "+c.getInt(ageIndex);
                Log.i("User", user);
            }while(c.moveToNext());
        }
        catch(Exception e){
            e.printStackTrace();
        }*/
    }
}
