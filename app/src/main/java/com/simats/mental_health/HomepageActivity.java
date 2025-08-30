package com.simats.mental_health;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.responses.SuggestionResponse;
import com.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomepageActivity extends AppCompatActivity {

    // Bottom buttons
    private ImageButton journalBtn, AiBtn, ProgressBtnBtn, ProfileBtn;

    // Drawer
    private ImageView menuIcon;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    // Suggestion card
    private LinearLayout suggestionCard;
    private TextView suggestionTitle, suggestionDesc;
    private Button startNowBtn;
    private String selectedMood = "";

    // Emoji Buttons
    private AppCompatButton happyBtn, calmBtn, sadBtn, AngryBtn, TiredBtn,
            NeutralBtn, LovingBtn, HeartbrokenBtn, AnxiousBtn, ConfusedBtn;

    // Retrofit
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homepage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //-----------------------------------------------------------
        // 0) SETUP RETROFIT
        //-----------------------------------------------------------
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.211.101.34/mentora/")   // must end with '/'
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        //-----------------------------------------------------------
        // 1) FIND YOUR VIEWS
        //-----------------------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        menuIcon = findViewById(R.id.menuIcon);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);


        journalBtn = findViewById(R.id.journalBtn);
        AiBtn = findViewById(R.id.AiBtn);
        ProgressBtnBtn = findViewById(R.id.ProgressBtnBtn);
        ProfileBtn = findViewById(R.id.ProfileBtn);

        suggestionCard = findViewById(R.id.suggestionCard);
        suggestionDesc = findViewById(R.id.suggestionDesc);
        startNowBtn = findViewById(R.id.startNowBtn);

        happyBtn = findViewById(R.id.happyBtn);
        calmBtn = findViewById(R.id.calmBtn);
        sadBtn = findViewById(R.id.sadBtn);
        AngryBtn = findViewById(R.id.AngryBtn);
        TiredBtn = findViewById(R.id.TiredBtn);
        NeutralBtn = findViewById(R.id.NeutralBtn);
        LovingBtn = findViewById(R.id.LovingBtn);
        HeartbrokenBtn = findViewById(R.id.HeartbrokenBtn);
        AnxiousBtn = findViewById(R.id.AnxiousBtn);
        ConfusedBtn = findViewById(R.id.ConfusedBtn);

        //-----------------------------------------------------------
        // 2) EMOJI BUTTON LOGIC
        //-----------------------------------------------------------
        happyBtn.setOnClickListener(v -> showSuggestion("Happy"));
        calmBtn.setOnClickListener(v -> showSuggestion("Calm"));
        sadBtn.setOnClickListener(v -> showSuggestion("Sad"));
        AngryBtn.setOnClickListener(v -> showSuggestion("Angry"));
        TiredBtn.setOnClickListener(v -> showSuggestion("Tired"));
        NeutralBtn.setOnClickListener(v -> showSuggestion("Neutral"));
        LovingBtn.setOnClickListener(v -> showSuggestion("Loving"));
        HeartbrokenBtn.setOnClickListener(v -> showSuggestion("Heartbroken"));
        AnxiousBtn.setOnClickListener(v -> showSuggestion("Anxious"));
        ConfusedBtn.setOnClickListener(v -> showSuggestion("Confused"));

        // redirect suggestion
        startNowBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SuggestionDetailActivity.class);
            intent.putExtra("mood", selectedMood);
            startActivity(intent);
        });

        //-----------------------------------------------------------
        // 3) BOTTOM NAV BUTTONS
        //-----------------------------------------------------------
        // Journal button → navigate to JournalEntriesActivity


        journalBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, JournalEntriesActivity.class);
            startActivity(intent);
        });

        AiBtn.setOnClickListener(v ->
                startActivity(new Intent(HomepageActivity.this, ChatActivity.class)));

        ProgressBtnBtn.setOnClickListener(v ->
                startActivity(new Intent(HomepageActivity.this, ProgressOverviewActivity.class)));

        ProfileBtn.setOnClickListener(v ->
                startActivity(new Intent(HomepageActivity.this, ProfilePageActivity.class)));

        //-----------------------------------------------------------
        // 4) NAVIGATION DRAWER
        //-----------------------------------------------------------
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        menuIcon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else
                drawerLayout.openDrawer(GravityCompat.START);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);

            if (id == R.id.nav_home) {
                startActivity(new Intent(HomepageActivity.this, HomepageActivity.class));
            } else if (id == R.id.nav_savings_plan) {
                startActivity(new Intent(HomepageActivity.this, MeditationLibraryActivity.class));
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(HomepageActivity.this, LogoPageActivity.class));
                finish();
            }
            overridePendingTransition(0, 0);
            return true;
        });
    }

    //-----------------------------------------------------------
    // RETROFIT FUNCTION
    //-----------------------------------------------------------
    private void showSuggestion(String mood) {
        selectedMood = mood;

        Call<SuggestionResponse> call = apiService.saveMood(1, mood);
        call.enqueue(new Callback<SuggestionResponse>() {
            @Override
            public void onResponse(Call<SuggestionResponse> call, Response<SuggestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SuggestionResponse res = response.body();

                    suggestionCard.setVisibility(View.VISIBLE);
                    suggestionDesc.setText(res.getSuggestionText());

                    startNowBtn.setOnClickListener(v -> {
                        try {
                            Class<?> cls = Class.forName("com.simats.mental_health." + res.getRedirectPage());
                            Intent intent = new Intent(HomepageActivity.this, cls);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<com.responses.SuggestionResponse> call, Throwable t) {
                t.printStackTrace();
                suggestionCard.setVisibility(View.VISIBLE);
                suggestionDesc.setText("Because you're feeling " + mood + ", we suggest doing this activity.");
            }
        });
    }
}
