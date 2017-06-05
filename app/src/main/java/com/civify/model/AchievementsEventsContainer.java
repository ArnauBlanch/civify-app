package com.civify.model;

import com.civify.model.achievement.Achievement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AchievementsEventsContainer {

    @Expose
    @SerializedName("achievements")
    List<Achievement> mAchievementList;

    @Expose
    @SerializedName("events")
    List<EventStub> mEventList;
}
