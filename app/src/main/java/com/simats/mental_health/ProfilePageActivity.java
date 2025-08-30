package com.simats.mental_health;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.retrofit.RetrofitClient;

import com.model.UserProfile;
import com.responses.GetProfileResponse;
import com.retrofit.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePageActivity extends AppCompatActivity {
    private TextView tvName, tvEmail, tvDob, tvGender;
    private SwitchCompat swNotifications, swDarkMode;
    private SharedPreferences prefs;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("mentora_prefs", MODE_PRIVATE);
        boolean dark = prefs.getBoolean("DARK_MODE", false);
        AppCompatDelegate.setDefaultNightMode(dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        apiService = RetrofitClient.getApiService();


        tvName = findViewById(R.id.textName);
        tvEmail = findViewById(R.id.textEmail);
        tvDob = findViewById(R.id.textDOB);
        tvGender = findViewById(R.id.textGender);

        Switch swNotifications;
        Switch swDarkMode;

        swNotifications = findViewById(R.id.switchNotification);
        swDarkMode = findViewById(R.id.switch_darkmode);
        // ✅ matches XML


        ImageView btnEdit = findViewById(R.id.edit_profile);


        // toggles
        swNotifications.setChecked(prefs.getBoolean("NOTIFICATIONS_ENABLED", true));
        swNotifications.setOnCheckedChangeListener((btn, checked) ->
                prefs.edit().putBoolean("NOTIFICATIONS_ENABLED", checked).apply()
        );

        swDarkMode.setChecked(dark);
        swDarkMode.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean("DARK_MODE", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        });

        btnEdit.setOnClickListener(v -> {
            startActivity(new Intent(ProfilePageActivity.this, EditProfileActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProfile();
    }

    private void fetchProfile() {
        int userId = prefs.getInt("USER_ID", -1);
        if (userId <= 0) { Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show(); return; }

        Map<String, Integer> body = new HashMap<>();
        body.put("user_id", userId);

        apiService.getProfile(body).enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(ProfilePageActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    return;
                }
                GetProfileResponse res = response.body();
                if (!res.status) {
                    Toast.makeText(ProfilePageActivity.this, res.message != null ? res.message : "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserProfile u = res.data;
                tvName.setText(u.name);
                tvEmail.setText(u.email);
                tvDob.setText(u.dob);      // dd-mm-yyyy (backend formatted)
                tvGender.setText(u.gender);

                // Cache locally
                prefs.edit()
                        .putString("NAME", u.name)
                        .putString("EMAIL", u.email)
                        .putString("DOB", u.dob)
                        .putString("GENDER", u.gender)
                        .apply();
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                Toast.makeText(ProfilePageActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // fallback: populate from prefs
                tvName.setText(prefs.getString("NAME","-"));
                tvEmail.setText(prefs.getString("EMAIL","-"));
                tvDob.setText(prefs.getString("DOB","-"));
                tvGender.setText(prefs.getString("GENDER","-"));
            }
        });
    }
}
