package com.civify.adapter.achievement;

import com.civify.service.achievement.AchievementSimpleCallback;
import com.civify.service.achievement.ListAchievementsSimpleCallback;
import com.civify.service.award.RewardCallback;

public interface AchievementAdapter {

    void getAchievements(ListAchievementsSimpleCallback callback);

    void getAchievement(String achievementAuthToken, AchievementSimpleCallback callback);

    void claimAchievement(String achievementToken, RewardCallback callback);
}
