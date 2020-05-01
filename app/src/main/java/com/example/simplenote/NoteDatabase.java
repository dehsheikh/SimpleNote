package com.example.simplenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database_note";
    public static final String TABLE_NOTE = "table_note";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    private static NoteDatabase instance;

    private NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        context.deleteDatabase(DATABASE_NAME);
    }

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new NoteDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTE_TABLE = "CREATE TABLE " +
                TABLE_NOTE + "(id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL, text TEXT)";
        db.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Inserting notes
    public void insert(Note note) {
        new inserAsyncTask().execute(note);
    }

    private class inserAsyncTask extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put("text", notes[0].getText());
                db.insert(TABLE_NOTE, null, values);
                db.setTransactionSuccessful();
                db.endTransaction();
            } catch (Exception e) {
                Log.d("Database Error", "Error in inserting a note");
                e.printStackTrace();
            }
            return null;
        }
    }

    //Retrieving all notes
    public List<Note> getAllNotes() {
        List<Note> notesList = new ArrayList<>();
        new getAllNotesAsyncTask().execute(notesList);
        return notesList;
    }

    private class getAllNotesAsyncTask extends AsyncTask<List<Note>, Void, Void> {
        @Override
        protected Void doInBackground(List<Note>... lists) {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTE, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String noteText = cursor.getString(
                                cursor.getColumnIndex("text"));
                        Note note = new Note(noteText);
                        note.setId(id);
                        lists[0].add(note);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //Update a note
    public void update(Note note) {
        new updateAsynTask().execute(note);
    }

    private class updateAsynTask extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {
            int id = notes[0].getId();
            String noteText = notes[0].getText();
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("text", noteText);

            db.beginTransaction();
            try {
                db.update(TABLE_NOTE, values, "id=" + id, null);
                db.setTransactionSuccessful();
                db.endTransaction();
            } catch (Exception e) {
                Log.d("Database Error", "Error in updating a note");
                e.printStackTrace();
            }
            return null;
        }
    }

    public void deleteNote(int noteId){
        new deleteNoteAsyncTask().execute(noteId);
    }

    private class deleteNoteAsyncTask extends AsyncTask<Integer, Void, Void>{
        @Override
        protected Void doInBackground(Integer... integers) {
            int id = integers[0];
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            try{
                db.delete(TABLE_NOTE, "id = " + id, null);
                db.setTransactionSuccessful();
                db.endTransaction();
            } catch (Exception e) {
                Log.d("Database Error", "Error in deleting a note");
                e.printStackTrace();
            }
            return null;
        }
    }
}
