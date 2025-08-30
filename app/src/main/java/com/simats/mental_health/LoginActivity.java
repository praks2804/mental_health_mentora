package com.simats.mental_health;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.retrofit.ApiClient;
import com.retrofit.ApiService;
import com.request.LoginRequest;
import com.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private AppCompatButton loginButton;

    private static final String USER_PREFS_NAME = "MentoraPrefs";
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        apiService = ApiClient.getClient().create(ApiService.class);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginBtn);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            performLogin(email, password);
        });
    }

    private void performLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isStatus()) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // Fetch user details
                        String userId = loginResponse.getData().getId();
                        String fullName = loginResponse.getData().getName();
                        String emailFromResponse = loginResponse.getData().getEmail();

                        // Save user data in SharedPreferences
                        SharedPreferences prefs = getSharedPreferences(USER_PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("user_id", userId);
                        editor.putString("full_name", fullName);
                        editor.putString("email", emailFromResponse);
                        editor.putBoolean("is_logged_in", true);
                        editor.apply();

                        // Navigate to HomepageActivity
                        Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                        intent.putExtra("user_id", userId);
                        intent.putExtra("full_name", fullName);
                        intent.putExtra("email", emailFromResponse);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Server error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
