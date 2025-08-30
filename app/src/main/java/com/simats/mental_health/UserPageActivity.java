package com.simats.mental_health;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserPageActivity extends AppCompatActivity {

    private Button NoBtn;
    private Button YesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.user_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        NoBtn = findViewById(R.id.NoBtn);
        YesBtn = findViewById(R.id.YesBtn);

        // "No" button action
        if (NoBtn != null) {
            NoBtn.setOnClickListener(v -> {
                Intent intent = new Intent(UserPageActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }

        // "Yes" button action
        if (YesBtn != null) {
            YesBtn.setOnClickListener(v -> {
                Intent intent = new Intent(UserPageActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}
