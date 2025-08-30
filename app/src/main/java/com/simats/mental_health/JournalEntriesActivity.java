package com.simats.mental_health;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.responses.StreakResponse;
import com.responses.PromptResponse;
import com.retrofit.ApiService;
import com.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JournalEntriesActivity extends AppCompatActivity {
    private TextView streakText, promptText, prompt1, prompt2, prompt3;
    private Button startJournalEntryBtn, myJournalBtn;
    private ApiService apiService;
    private int userId;
    private String userName;

    private TextView[] weekCircles = new TextView[7];

    private static final String USER_PREFS_NAME = "MentoraPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_entries);

        // ✅ Get current logged-in user
        SharedPreferences prefs = getSharedPreferences(USER_PREFS_NAME, MODE_PRIVATE);
        String userIdStr = prefs.getString("user_id", "0"); // default 0 if not logged in
        userId = Integer.parseInt(userIdStr);
        userName = prefs.getString("full_name", "User");

        streakText = findViewById(R.id.streakNumber);
        promptText = findViewById(R.id.promptText);

        prompt1 = findViewById(R.id.prompt1);
        prompt2 = findViewById(R.id.prompt2);
        prompt3 = findViewById(R.id.prompt3);

        startJournalEntryBtn = findViewById(R.id.startJournalEntryBtn);
        myJournalBtn = findViewById(R.id.myJournalBtn);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Map week circles
        weekCircles[0] = findViewById(R.id.daySun);
        weekCircles[1] = findViewById(R.id.dayMon);
        weekCircles[2] = findViewById(R.id.dayTue);
        weekCircles[3] = findViewById(R.id.dayWed);
        weekCircles[4] = findViewById(R.id.dayThu);
        weekCircles[5] = findViewById(R.id.dayFri);
        weekCircles[6] = findViewById(R.id.daySat);

        // Fetch streaks & prompts
        fetchStreak();
        fetchPrompts();

        // Start Journal without prompt
        startJournalEntryBtn.setOnClickListener(v -> openTypingPage(""));

        // My Journal page
        myJournalBtn.setOnClickListener(v -> startActivity(new Intent(this, MyJournalActivity.class)));

        // Prompt clicks
        prompt1.setOnClickListener(v -> openTypingPage(prompt1.getText().toString()));
        prompt2.setOnClickListener(v -> openTypingPage(prompt2.getText().toString()));
        prompt3.setOnClickListener(v -> openTypingPage(prompt3.getText().toString()));
    }

    private void openTypingPage(String prompt) {
        Intent intent = new Intent(this, JournalTypingActivity.class);
        intent.putExtra("PROMPT_TEXT", prompt);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }

    private void fetchStreak() {
        Call<StreakResponse> call = apiService.getStreak(userId);
        call.enqueue(new Callback<StreakResponse>() {
            @Override
            public void onResponse(Call<StreakResponse> call, Response<StreakResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int streak = response.body().getStreak();
                    streakText.setText("🔥 Streak: " + streak);
                    List<Integer> weeklyProgress = response.body().getWeeklyProgress();
                    if (weeklyProgress != null && weeklyProgress.size() == 7) updateWeeklyCircles(weeklyProgress);
                } else streakText.setText("🔥 Streak: 0");
            }

            @Override
            public void onFailure(Call<StreakResponse> call, Throwable t) {
                streakText.setText("🔥 Streak: 0");
                Log.e("API_FAIL", t.getMessage(), t);
            }
        });
    }

    private void updateWeeklyCircles(List<Integer> weeklyProgress) {
        for (int i = 0; i < 7; i++) {
            weekCircles[i].setBackgroundResource(weeklyProgress.get(i) == 1 ?
                    R.drawable.journal_circle_filled : R.drawable.journal_circle_empty);
        }
    }

    private void fetchPrompts() {
        Call<PromptResponse> call = apiService.getPrompts(userId);
        call.enqueue(new Callback<PromptResponse>() {
            @Override
            public void onResponse(Call<PromptResponse> call, Response<PromptResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PromptResponse.PromptItem> prompts = response.body().getPrompts();
                    if (prompts != null && !prompts.isEmpty()) {
                        if (prompts.size() > 0) prompt1.setText(prompts.get(0).getPrompt_text());
                        if (prompts.size() > 1) prompt2.setText(prompts.get(1).getPrompt_text());
                        if (prompts.size() > 2) prompt3.setText(prompts.get(2).getPrompt_text());
                        promptText.setText("Today's Prompts (" + response.body().getDay() + ")");
                    } else {
                        prompt1.setText("No prompt");
                        prompt2.setText("No prompt");
                        prompt3.setText("No prompt");
                        promptText.setText("No prompts today.");
                    }
                }
            }

            @Override
            public void onFailure(Call<PromptResponse> call, Throwable t) {
                promptText.setText("Error loading prompts.");
            }
        });
    }
}
