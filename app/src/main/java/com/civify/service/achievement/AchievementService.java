package com.civify.service.achievement;

import com.civify.model.RewardContainer;
import com.civify.model.achievement.Achievement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AchievementService {
    @GET("/achievements")
    Call<List<Achievement>> getAchievements(@Header("Authorization") String authToken);

    @GET("/achievements/{achievement_token}")
    Call<Achievement> getAchievement(@Header("Authorization") String authToken,
            @Path("achievement_token") String achievementAuthToken);

    @POST("/achievements/{achievement_token}/claim")
    Call<RewardContainer> claimAchievement(@Header("Authorization") String authToken,
            @Path("achievement_token") String achievementAuthToken);
}
