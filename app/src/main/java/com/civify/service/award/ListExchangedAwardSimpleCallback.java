package com.civify.service.award;

import com.civify.model.award.ExchangedAward;

import java.util.List;

public interface ListExchangedAwardSimpleCallback {

    void onSuccess(List<ExchangedAward> exchangedAwards);

    void onFailure();

}
