package com.retrofit;

import com.request.JournalRequest;
import com.request.LoginRequest;
import com.request.SignupRequest;
import com.request.UpdateProfileRequest;
import com.responses.BasicResponse;
import com.responses.GetProfileResponse;
import com.responses.JournalResponse;
import com.responses.LoginResponse;
import com.responses.PromptResponse;
import com.responses.SignupResponse;
import com.responses.StreakResponse;
import com.responses.SuggestionResponse;
import com.responses.TodayJournalResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("sign_up.php")  // endpoint file name
    Call<SignupResponse> registerUser(@Body SignupRequest request);

    //login
    @Headers("Content-Type: application/json")
    @POST("login.php")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    //mood+emojie
    @GET("save_mood.php")
    Call<SuggestionResponse> saveMood(
            @Query("user_id") int userId,
            @Query("mood") String mood
    );

    @GET("endpoint")
    Call<ResponseBody> getData();

    //streaks
    @GET("get_streaks.php")
    Call<StreakResponse> getStreak(@Query("user_id") int userId);

    //daily prompts in journal
    @GET("get_prompts.php")
    Call<PromptResponse> getPrompts(@Query("user_id") int userId);

    //journals
    @GET("get_journals.php")
    Call<JournalResponse> getJournals(@Query("user_id") int userId);

    @Headers("Content-Type: application/json")
    @POST("save_journal.php")
    Call<JournalResponse> saveJournal(@Body JournalRequest journalRequest);

    // Get today's journal for a user
    @GET("get_today_journal.php")
    Call<TodayJournalResponse> getTodayJournal(@Query("user_id") int userId);

    //Profile
    @POST("get_profile.php")
    Call<GetProfileResponse> getProfile(@Body Map<String, Integer> body);

    @POST("update_profile.php")
    Call<BasicResponse> updateProfile(@Body UpdateProfileRequest body);

    @GET("get_journals.php")
    Call<JournalResponse> getUserJournals(@Query("user_id") int userId);


}
