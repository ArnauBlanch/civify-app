package com.civify.model;

import com.civify.model.achievement.Achievement;
import com.civify.model.event.Event;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AchievementsEventsContainer {

    @Expose
    @SerializedName("achievements")
    private List<Achievement> mAchievementList;

    @Expose
    @SerializedName("events")
    private List<Event> mEventList;

    public AchievementsEventsContainer() {
    }

    public AchievementsEventsContainer(List<Achievement> achievementList,
            List<Event> eventList) {
        mAchievementList = achievementList;
        mEventList = eventList;
    }

    public List<Achievement> getAchievementList() {
        return mAchievementList;
    }

    public void setAchievementList(List<Achievement> achievementList) {
        mAchievementList = achievementList;
    }

    public List<Event> getEventList() {
        return mEventList;
    }

    public void setEventList(List<Event> eventList) {
        mEventList = eventList;
    }
}
