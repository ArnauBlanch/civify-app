package com.civify.model.achievement;

/**
 * Created by MSI on 01/06/2017.
 */

public class AchievementProgressStub {

    private int mId;
    private int mUserId;
    private int mAchievementId;
    private int mProgress;
    private boolean mCompleted;
    private boolean mClaimed;

    public AchievementProgressStub(int id, int userId, int achievementId, int progress,
            boolean completed, boolean claimed) {
        mId = id;
        mUserId = userId;
        mAchievementId = achievementId;
        mProgress = progress;
        mCompleted = completed;
        mClaimed = claimed;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getAchievementId() {
        return mAchievementId;
    }

    public void setAchievementId(int achievementId) {
        mAchievementId = achievementId;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public boolean isClaimed() {
        return mClaimed;
    }

    public void setClaimed(boolean claimed) {
        mClaimed = claimed;
    }
}
