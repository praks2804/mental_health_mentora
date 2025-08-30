package com.simats.mental_health;

import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.model.ChatMessage;
import com.request.GeminiRequest;
import com.responses.GeminiResponse;
import com.retrofit.GeminiApiClient;
import com.retrofit.GeminiApiService;
import com.simats.mental_health.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private ImageButton buttonSend;

    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private LinearLayout welcomeLayout;


    private static final String API_KEY = "AIzaSyDE_I-wdeByHlDmksCFz5tGp5owZzIkPxw"; // Your Gemini API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        welcomeLayout = findViewById(R.id.welcomeLayout);


        buttonSend.setOnClickListener(v -> {
            String userMessage = editTextMessage.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                // 👇 Hide the welcome layout
                if (welcomeLayout.getVisibility() == View.VISIBLE) {
                    welcomeLayout.setVisibility(View.GONE);
                }

                addMessage(new ChatMessage(userMessage, true)); // User message
                editTextMessage.setText("");
                sendMessageToGemini(userMessage);
            }
        });

        AppCompatButton btnSavingTips = findViewById(R.id.btnSavingTips);

//        AppCompatButton btnSavingTips = findViewById(R.id.btnSavingTips);
//        btnSavingTips.setOnClickListener(v -> {
//            String message = btnSavingTips.getText().toString();
//
//            // Hide welcome layout if visible
//            if (welcomeLayout.getVisibility() == View.VISIBLE) {
//                welcomeLayout.setVisibility(View.GONE);
//            }
//
//            // Add user message to chat
//            addMessage(new ChatMessage(message, true));
//
//            // Send message to Gemini
//            sendMessageToGemini(message);
//        });

        setupQuickSendButton(btnSavingTips);

    }

    private void setupQuickSendButton(AppCompatButton button) {
        button.setOnClickListener(v -> {
            String message = button.getText().toString();

            if (welcomeLayout.getVisibility() == View.VISIBLE) {
                welcomeLayout.setVisibility(View.GONE);
            }

            addMessage(new ChatMessage(message, true));
            sendMessageToGemini(message);
        });
    }

    private void addMessage(ChatMessage message) {
        chatMessages.add(message);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
    }

    private void sendMessageToGemini(String userMessage) {
        GeminiApiService apiService = GeminiApiClient.getClient().create(GeminiApiService.class);
        String endpoint = "models/gemini-2.5-flash:generateContent?key=" + API_KEY;
        GeminiRequest request = new GeminiRequest(userMessage);

        // Add typing indicator
        ChatMessage typingMessage = new ChatMessage(true); // isTyping = true
        chatMessages.add(typingMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerViewChat.scrollToPosition(chatMessages.size() - 1);

        apiService.generateContent(endpoint, request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                String reply = "⚠️ No reply from model";

                if (response.isSuccessful() && response.body() != null) {
                    GeminiResponse geminiResponse = response.body();

                    if (geminiResponse.candidates != null &&
                            !geminiResponse.candidates.isEmpty() &&
                            geminiResponse.candidates.get(0).content != null &&
                            geminiResponse.candidates.get(0).content.parts != null &&
                            !geminiResponse.candidates.get(0).content.parts.isEmpty()) {

                        reply = geminiResponse.candidates.get(0).content.parts.get(0).text;
                    }
                } else {
                    reply = "⚠️ Error: " + response.message();
                }

                // Replace typing indicator with bot response
                int index = chatMessages.indexOf(typingMessage);
                if (index != -1) {
                    chatMessages.set(index, new ChatMessage(reply, false));
                    chatAdapter.notifyItemChanged(index);
                    recyclerViewChat.scrollToPosition(index);
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                String failMsg = "❌ Failed: " + t.getMessage();
                int index = chatMessages.indexOf(typingMessage);
                if (index != -1) {
                    chatMessages.set(index, new ChatMessage(failMsg, false));
                    chatAdapter.notifyItemChanged(index);
                    recyclerViewChat.scrollToPosition(index);
                }
            }
        });
    }
}