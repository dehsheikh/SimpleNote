package com.example.simplenote;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final int RESULT_SAVE_NOTE = 0;
    public static final int RESULT_DELETE_NOTE = 1;

    public static final String EXTRA_ID =
            "com.example.simplenote.EXTRA_ID";
    public static final String EXTRA_TEXT =
            "com.example.simplenote.EXTRA_TEXT";
    public static final String EXTRA_NOTE_DELETED =
            "com.example.simplenote.EXTRA_NOTE_DELETED";


    private EditText noteText;
    private NoteDatabase db;
    private boolean isThisEditNoteActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        if (getIntent().hasExtra(EXTRA_ID)) {
            isThisEditNoteActivity = true;
            initializeEditNoteActivity();
        } else {
            initializeAddNoteActivity();
        }


        if (savedInstanceState != null) {
            noteText.setText(savedInstanceState.get("note_text").toString());
        }
    }

    //Which activity should start? add or edit?
    private void initializeAddNoteActivity() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Note");
        noteText = findViewById(R.id.edit_text_note_text);
        db = NoteDatabase.getInstance(this.getApplicationContext());
    }

    private void initializeEditNoteActivity() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Note");
        db = NoteDatabase.getInstance(this.getApplicationContext());
        noteText = findViewById(R.id.edit_text_note_text);
        noteText.setText(getIntent().getStringExtra(EXTRA_TEXT));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("note_text", noteText.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private String trimTextViewData() {
        String text = noteText.getText().toString();
        if (!text.isEmpty()) {
            return text;
        }
        return "no text";
    }

    private void addNote() {
        Intent data = new Intent();
        data.putExtra(EXTRA_TEXT, noteText.getText().toString());
        db.insert(new Note(noteText.getText().toString()));
        setResult(RESULT_SAVE_NOTE, data);
        finish();
    }

    private void updateNote() {
        Intent data = new Intent();
        Note newNote = new Note(noteText.getText().toString());
        newNote.setId(getIntent().getIntExtra(EXTRA_ID, -1));
        data.putExtra(EXTRA_TEXT, noteText.getText().toString());
        db.update(newNote);
        setResult(RESULT_SAVE_NOTE, data);
        finish();
    }

    private void deleteNote() {
        new AlertDialog.Builder(this)
                .setMessage("Delete this note?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data = new Intent();
                        data.putExtra(EXTRA_TEXT, EXTRA_NOTE_DELETED);
                        db.deleteNote(getIntent().getIntExtra(EXTRA_ID, -1));
                        setResult(RESULT_DELETE_NOTE, data);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
        .show();
    }

    private void backButtonPressed() {
        if (!trimTextViewData().equals("no text")) {
            if (isThisEditNoteActivity) {
                updateNote();
                return;
            }
            addNote();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                deleteNote();
                break;
            default:
                backButtonPressed();
        }
        //This doesn't consume event. Just returns false.
        return super.onOptionsItemSelected(item);
//        return true;
    }

    @Override
    public void onBackPressed() {
        backButtonPressed();
//        This sent null intent to parent activity so commented it.
//        super.onBackPressed();
    }
}
