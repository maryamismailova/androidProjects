package com.maryam.resizingrect;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
    boolean wasClicked=false;
    int acceleration=100;
    public void resize(View view){
        wasClicked=true;
        Button brick=findViewById(R.id.brick);
//        int width=brick.getWidth();
        brick.animate().translationYBy(70).setDuration(5);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Handler handler=new Handler();
        Runnable r=new Runnable() {
            @Override
            public void run() {
                if(acceleration>0) {
                    if (wasClicked) {
                        Button brick = findViewById(R.id.brick);
                        LinearLayout layout = (LinearLayout) findViewById(R.id.window);
//                        View line=findViewById(R.id.line);
                        int height = layout.getHeight();
                        if (brick.getY() > -brick.getHeight() && brick.getY() <= height) {
                            brick.animate().translationYBy(-10).setDuration(10);
//                            line.setLayoutParams(new LinearLayout.LayoutParams(line.getWidth(), (int) brick.getY()));
                        } else {
                            String msg;
                            if (brick.getY() <= -brick.getHeight()) msg = "You lost:(";
                            else msg = "You won!!!";
                            Toast t = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
                            t.show();
                            brick.animate().translationY(0).setDuration(1000);
                            wasClicked = false;
                            acceleration -= 5;
                        }
                        handler.postDelayed(this, acceleration);
                    } else handler.postDelayed(this, acceleration);
                }else{
                    Toast t = Toast.makeText(MainActivity.this, "GAME FINISHED", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        };
        handler.postDelayed(r, 100);
    }
}
