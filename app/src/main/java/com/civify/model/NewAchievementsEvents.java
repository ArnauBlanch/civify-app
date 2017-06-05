package com.civify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewAchievementsEvents {

    @Expose
    @SerializedName("achievements")
    private boolean mNewAchievements;

    @Expose
    @SerializedName("events")
    private boolean mNewEvents;

    public boolean isNewAchievements() {
        return mNewAchievements;
    }

    public void setNewAchievements(boolean newAchievements) {
        mNewAchievements = newAchievements;
    }

    public boolean isNewEvents() {
        return mNewEvents;
    }

    public void setNewEvents(boolean newEvents) {
        mNewEvents = newEvents;
    }
}
