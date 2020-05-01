package com.example.simplenote;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notesList = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public NoteAdapter(Context context) {
        notesList = NoteDatabase.getInstance(context).getAllNotes();
        this.context = context;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView noteText;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteText = itemView.findViewById(R.id.text_view_note);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(notesList.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View noteView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_layout, viewGroup, false);
        return new NoteViewHolder(noteView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder noteViewHolder, int i) {
        Note currentNote = notesList.get(i);
        noteViewHolder.noteText.setText(currentNote.getText());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void notifyAdapter() {
        notesList = NoteDatabase.getInstance(context).getAllNotes();
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
