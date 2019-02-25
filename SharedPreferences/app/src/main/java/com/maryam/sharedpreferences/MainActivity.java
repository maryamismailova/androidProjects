  package com.maryam.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

  public class MainActivity extends AppCompatActivity {
      EditText editText;
      TextView textView;
      ArrayList<String> words=new ArrayList<>();
      String saved="";
      SharedPreferences sharedPreferences;
      public String listToString(ArrayList<String> list){
          String newString="";
          for(int i=0;i<list.size();i++){
              newString+=list.get(i)+"\n";
          }
          return newString;
      }
      public void submitWord(View view){
          String w=editText.getText().toString();
          words.add(w);
          try {
              sharedPreferences.edit().putString("newWords", ObjectSerializer.serialize(words)).apply();
          } catch (IOException e) {
              e.printStackTrace();
          }
          textView.setText(listToString(words));
          editText.setText("");

      }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.savedName);
        sharedPreferences=this.getSharedPreferences("com.maryam.sharedpreferences", Context.MODE_PRIVATE);
        String input=sharedPreferences.getString("newWords", "");
        if(!input.equals("")){
            try {
                words=(ArrayList<String>)ObjectSerializer.deserialize(input);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        textView.setText(listToString(words));

    }
}
