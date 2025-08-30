package com.simats.mental_health;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.model.JournalEntryyyy;
import com.retrofit.ApiService;
import com.retrofit.RetrofitClient;
import com.responses.JournalResponse;
import com.simats.mental_health.adapter.JournalAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyJournalActivity extends AppCompatActivity {

    private RecyclerView rvJournal;
    private JournalAdapter adapter;
    private List<JournalEntryyyy> journalList;

    private static final String USER_PREFS_NAME = "MentoraPrefs";
    private int userId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_journal);

        // ✅ get current user
        SharedPreferences prefs = getSharedPreferences(USER_PREFS_NAME, MODE_PRIVATE);
        String userIdStr = prefs.getString("user_id", "0");
        userId = Integer.parseInt(userIdStr);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        rvJournal = findViewById(R.id.rvJournal);
        rvJournal.setLayoutManager(new LinearLayoutManager(this));

        journalList = new ArrayList<>();
//        adapter = new JournalAdapter(this, journalList);
        rvJournal.setAdapter(adapter);

        // ✅ fetch journals from backend
//        fetchUserJournals();
    }

//    private void fetchUserJournals() {
//        Call<JournalResponse> call = apiService.getUserJournals(userId);
//        call.enqueue(new Callback<JournalResponse>() {
//            @Override
//            public void onResponse(Call<JournalResponse> call, Response<JournalResponse> response) {
//                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
//                    journalList.clear();
//                    for (JournalResponse.JournalItem item : response.body().getData()) {
//                        Log.d("MyJournalActivity", "Journal item -> " +
//                                "date=" + item.getDate() +
//                                ", time=" + item.getTime() +
//                                ", title=" + item.getTitle() +
//                                ", text=" + item.getText());
//
//                        journalList.add(new JournalEntryyyy(
//                                item.getTime(),
//                                item.getDate(),
//                                item.getTitle(),
//                                item.getText()
//                        ));
//                    }
//
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JournalResponse> call, Throwable t) {
//                Log.e("MyJournalActivity", "Failed to fetch journals: " + t.getMessage());
//            }
//        });
//    }
}
