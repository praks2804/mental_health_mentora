package com.request;

public class JournalRequest {
    private int user_id;
    private String prompt_text;
    private String journal_text;

    public JournalRequest(int user_id, String prompt_text, String journal_text) {
        this.user_id = user_id;
        this.prompt_text = prompt_text;
        this.journal_text = journal_text;
    }
}

