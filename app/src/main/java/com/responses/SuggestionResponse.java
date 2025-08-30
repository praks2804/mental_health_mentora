package com.responses;

import com.google.gson.annotations.SerializedName;

public class SuggestionResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("suggestion_text")
    private String suggestionText;

    @SerializedName("redirect_page")
    private String redirectPage;

    public boolean isSuccess() {
        return success;
    }

    public String getSuggestionText() {
        return suggestionText;
    }

    public String getRedirectPage() {
        return redirectPage;
    }
}
