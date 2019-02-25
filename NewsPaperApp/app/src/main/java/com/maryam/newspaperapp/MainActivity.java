package com.maryam.newspaperapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView newsList;
    ArrayList<String> titles=new ArrayList<>();
    ArrayList<String> articleURLs=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    SQLiteDatabase database;

    public class DownloadIdTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String data="";
            JSONArray jsonArray=null;
            try {
                //READ IDs
                data=readURL(strings[0]);
                jsonArray=new JSONArray(data);
                int nbArticles=20;
                if(jsonArray.length()<nbArticles)nbArticles=jsonArray.length();

                for(int i=0;i<nbArticles;i++){
                    //READ TITLE&URL
                    Log.i("Article ID", jsonArray.getString(i));
                    String articleId=jsonArray.getString(i);
                    String articleObject=readURL(" https://hacker-news.firebaseio.com/v0/item/"+articleId+".json?print=pretty");
                    JSONObject jsonObject=new JSONObject(articleObject);
                    if(!jsonObject.isNull("title") && !jsonObject.isNull("url")) {
//                        Log.i("Title", jsonObject.getString("title"));
//                        Log.i("URL", jsonObject.getString("url"));

                        // READ HTML
                        String pageData = readURL(jsonObject.getString("url"));
                        String sqlQuery="INSERT INTO news (title, data) VALUES ('"+jsonObject.getString("title")+"' , '"+pageData+"' )";
//                        database.execSQL("INSERT INTO news (title, data) VALUES (" + jsonObject.getString("title") + ", " +
//                                pageData + ")");
//                        SQLiteStatement statement=new SQLiteStatement(database, "INSERT INTO news (title, data) VALUES (?, ?, ?)", null);
                        SQLiteStatement statement=database.compileStatement("INSERT INTO news (title, data) VALUES (?, ?)");
                        statement.bindString(1, jsonObject.getString("title"));
                        statement.bindString(2, pageData);
                        statement.execute();
//                        publishProgress(sqlQuery);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            Log.i("SQL", values[0]);
            database.execSQL(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Cursor c=database.rawQuery("SELECT * FROM news", null);
            c.moveToFirst();
            do{
                int titleIndex=c.getColumnIndex("title");
                titles.add(c.getString(titleIndex));
                arrayAdapter.notifyDataSetChanged();
            }while(c.moveToNext());

        }

        protected String readURL(String urlString){
            URL url;
            URLConnection connection;
            String data="";
            try {
                //READ IDs
                url = new URL(urlString);
                connection = url.openConnection();
                connection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String read;
                while ((read = bufferedReader.readLine()) != null) {
                    data += read;
                }
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
    }


    public void setupData(){
        Cursor c=database.rawQuery("SELECT * FROM news", null);
        if(c==null || c.getCount()==0){
            //need to download the data!
            Log.i("Database", "EMPTY");
            try {
                String url="https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";
                DownloadIdTask task=new DownloadIdTask();
                task.execute(url);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Log.i("SQL", "Access data");
            c.moveToFirst();
            do{
                int titleIndex=c.getColumnIndex("title");
                titles.add(c.getString(titleIndex));
                arrayAdapter.notifyDataSetChanged();
            }while(c.moveToNext());

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsList=findViewById(R.id.newsList);
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        newsList.setAdapter(arrayAdapter);
        database=this.openOrCreateDatabase("News", MODE_PRIVATE, null);
//        database.execSQL("DELETE FROM news");
        database.execSQL("CREATE TABLE IF NOT EXISTS news (title VARCHAR, data VARCHAR)");
        setupData();
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c=database.rawQuery("SELECT * FROM news", null);
                int textIndex=c.getColumnIndex("data");
                c.move(position+1);
                Log.i("NewsSelected", ""+position);
                String pageText=c.getString(textIndex);
                Intent intent=new Intent(getApplicationContext(),Main2Activity.class );
                intent.putExtra("pageText", pageText);
                startActivity(intent);
            }
        });

    }
}
