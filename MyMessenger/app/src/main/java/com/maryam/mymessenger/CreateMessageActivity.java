package com.maryam.mymessenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateMessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
    }
    public void onSendMessage(View view){
        //send intent to android system to access ReceiveMessageActivity
        //and start that activity
        EditText messageView=(EditText)findViewById(R.id.message);
        String message=messageView.getText().toString();
        if(!message.replaceAll(" ", "").equals("")) {
            /*Intent intent = new Intent(this, ReceiveMessageActivity.class);
            intent.putExtra(ReceiveMessageActivity.EXTRA_MESSAGE, message);
            startActivity(intent);*/
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,message);
            intent.putExtra(Intent.EXTRA_SUBJECT, "my new app");
            String chooserText=getString(R.string.chooser);
            Intent chooserIntent=Intent.createChooser(intent, chooserText);
            startActivity(chooserIntent);
        }
    }
}
