package com.maryam.higherlower;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HigherLower extends Activity {
    int randNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_higher_lower);
        randNumber=(int)(Math.random()*20);
    }
    public void makeToast(String msg){
        Toast.makeText(HigherLower.this, msg, Toast.LENGTH_LONG).show();
    }
    public void onClickButton(View view){
        EditText guess=findViewById(R.id.guess);
        int guessedNum=Integer.parseInt(guess.getText().toString());
        if(guessedNum>randNumber){
            String outputString="LOWER :P";
            System.out.println(outputString);
            makeToast(outputString);
        }
        else if(guessedNum<randNumber){
            String outputString="Higher ;)";
            System.out.println(outputString);
            makeToast(outputString);
        }
        else{
            makeToast("Congratulations!\nTry once more!");
            System.out.println("Congratulations!\nTry once more!");
            guess.setText("");
            randNumber=(int)(Math.random()*20);
        }
    }
}
