package com.civify.model;

import com.civify.model.achievement.Achievement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AchievementsEventsContainer {

    @Expose
    @SerializedName("achievements")
    private List<Achievement> mAchievementList;

    @Expose
    @SerializedName("events")
    private List<EventStub> mEventList;

    public AchievementsEventsContainer() {
    }

    public AchievementsEventsContainer(List<Achievement> achievementList,
            List<EventStub> eventList) {
        mAchievementList = achievementList;
        mEventList = eventList;
    }

    public List<Achievement> getAchievementList() {
        return mAchievementList;
    }

    public void setAchievementList(List<Achievement> achievementList) {
        mAchievementList = achievementList;
    }

    public List<EventStub> getEventList() {
        return mEventList;
    }

    public void setEventList(List<EventStub> eventList) {
        mEventList = eventList;
    }
}
