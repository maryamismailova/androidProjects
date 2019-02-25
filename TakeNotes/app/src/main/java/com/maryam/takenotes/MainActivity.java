package com.maryam.takenotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ArrayList<String> notes=new ArrayList<>();
    int selectedNote=-1;
    ListView notesView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    public void openEditNoteActivity(){
        Intent addNote=new Intent(getApplicationContext(), EditNoteActivity.class);
        if(selectedNote!=-1){
            addNote.putExtra("note", notes.get(selectedNote));
        }
        startActivityForResult(addNote, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            if(data!=null){
                String newData=data.getStringExtra("newNote");
                if (selectedNote != -1) {
                    notes.remove(selectedNote);
                    if(!newData.equals("")) {
                        notes.add(selectedNote, newData);
                    }
                    selectedNote = -1;
                } else {
                    if(!newData.equals("")) {
                        notes.add(newData);
                    }
                }
                Log.i("New Data", newData);
                updateListView();
                updateSharedPreferences();
            }else{
                Log.i("Data", "NULL");
            }
        }else{
            Log.i("Result", "Incorrect res.code");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.newNote){
            Log.i("Menu", "add new note");
            openEditNoteActivity();
        }
        return true;
    }
    public void updateListView(){
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        notesView.setAdapter(arrayAdapter);

    }

    public void updateSharedPreferences(){
        try {
            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesView=findViewById(R.id.notesList);
        notesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long noteId=id;
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Delete").setMessage("Are you sure you want to delete the note?");
                alertDialog.setIcon(android.R.drawable.ic_delete);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove((int)noteId);
                        updateListView();
                        updateSharedPreferences();
                        Log.i("Remove", "Note "+noteId);
                    }
                });
                alertDialog.setNegativeButton("No", null);
                alertDialog.show();

                return true;
            }
        });
        notesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNote=position;
                openEditNoteActivity();
            }
        });
        sharedPreferences=getSharedPreferences("com.maryam.takenotes", Context.MODE_PRIVATE);
        String notesString=sharedPreferences.getString("notes", null);
        if(notesString!=null){
            try {
                notes= (ArrayList<String>)ObjectSerializer.deserialize(notesString);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        updateListView();


    }
}
