package com.model;

public class JournalItem {
    private String journalentryyyy;
    private String timeDisplay;
    private String dateDisplay;

    // ✅ Constructor
    public JournalItem(String journalentryyyy, String timeDisplay, String dateDisplay) {
        this.journalentryyyy = journalentryyyy;
        this.timeDisplay = timeDisplay;
        this.dateDisplay = dateDisplay;
    }

    // ✅ Getter & Setter for journalentryyyy
    public String getJournalentryyyy() {
        return journalentryyyy;
    }

    public void setJournalentryyyy(String journalentryyyy) {
        this.journalentryyyy = journalentryyyy;
    }

    // ✅ Getter & Setter for timeDisplay
    public String getTimeDisplay() {
        return timeDisplay;
    }

    public void setTimeDisplay(String timeDisplay) {
        this.timeDisplay = timeDisplay;
    }

    // ✅ Getter & Setter for dateDisplay
    public String getDateDisplay() {
        return dateDisplay;
    }

    public void setDateDisplay(String dateDisplay) {
        this.dateDisplay = dateDisplay;
    }
}
