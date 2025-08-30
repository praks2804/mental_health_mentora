package com.simats.mental_health;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SuggestionDetailActivity extends AppCompatActivity {

    TextView detailTitle, detailDesc;
    Button startMeditationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggestion_detail);

        // For Edge-to-Edge view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //--------------------------------------------
        // Find Views
        //--------------------------------------------
        detailTitle = findViewById(R.id.detailTitle);
        detailDesc = findViewById(R.id.detailDesc);
        startMeditationBtn = findViewById(R.id.startMeditationBtn);

        //--------------------------------------------
        // Receive mood from Intent
        //--------------------------------------------
        String mood = getIntent().getStringExtra("mood");

        //--------------------------------------------
        // Content Logic based on mood
        //--------------------------------------------
        detailTitle.setText(mood);

        switch (mood) {
            case "Sad":
                detailDesc.setText("Try a relaxing meditation to feel better 😊");
                break;
            case "Angry":
                detailDesc.setText("Do a deep breathing activity to calm down.");
                break;
            case "Tired":
                detailDesc.setText("Do a quick 5-minute recharge meditation.");
                break;
            case "Happy":
                detailDesc.setText("Keep the vibes going with a gratitude journaling activity!");
                break;
            case "Calm":
                detailDesc.setText("Stay relaxed with a mindfulness session.");
                break;
            case "Neutral":
                detailDesc.setText("Try an uplifting activity to brighten your day.");
                break;
            case "Loving":
                detailDesc.setText("Spread the love with a heart-warming exercise!");
                break;
            case "Heartbroken":
                detailDesc.setText("Heal your heart with a gentle breathing meditation.");
                break;
            case "Anxious":
                detailDesc.setText("Reduce anxiety with slow breathing and guided meditation.");
                break;
            case "Confused":
                detailDesc.setText("Clear your head with a calm focus session.");
                break;
            default:
                detailDesc.setText("This activity will suit your mood!");
        }

        //--------------------------------------------
        // Start Now Button
        //--------------------------------------------
        startMeditationBtn.setOnClickListener(v -> {
            // TODO → Open your meditation video page OR activity here
        });
    }
}
