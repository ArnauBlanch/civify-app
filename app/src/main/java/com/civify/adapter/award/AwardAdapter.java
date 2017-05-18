package com.civify.adapter.award;

import android.support.annotation.NonNull;

import com.civify.adapter.SimpleCallback;
import com.civify.service.award.AwardSimpleCallback;
import com.civify.service.award.ListAwardsSimpleCallback;

public interface AwardAdapter {

    void getOfferedAwards(@NonNull ListAwardsSimpleCallback callback);

    void getOfferedAward(@NonNull String awardAuthToken, @NonNull AwardSimpleCallback callback);

    void getExchangedAwards(@NonNull ListAwardsSimpleCallback callback);

    void exchangeAward(@NonNull String awardToken, @NonNull SimpleCallback callback);
}
