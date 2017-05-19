package com.civify.service.award;

import com.civify.model.Reward;

public interface RewardCallback {
    void onSuccess(Reward reward);

    void onFailure();
}
