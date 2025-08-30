package com.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JournalResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // For fetching list of journals
    @SerializedName("data")
    private List<JournalItem> data;

    // For newly inserted journal ID
    @SerializedName("entry_id")
    private int entry_id;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<JournalItem> getData() {
        return data;
    }

    public int getEntry_id() {
        return entry_id;
    }

    // Inner class for each journal entry
    public static class JournalItem {
        @SerializedName("id")
        private String id;

        @SerializedName("title")
        private String title;

        @SerializedName("text")
        private String text;

        @SerializedName("date")
        private String date;

        @SerializedName("time")
        private String time;

        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getText() { return text; }
        public String getDateDisplay() { return date; }
        public String getTimeDisplay() { return time; }
    }
}
