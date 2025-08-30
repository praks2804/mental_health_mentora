package com.responses;

import com.google.gson.annotations.SerializedName;

public class SaveJournalResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("entry_id")
    private int entry_id;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getEntry_id() {
        return entry_id;
    }
}
