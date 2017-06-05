package com.civify.service;

import com.civify.model.AchievementsEventsContainer;

public interface AchievementsEventsCallback {
    void onSuccess(AchievementsEventsContainer achievementsEventsContainer);

    void onFailure();
}
