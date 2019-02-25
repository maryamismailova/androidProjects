package com.maryam.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    //2 means empty cell
    int[] boardCells={2,2,2,2,2,2,2,2,2};
    final int[][] winCombinations={{0,1,2},{3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}};
    //0 - red player, 1 - yellow player
    int activePlayer=0;
    //-1 game is continuing with no winner, -2 game finished with no winner
    int winner=-1;

    public void playAgain(View view){
        GridLayout board=findViewById(R.id.gameGrid);
        int cells=board.getColumnCount()*board.getRowCount();
        for(int i=0;i<cells;i++){
            ((ImageView)board.getChildAt(i)).setImageResource(0);
        }
        for(int i=0;i<boardCells.length;i++)boardCells[i]=2;
        activePlayer=0;
        winner=-1;
        LinearLayout playAgain=findViewById(R.id.playAgainLayout);
        playAgain.setVisibility(View.INVISIBLE);
    }

    public void dropIn(View view){
        if(winner==-1) {
            ImageView img = (ImageView) view;
            int cell = Integer.parseInt(img.getTag().toString());
            System.out.println("Pressed "+cell);
            if (boardCells[cell] == 2) {
                img.setTranslationY(-1000f);
                img.setVisibility(View.VISIBLE);
                boardCells[cell] = activePlayer;
                if (activeIsWinner()) {
                    winner=activePlayer;
                }
                if (activePlayer == 0) {
                    img.setImageResource(R.drawable.red);
                    activePlayer = 1;
                } else {
                    img.setImageResource(R.drawable.yellow);
                    activePlayer = 0;
                }
                img.animate().translationYBy(1000f).setDuration(300);
                if(winner==-1 && finishedWithNoWin())winner=-2;
                if(winner!=-1 || winner==-2){
                    LinearLayout layout=findViewById(R.id.playAgainLayout);
                    layout.setVisibility(View.VISIBLE);
                    TextView text=findViewById(R.id.winnerText);
                    String msg="";
                    if(winner!=-2){
                        msg+="Congratulations!\n";
                        if(winner==1)msg+="Yellow Player won!";
                        else msg+="Red Player won!";
                    }else{
                        msg+="NO WIN :(";
                    }
                    msg+="\n\nWant to continue?";
                    text.setText(msg);
                }
            }
        }else{
            System.out.println("Cell is not accessible!");
        }
    }
    boolean finishedWithNoWin(){
        boolean noWin=true;
        for(int i=0;i<boardCells.length;i++){
            if(boardCells[i]==2)noWin=false;
        }
        return noWin;
    }
    boolean activeIsWinner(){
        for(int i=0;i<winCombinations.length;i++){
            boolean isWinning=true;
            for(int j=0;j<winCombinations[i].length;j++){
                if(boardCells[winCombinations[i][j]]!=activePlayer){
                    isWinning=false;
                }
            }
            if(isWinning)return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
