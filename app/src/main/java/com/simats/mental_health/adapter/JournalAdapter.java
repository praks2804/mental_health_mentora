package com.simats.mental_health.adapter;


import static android.os.Build.VERSION_CODES.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.model.JournalEntryyyy;
import com.responses.JournalResponse;
import com.simats.mental_health.MyJournalActivity;
import com.simats.mental_health.R;  // ✅ Correct import for your app resources
import com.responses.JournalResponse; // ✅ import the response model

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private List<JournalResponse.JournalItem> journalList;
    // ✅ Constructor
    public JournalAdapter(List<JournalResponse.JournalItem> journalList) {
        this.journalList = journalList;
    }

    public JournalAdapter(MyJournalActivity myJournalActivity, List<JournalEntryyyy> journalList) {
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        JournalResponse.JournalItem item = journalList.get(position);

        // ✅ Date & Time
        holder.tvTime.setText(item.getTimeDisplay());
        holder.tvDateChip.setText(item.getDateDisplay());

        // ✅ Title (Prompt)
        if (item.getTitle() != null && !item.getTitle().trim().isEmpty()) {
            holder.tvPrompt.setVisibility(View.VISIBLE);
            holder.tvPrompt.setText(item.getTitle());
        } else {
            holder.tvPrompt.setVisibility(View.GONE);
        }

        // ✅ Body (Journal Text)
        if (item.getText() != null && !item.getText().trim().isEmpty()) {
            holder.tvJournal.setVisibility(View.VISIBLE);
            holder.tvJournal.setText(item.getText());
        } else {
            holder.tvJournal.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return journalList != null ? journalList.size() : 0;
    }

    // ✅ ViewHolder
    public static class JournalViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvDateChip, tvPrompt, tvJournal;

        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R);
            tvDateChip = itemView.findViewById(R);
            tvPrompt = itemView.findViewById(R);
//            tvJournal = itemView.findViewById(R.id.tvJournal);
            tvTime = itemView.findViewById(R);
            tvDateChip = itemView.findViewById(R);
            tvPrompt = itemView.findViewById(R);
            tvJournal = itemView.findViewById(R);
        }
    }

    // ✅ Optional: method to update data dynamically
    public void updateData(List<JournalResponse.JournalItem> newList) {
        this.journalList = newList;
        notifyDataSetChanged();
    }
}
