package com.civify.service;

import com.civify.model.AchievementsEventsContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface AchievementsEventsService {
    @GET("/new_achievements_events")
    Call<AchievementsEventsContainer> getNewAchievementsEvents(@Header("Authorization")
            String authToken);
}
