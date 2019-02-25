package com.maryam.basicphraseapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    /*int[] phrasesAudio={R.raw.hello,R.raw.doyouspeakenglish, R.raw.goodevening, R.raw.howareyou,
    R.raw.ilivein, R.raw.mynameis, R.raw.please, R.raw.welcome};
    ArrayList<View> buttons;*/

    public void playAudio(View view){
        /*int index=buttons.indexOf(view);
        Log.i("Pressed button: ", Integer.toString(index));
        MediaPlayer player=MediaPlayer.create(MainActivity.this, phrasesAudio[index]);
        player.start();*/
        String res=view.getResources().getResourceEntryName(view.getId());
        Log.i("resource name: ", res);
        int resId=getResources().getIdentifier(res,"raw", getPackageName());
        Log.i("Resource id:", Integer.toString(resId));
        MediaPlayer player=MediaPlayer.create(MainActivity.this, resId);
        player.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        buttons=((findViewById(R.id.buttonContainer)).getTouchables());
        SeekBar volumeBar=findViewById(R.id.volumeBar);

        final AudioManager audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeBar.setBackgroundColor(Color.BLUE);
        volumeBar.setMax(maxVolume);
        volumeBar.setProgress(curVolume);

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Seek volume: ",Integer.toString(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
