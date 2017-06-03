package com.civify.service.achievement;

import com.civify.model.achievement.Achievement;

import java.util.List;

public interface ListAchievementsSimpleCallback {
    void onSuccess(List<Achievement> achievements);

    void onFailure();
}
