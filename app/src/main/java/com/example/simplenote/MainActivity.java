package com.example.simplenote;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 11;
    public static final int EDIT_NOTE_REQUEST = 12;
    private NoteAdapter noteAdapter;
    private RecyclerView recyclerView;
    private NoteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting adapter
        noteAdapter = new NoteAdapter(this.getApplicationContext());
        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TEXT, note.getText());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });

        //Setting recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));

        //Setting floatingActionButton on screen
        FloatingActionButton button_add = findViewById(R.id.floatingActionButton);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        //Initializing database
        database = NoteDatabase.getInstance(this.getApplicationContext());

        Log.d("lifecycle", "____");
        Log.d("lifecycle", "onCreate");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_NOTE_REQUEST || requestCode == EDIT_NOTE_REQUEST) &&
                (resultCode == AddEditNoteActivity.RESULT_SAVE_NOTE ||
                        resultCode ==AddEditNoteActivity.RESULT_DELETE_NOTE )
                && data != null) {
            noteAdapter.notifyAdapter();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle", "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onResume");
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("lifecycle", "onSaveInstanceState");
    }
}
