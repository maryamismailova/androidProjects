package com.maryam.guesscelebrity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    LinearLayout gameLayout;
    Button startButton;
    ArrayList<CelebrityClass> celebrities=new ArrayList<>();
    TextView gameScore;
    ImageView celebrityImage;
    TextView finalScoreView;
    int nbAnswers=4;

    ArrayList<Integer> askedQuestions=new ArrayList<>();
    boolean gameModeOn=false;
    String correctAnswer="";
    int score=0;
    int nbQuestionsAsked=0;
    int MAXSCORE=10;

    public class CelebrityExtractTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Please wait, data is being downloaded", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String data="";
            try {
                URL url = new URL(strings[0]);
                ArrayList<CelebrityClass> celebrities = new ArrayList<>();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
                String readLine=reader.readLine();
                while (readLine!=null) {
                    data+=readLine+"\n";
                    readLine=reader.readLine();
                }
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                Pattern pattern = Pattern.compile("<img alt=\"(.*?)\"\n" +
                        "height=\"209\"\n" +
                        "src=\"(.*?)\"\n" +
                        "width=\"140\" />");
                Matcher matcher = pattern.matcher(s);
                while (matcher.find()) {
                    celebrities.add(new CelebrityClass(matcher.group(1), matcher.group(2)));
                }
                Toast.makeText(MainActivity.this, "Everything is ready! GO!", Toast.LENGTH_SHORT).show();
                startButton.setEnabled(true);
            }
            else{
                Toast.makeText(MainActivity.this, "You might have some connection problems:(\nCan't load data. ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{
                URL url=new URL(strings[0]);
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                connection.connect();
                Bitmap image=BitmapFactory.decodeStream(connection.getInputStream());
                return image;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    public void startGame(View view){
        gameModeOn=true;
        gameLayout.setVisibility(View.VISIBLE);
        setNewQuestion();
    }

    public void setScoreView(){
        String text=String.valueOf(score)+"/"+String.valueOf(MAXSCORE);
        gameScore.setText(text);
    }

    public void guessMade(View view){
        if(((Button)view).getText().equals(correctAnswer)){
            score++;
            setScoreView();
            Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
        }
        setNewQuestion();
    }

    public boolean hasOccuredInArray(String array[],String string, int end){
        for(int i=0;i<end;i++){
            if(array[i].equals(string))return true;
        }
        return false;
    }

    public void setAnswerButtons(){
        int correct=(int)(Math.random()*nbAnswers);
        String[] answers=new String[nbAnswers];
        answers[correct]=correctAnswer;
        for(int i=0;i<nbAnswers;i++){
            if(i!=correct){
                int x=(int)(Math.random()*celebrities.size());
                while(hasOccuredInArray(answers, celebrities.get(x).getName(), i) ){
                    x=(int)(Math.random()*celebrities.size());
                }
                answers[i]=celebrities.get(x).getName();
            }
        }
        ArrayList<View> buttons=(findViewById(R.id.buttonsLayout)).getTouchables();
        for(int i=0;i<buttons.size();i++){
            ((Button)buttons.get(i)).setText(answers[i]);
        }
    }

    public void setNewQuestion(){
        if(nbQuestionsAsked<MAXSCORE) {
            try {
                int newCeleb = (int) (Math.random() * celebrities.size());
                while (askedQuestions.contains(new Integer(newCeleb))) {
                    newCeleb = (int) (Math.random() * celebrities.size());
                }
                askedQuestions.add(newCeleb);
                correctAnswer = celebrities.get(newCeleb).getName();
                Log.i("Selected Celebrity", celebrities.get(newCeleb).toString());

                DownloadImageTask downloadImage = new DownloadImageTask();
                Bitmap img = downloadImage.execute(celebrities.get(newCeleb).getUrl()).get();

                celebrityImage.setImageBitmap(img);
                setAnswerButtons();
                nbQuestionsAsked++;
            } catch (Exception e) {
                Log.i("Error download", "fail");
                e.printStackTrace();
            }
        }else{
            gameLayout.setVisibility(View.INVISIBLE);
            String finalScore="Your score is "+String.valueOf(score)+"/"+String.valueOf(MAXSCORE)+"\nTry once more?";
            gameScore.setText("0/10");
            finalScoreView.setText(finalScore);
            finalScoreView.setVisibility(View.VISIBLE);
            score=0;
            nbQuestionsAsked=0;
            askedQuestions=new ArrayList<>();
            gameModeOn=false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameLayout=findViewById(R.id.gameLayout);
        gameScore=findViewById(R.id.gameScore);
        startButton=findViewById(R.id.startButton);
        startButton.setEnabled(false);
        celebrityImage=findViewById(R.id.celebrityImage);
        finalScoreView=findViewById(R.id.scoreView);
        CelebrityExtractTask findCeleb=new CelebrityExtractTask();
        findCeleb.execute("https://www.imdb.com/list/ls052283250/");

    }
}
