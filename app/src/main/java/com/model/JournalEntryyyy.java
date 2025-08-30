package com.model;

public class JournalEntryyyy {
    private String time;
    private String date;
    private String title;
    private String text;

    public JournalEntryyyy(String time, String date, String title, String text) {
        this.time = time;
        this.date = date;
        this.title = title;
        this.text = text;
    }

    public String getTime() { return time; }
    public String getDate() { return date; }
    public String getTitle() { return title; }
    public String getText() { return text; }   // ✅ must be here
}
