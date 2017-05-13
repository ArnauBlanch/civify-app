package com.civify.service.award;

import com.civify.model.award.Award;

import java.util.List;

public interface ListAwardsSimpleCallback {

    void onSuccess(List<Award> awards);

    void onFailure();
}
