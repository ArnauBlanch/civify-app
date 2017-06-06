package com.civify.service.achievement;

import com.civify.model.achievement.Achievement;

public interface AchievementSimpleCallback {
    void onSucces(Achievement achievement);

    void onFailure();
}
