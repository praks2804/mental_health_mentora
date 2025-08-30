package com.responses;

import java.util.List;

public class PromptResponse {
    private String day;
    private List<PromptItem> prompts;

    public String getDay() {
        return day;
    }

    public List<PromptItem> getPrompts() {
        return prompts;
    }

    public static class PromptItem {
        private int id;
        private String prompt_text;

        public int getId() { return id; }
        public String getPrompt_text() { return prompt_text; }
    }
}
