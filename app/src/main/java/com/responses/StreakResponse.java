package com.responses;

import java.util.List;

public class StreakResponse {
    private boolean success;
    private int current_streak;
    private List<Integer> weekly_progress;

    public boolean isSuccess() {
        return success;
    }

    public int getStreak() {
        return current_streak;
    }

    public List<Integer> getWeeklyProgress() {
        return weekly_progress;
    }
}
