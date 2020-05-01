package com.example.simplenote;

public class Note {
    private int id;
    private String text;

    public Note(String text) {
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }


    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
