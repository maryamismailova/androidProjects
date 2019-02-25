package com.maryam.braintrain;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {
    int maxNumberOfIter=15;
    int curNumberOfIter=1;
    int timePerIteration=15;
    int score=0;
    CountDownTimer timer;
    int curAnswer;
    boolean answerGuessed=false;
    boolean gameIsOn=true;

    public void setScore(){
        ((TextView)findViewById(R.id.gameScore)).setText(String.valueOf(score)+"/15");
    }
    public void setIteration(){
        ((TextView)findViewById(R.id.gameIteration)).setText(String.valueOf(curNumberOfIter)+"/15");
    }

    public void answerClicked(View view){
        //if game still continues
        if(gameIsOn) {
            Button clicked = (Button) view;
            if (curAnswer == Integer.parseInt(clicked.getText().toString())) {
                score++;
                setScore();
                timer.cancel();
                Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                generateNewTask();
            } else {
                timer.cancel();
                Toast.makeText(MainActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                generateNewTask();
            }
        }else{
            timer.cancel();
            Button playAgain=findViewById(R.id.playAgain);
            playAgain.setVisibility(View.VISIBLE);
        }
    }

    public boolean hasOccuredInArray(int key, int array[],int endIndex){
        for(int i=0;i<endIndex;i++){
            if(array[i]==key)return true;
        }
        return false;
    }
    public void setAnswerButtons(){
        GridLayout grid=findViewById(R.id.buttonGrid);
        ArrayList<View> list=grid.getTouchables();
        int correct=(int)(Math.random()*4);
        int answers[]=new int[4];
        for(int i=0;i<4;i++){
            if(i!=correct){
                int fake;
                do{
                    fake=(int)(Math.random()*(curAnswer+20));
                }while (hasOccuredInArray(fake, answers, i)==true);

                answers[i]=fake;
                ((Button)list.get(i)).setText(String.valueOf((fake)));
            }else{
                answers[i]=curAnswer;
                ((Button)list.get(i)).setText(String.valueOf(curAnswer) );
            }
        }
    }

    public void playAgain(View view){
        curNumberOfIter=0;
        score=0;
        gameIsOn=true;
        view.setVisibility(View.INVISIBLE);
        setIteration();
        setScore();
        generateNewTask();
        Toast.makeText(MainActivity.this, "New game started!", Toast.LENGTH_SHORT).show();
    }

    public void generateNewTask(){
        if(curNumberOfIter<=maxNumberOfIter) {
            Log.i("Iteration #: ", String.valueOf(curNumberOfIter));
            setIteration();
            //make random expression
            int x1 = (int) (Math.random() * 100 - 50);
            int x2 = (int) (Math.random() * 100 - 50);
            curAnswer = x1 + x2;

            //set the text for question
            String taskQuestion = "" + Integer.valueOf(x1);
            if (x2 > 0) taskQuestion += "+" + Integer.valueOf(x2);
            else {
                taskQuestion += Integer.valueOf(x2);
            }
            TextView expression = findViewById(R.id.expression);
            expression.setText(taskQuestion);

            //set random answers
            setAnswerButtons();

            //create a timer for new iteration
            timer = new CountDownTimer(timePerIteration * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
//                    Log.i("Time left", String.valueOf(millisUntilFinished/1000));
                    ((TextView)findViewById(R.id.timeCount)).setText(String.valueOf((int) millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    if (answerGuessed) score += 1;
                    generateNewTask();
                    this.cancel();
                }
            };
            timer.start();
            curNumberOfIter++;
        }else{
            //TODO
            gameIsOn=false;
            timer.cancel();
            Button playAgain=findViewById(R.id.playAgain);
            playAgain.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setScore();
        TextView timeView=findViewById(R.id.timeCount);
        timeView.setText("15");

        generateNewTask();
    }
}
