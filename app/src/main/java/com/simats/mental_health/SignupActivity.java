package com.simats.mental_health;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.retrofit.ApiClient;
import com.retrofit.ApiService;
import com.request.SignupRequest;
import com.responses.SignupResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText editName, editEmail, editPassword;
    private Button submitbtn;

    private static final String USER_PREFS_NAME = "MentoraPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editName     = findViewById(R.id.editName);
        editEmail    = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        submitbtn    = findViewById(R.id.submitbtn);

        submitbtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name     = editName.getText().toString().trim();
        String email    = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        SignupRequest request = new SignupRequest(name, email, password);

        apiService.registerUser(request).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SignupResponse signupResponse = response.body();

                    if (signupResponse.isStatus()) {
                        Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                        // ✅ Save user details for multi-user support
                        int userId   = signupResponse.getData().getId();
                        String fullName = signupResponse.getData().getName();
                        String emailResp = signupResponse.getData().getEmail();

                        // Use user-specific key prefix for multi-user support
                        SharedPreferences prefs = getSharedPreferences(USER_PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("user_" + userId + "_id", userId);
                        editor.putString("user_" + userId + "_full_name", fullName);
                        editor.putString("user_" + userId + "_email", emailResp);

                        // Optionally, track last logged-in user
                        editor.putInt("last_logged_in_user_id", userId);

                        editor.apply();

                        // 🔁 Navigate to StartAssessmentActivity
                        Intent intent = new Intent(SignupActivity.this, StartAssessmentActivity.class);
                        intent.putExtra("user_id", userId);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(SignupActivity.this, signupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SignupActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
