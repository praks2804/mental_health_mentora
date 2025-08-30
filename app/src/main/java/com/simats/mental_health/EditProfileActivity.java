package com.simats.mental_health;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.retrofit.RetrofitClient;

import com.request.UpdateProfileRequest;
import com.responses.BasicResponse;
import com.retrofit.ApiService;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etName;
    private TextView tvDob;
    private Spinner spGender;
    private Button btnSave;

    private ApiService apiService; // your Retrofit interface
    private int userId = 1; // TODO: replace with actual logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        etName = findViewById(R.id.editName);
        tvDob = findViewById(R.id.editDOB);
        spGender = findViewById(R.id.spinnerGender);
        btnSave = findViewById(R.id.btnSave);

        // Date picker
        tvDob.setOnClickListener(v -> showDatePicker());

        // Save button
        btnSave.setOnClickListener(v -> saveProfile());

        apiService = RetrofitClient.getClient().create(ApiService.class);

    }

    private void showDatePicker() {
        final Calendar cal = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String dd = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month + 1, year);
            tvDob.setText(dd);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String dob = tvDob.getText().toString().trim();
        String gender = spGender.getSelectedItem().toString();

        if (name.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateProfileRequest req = new UpdateProfileRequest(userId, name, dob, gender);
        apiService.updateProfile(req).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().status) {
                    Toast.makeText(EditProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish(); // go back
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
