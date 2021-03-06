package com.civify.adapter.award;

import android.support.annotation.NonNull;

import com.civify.service.award.AwardSimpleCallback;
import com.civify.service.award.ListAwardsSimpleCallback;
import com.civify.service.award.ListExchangedAwardSimpleCallback;
import com.civify.service.award.RewardCallback;

public interface AwardAdapter {

    void getOfferedAwards(@NonNull ListAwardsSimpleCallback callback);

    void getOfferedAward(@NonNull String awardAuthToken, @NonNull AwardSimpleCallback callback);

    void exchangeAward(@NonNull String awardToken, @NonNull RewardCallback callback);

    void getExchangedAwards(@NonNull String authToken, @NonNull ListExchangedAwardSimpleCallback
            callback);
}
