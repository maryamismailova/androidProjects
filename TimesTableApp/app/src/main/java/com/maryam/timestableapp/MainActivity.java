package com.maryam.timestableapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    int multiply=0;
    ListView timesTable;
    public void printTimesTable(int max){
        TextView textView=findViewById(R.id.timesValueText);
        textView.setText("TimesTable for "+Integer.toString(multiply));
        ArrayList<String> table=new ArrayList<>();
        for(int i=1;i<max;i++){
            table.add(Integer.toString(multiply)+"*"+Integer.toString(i)+"="+Integer.toString(i*multiply));
        }
        ArrayAdapter<String > arrayAdapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,
                table);
        timesTable.setAdapter(arrayAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timesTable=findViewById(R.id.timesTable);

        final SeekBar multiplier=findViewById(R.id.multiplier);
        multiplier.setMax(19);
        multiplier.setProgress(10);
        multiply=10;
        printTimesTable(10);
        multiplier.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                multiply=progress+1;
                printTimesTable(10);
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
