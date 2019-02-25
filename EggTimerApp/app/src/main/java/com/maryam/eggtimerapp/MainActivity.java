package com.maryam.eggtimerapp;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.sql.Time;
import java.time.Instant;

public class MainActivity extends Activity {
    SeekBar timeBar;
    TextView textView;
    ImageView img;
    Button startButton;
    CountDownTimer countDownTimer;
    int selectedTime=60;
    boolean start=false;

    public void updateTimer(){
        textView.setText(getTimeString());
    }
    public String getTimeString(){
        int minutes=((selectedTime)%3600)/60;
        int seconds=((selectedTime)%3600)%60;
        String sec=String.valueOf(seconds);
        if(seconds<10){
            sec="0"+sec;
        }
        String min=String.valueOf(minutes);
        if(minutes<10){
            min="0"+min;
        }
        String time=min+":"+sec;
        return time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.timeView);
        textView.setText(getTimeString());
        timeBar=findViewById(R.id.timeBar);
        timeBar.setMax(600);
        timeBar.setProgress(selectedTime);

        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedTime=progress;
                updateTimer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startButton=findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start){
                    start=false;
                    ((Button)v).setText("Start");
                    timeBar.setVisibility(View.VISIBLE);
                    timeBar.setProgress(selectedTime);
                    countDownTimer.cancel();
                }
                else{
                    start=true;
                    ((Button)v).setText("Pause");
                    timeBar.setVisibility(View.INVISIBLE);
                    countDownTimer=new CountDownTimer(selectedTime*1000+100,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            selectedTime=(int)millisUntilFinished/1000;
                            updateTimer();
                        }

                        @Override
                        public void onFinish() {
                            selectedTime=0;
                            updateTimer();
                            //CHANGE IMAGE TO CHICKEN
                            textView.setVisibility(View.INVISIBLE);
                            startButton.setVisibility(View.INVISIBLE);
                            img=findViewById(R.id.eggView);
                            img.setImageResource(R.drawable.chicken);
                            //PLAY MUSIC
                            MediaPlayer mediaPlayer=MediaPlayer.create(MainActivity.this, R.raw.rooster);
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                //CHANGE IMAGE BACK TO EGG UPON COMPLETION
                                public void onCompletion(MediaPlayer mp) {
                                    img.setImageResource(R.drawable.egg);
                                    textView.setVisibility(View.VISIBLE);
                                    startButton.setVisibility(View.VISIBLE);
                                }
                            });
                            start=false;
                            timeBar.setVisibility(View.VISIBLE);
                        }
                    };
                    countDownTimer.start();
                }
            }
        });

    }
}
