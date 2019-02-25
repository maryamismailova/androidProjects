package com.maryam.readerapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView reader;

    public class TranslateTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String key="trnsl.1.1.20190224T191805Z.ed3a6a7d1a81a838.315d87fd6d37f391a5739714352cf85c9112481c";
            String detectLangRequest="https://translate.yandex.net/api/v1.5/tr.json/detect?key="+key+
                    "&text="+strings[0];
            String getSupportedLangsRequest="https://translate.yandex.net/api/v1.5/tr.json/getLangs" +
                    "?key=" +key+
                    " &ui=en";
            String defLang=Locale.getDefault().getDisplayLanguage();
            URL url;
            URLConnection connection;
            try {

                url=new URL(getSupportedLangsRequest);
                connection=url.openConnection();
                connection.connect();
                String langs="";
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String readLine=bufferedReader.readLine();
                while(readLine!=null){
                    langs+=readLine;
                    readLine=bufferedReader.readLine();
                }
//                Log.i("Supported Langs", langs);
                JSONObject support=new JSONObject(langs);
                JSONObject s=support.getJSONObject("langs");
//                Log.i("JSON LANGS", s.toString());
                String translateTo="ru";
                Iterator<String> keys=s.keys();
                for(int i=0;i<s.length();i++){
                    String cur=keys.next();
                    if(defLang.equals(s.getString(cur))){
                        translateTo=cur;
                    }
                }
//                Log.i("Def Lang Key", translateTo);

//                Log.i("Detect Language", detectLangRequest);

                //detect language
                String language="";
                url=new URL(detectLangRequest);
                connection=url.openConnection();
                connection.connect();
                bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                readLine=bufferedReader.readLine();
                while(readLine!=null){
                    language+=readLine;
                    readLine=bufferedReader.readLine();
                }
//                Log.i("Language", language );
                JSONObject langObject=new JSONObject(language);
                String translateFrom=langObject.getString("lang");
//                Log.i("Language", translateFrom);
                String translateRequest="https://translate.yandex.net/api/v1.5/tr.json/translate?key="+key+
                        "&text="+strings[0]+"&lang="+translateFrom+"-"+translateTo;
//                Log.i("Translate req", translateRequest);
                String translatedText="";
                url=new URL(translateRequest);
                connection=url.openConnection();
                connection.connect();
                bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                readLine=bufferedReader.readLine();
                while(readLine!=null){
                    translatedText+=readLine;
                    readLine=bufferedReader.readLine();
                }
//                Log.i("TRANSLATION", translatedText);
                JSONObject jsonObject=new JSONObject((translatedText));
                JSONArray translations=jsonObject.getJSONArray("text");
                String translated="";
                for(int i=0;i<translations.length();i++){
                    Log.i("translated", translations.getString(i));
                    translated+=translations.getString(i);
                }
                return  translated;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private String findWordForRightHanded(String str, int offset) { // when you touch ' ', this method returns left word.
        if (str.length() == offset) {
            offset--; // without this code, you will get exception when touching end of the text
            return "";
        }

        if (str.charAt(offset) == ' ') {
            offset--;
        }
        int startIndex = offset;
        int endIndex = offset;

        try {
            while (str.charAt(startIndex) != ' ' && str.charAt(startIndex) != '\n') {
                startIndex--;
            }
        } catch (StringIndexOutOfBoundsException e) {
            startIndex = 0;
        }

        try {
            while (str.charAt(endIndex) != ' ' && str.charAt(endIndex) != '\n') {
                endIndex++;
            }
        } catch (StringIndexOutOfBoundsException e) {
            endIndex = str.length();
        }

        // without this code, you will get 'here!' instead of 'here'
        // if you use only english, just check whether this is alphabet,
        // but 'I' use korean, so i use below algorithm to get clean word.
        char last = str.charAt(endIndex - 1);
        if (last == ',' || last == '.' ||
                last == '!' || last == '?' ||
                last == ':' || last == ';') {
            endIndex--;
        }

        return str.substring(startIndex, endIndex);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ConstraintLayout layout=findViewById(R.id.layout);
        reader=findViewById(R.id.textView);
        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent motionEvent) {
                Log.e("", "Longpress detected");
                int mOffset = reader.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
                Log.i("Pressed X", String.valueOf(motionEvent.getX()));
                Log.i("Pressed Y", String.valueOf(motionEvent.getY()));
                Log.i("Pressed Offset", String.valueOf(mOffset));
                if(mOffset<reader.getText().toString().length()-1 && mOffset>=0) {
//                        Toast.makeText(MainActivity.this, findWordForRightHanded(reader.getText().toString(), mOffset), Toast.LENGTH_SHORT).show();
                    String wordToTranslate=findWordForRightHanded(reader.getText().toString(), mOffset);
                    TranslateTask task=new TranslateTask();
                    String translation="";
                    try {
                        translation=task.execute(wordToTranslate).get();
                        Toast.makeText(MainActivity.this, wordToTranslate+"-"+translation, Toast.LENGTH_LONG).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        reader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        /*
        reader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    int mOffset = reader.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
                    Log.i("Pressed X", String.valueOf(motionEvent.getX()));
                    Log.i("Pressed Y", String.valueOf(motionEvent.getY()));
                    Log.i("Pressed Offset", String.valueOf(mOffset));
                    if(mOffset<reader.getText().toString().length()-1 && mOffset>=0) {
//                        Toast.makeText(MainActivity.this, findWordForRightHanded(reader.getText().toString(), mOffset), Toast.LENGTH_SHORT).show();
                        String wordToTranslate=findWordForRightHanded(reader.getText().toString(), mOffset);
                        TranslateTask task=new TranslateTask();
                        String translation="";
                        try {
                            translation=task.execute(wordToTranslate).get();
                            Toast.makeText(MainActivity.this, wordToTranslate+"-"+translation, Toast.LENGTH_LONG).show();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });*/
    }
}
