package com.maryam.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class StopwatchActivity extends Activity {
    private int seconds=0;
    private boolean running=false;
    private boolean wasrunning=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        if(savedInstanceState!=null){
            seconds=savedInstanceState.getInt("seconds");
            running=savedInstanceState.getBoolean("running");
            wasrunning=savedInstanceState.getBoolean("wasrunning");
        }
        runTimer();
    }
    public void onResume(){
        super.onStart();
        if(wasrunning){
            running=true;
        }
    }
    public void onPause(){
        super.onStop();
        wasrunning=running;
        running=false;
    }

    public void onClickStart(View view){
        running=true;
    }
    public void onClickStop(View view){
        running=false;
    }
    public void onClickReset(View view){
        running=false;
        seconds=0;
    }
    private void runTimer(){
        final TextView timeView=findViewById(R.id.time_view);
        final Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run(){
                int hours=seconds/3600;
                int minutes=(seconds%3600)/60;
                int secs=seconds%60;
                String time=String.format(Locale.getDefault(), "%d:%02d:%02d",
                        hours, minutes, secs);
                timeView.setText(time);
                if(running){
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onSaveInstanceState(Bundle savedInstance){
        savedInstance.putInt("seconds", seconds);
        savedInstance.putBoolean("running", running);
        savedInstance.putBoolean("wasrunning", wasrunning);
    }

}
