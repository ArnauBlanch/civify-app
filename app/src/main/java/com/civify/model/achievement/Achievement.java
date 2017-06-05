package com.civify.model.achievement;

import android.support.v4.app.Fragment;

import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.achievements.AchievementDetailsFragment;
import com.civify.model.Picture;
import com.civify.model.map.CivifyMap;
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
    private String mToken;

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

    @Expose
    @SerializedName("badge")
    private Picture mBadge;

    public Achievement() {
    }

    public Achievement(String title, String description, int number, String kind, int coins, int xp,
            Date createdAt, String token, boolean enabled, int progress,
            boolean completed, boolean claimed, Picture badge) {
        mTitle = title;
        mDescription = description;
        mNumber = number;
        mKind = kind;
        mCoins = coins;
        mXp = xp;
        mCreatedAt = createdAt;
        mToken = token;
        mEnabled = enabled;
        mProgress = progress;
        mCompleted = completed;
        mClaimed = claimed;
        mBadge = badge;
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

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
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

    public Picture getBadge() {
        return mBadge;
    }

    public void setBadge(Picture badge) {
        mBadge = badge;
    }

    public void showAchievementDetails() {
        Fragment achievementDetailsFragment = AchievementDetailsFragment.newInstance(this);
        CivifyMap.getInstance().getContext()
                .setFragment(achievementDetailsFragment, DrawerActivity.DETAILS_ACHIEVEMENTS_ID);

    }
}
