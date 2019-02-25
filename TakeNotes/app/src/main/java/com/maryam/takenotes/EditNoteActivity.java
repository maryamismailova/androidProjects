package com.maryam.takenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class EditNoteActivity extends AppCompatActivity {
    String newNote;
    EditText editNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editNote=findViewById(R.id.editNote);
        Intent invokeIntent=getIntent();
        newNote=invokeIntent.getStringExtra("note");
        if(newNote==null)newNote="";
        editNote.setText(newNote);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent=new Intent(this, MainActivity.class);
        newNote=editNote.getText().toString();
        backIntent.putExtra("newNote", newNote);
        setResult(1, backIntent);
        Log.i("EditNote", "new Note sent");
        super.onBackPressed();

    }
}
