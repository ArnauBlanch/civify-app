package com.civify.model.achievement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Achievement implements Serializable {

    @Expose
    @SerializedName("title")
    private String mTitle;

    @Expose
    @SerializedName("description")
    private String mDescription;

    @Expose
    @SerializedName("number")
    private int mNumber;

    @Expose
    @SerializedName("kind")
    private String mKind;

    @Expose
    @SerializedName("coins")
    private int mCoins;

    @Expose
    @SerializedName("xp")
    private int mXp;

    @Expose
    @SerializedName("created_at")
    private Date mCreatedAt;

    @Expose
    @SerializedName("achievement_token")
    private String mAchievementToken;

    @Expose
    @SerializedName("enabled")
    private boolean mEnabled;

    @Expose
    @SerializedName("progress")
    private int mProgress;

    @Expose
    @SerializedName("completed")
    private boolean mCompleted;

    @Expose
    @SerializedName("claimed")
    private boolean mClaimed;

    public Achievement() {
    }

    public Achievement(String title, String description, int number, String kind, int coins, int xp,
            Date createdAt, String achievementToken, boolean enabled, int progress,
            boolean completed, boolean claimed) {
        mTitle = title;
        mDescription = description;
        mNumber = number;
        mKind = kind;
        mCoins = coins;
        mXp = xp;
        mCreatedAt = createdAt;
        mAchievementToken = achievementToken;
        mEnabled = enabled;
        mProgress = progress;
        mCompleted = completed;
        mClaimed = claimed;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        mKind = kind;
    }

    public int getCoins() {
        return mCoins;
    }

    public void setCoins(int coins) {
        mCoins = coins;
    }

    public int getXp() {
        return mXp;
    }

    public void setXp(int xp) {
        mXp = xp;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public String getAchievementToken() {
        return mAchievementToken;
    }

    public void setAchievementToken(String achievementToken) {
        mAchievementToken = achievementToken;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
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
