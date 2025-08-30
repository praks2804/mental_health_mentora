package com.simats.mental_health;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.request.JournalRequest;
import com.responses.JournalResponse;
import com.retrofit.ApiClient;
import com.retrofit.ApiService;

import retrofit2.Call;

public class JournalTypingActivity extends AppCompatActivity {

    private TextView journalEntryNumber, userName, tvPromptQuestion;
    private EditText entryInput;
    private Button saveJournalBtn;
    private ImageView backArrow;
    SharedPreferences prefs;
    private static final String USER_PREFS_NAME = "MentoraPrefs";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("MentoraPrefs", MODE_PRIVATE);
        String userIdStr = prefs.getString("user_id", "-1"); // read as String
        int userId = -1;
        try {
            userId = Integer.parseInt(userIdStr); // convert to int
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.journal_typing);

        // Initialize views
        journalEntryNumber = findViewById(R.id.journalEntryNumber);
        userName = findViewById(R.id.userName);
        tvPromptQuestion = findViewById(R.id.promptQuestionTV); // <-- NEW textview from xml
        entryInput = findViewById(R.id.entryInput);
        saveJournalBtn = findViewById(R.id.saveJournalBtn);
        backArrow = findViewById(R.id.backArrow);

        // Get the data passed from prompts or elsewhere
        String promptText = getIntent().getStringExtra("PROMPT_TEXT");
        String userFullName = getIntent().getStringExtra("USER_NAME");

        // Show the passed prompt if available
        if (promptText != null && !promptText.isEmpty()) {
            tvPromptQuestion.setText(promptText);
            tvPromptQuestion.setVisibility(View.VISIBLE);
        } else {
            tvPromptQuestion.setVisibility(View.GONE);
        }

        // Set username if available
        if (userFullName != null) {
            userName.setText(userFullName);
        }

        // Back button
        backArrow.setOnClickListener(v -> onBackPressed());

        // Save button click
        int finalUserId = userId;
        saveJournalBtn.setOnClickListener(v -> {
            String entryText = entryInput.getText().toString().trim();
            String prompt = tvPromptQuestion.getText().toString().trim();

            if (entryText.isEmpty()) {
                Toast.makeText(JournalTypingActivity.this, "Please write something.", Toast.LENGTH_SHORT).show();
            } else {
                // Create request object
//                int userId = sharedPreferences.getInt("USER_ID", 0); // get from login session
                JournalRequest request = new JournalRequest(finalUserId, prompt, entryText);

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                apiService.saveJournal(request).enqueue(new retrofit2.Callback<JournalResponse>() {
                    @Override
                    public void onResponse(Call<JournalResponse> call, retrofit2.Response<JournalResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            JournalResponse res = response.body();
                            if (res.isSuccess()) {
                                Toast.makeText(JournalTypingActivity.this, "Saved! Entry ID: " + res.getEntry_id(), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(JournalTypingActivity.this, "Error: " + res.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(JournalTypingActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JournalResponse> call, Throwable t) {
                        Toast.makeText(JournalTypingActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
