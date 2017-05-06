package com.civify.service.award;

import com.civify.model.award.Award;

public interface AwardSimpleCallback {

    void onSuccess(Award award);

    void onFailure();
}
