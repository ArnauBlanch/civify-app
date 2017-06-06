package com.civify.adapter.event;

import com.civify.service.award.RewardCallback;
import com.civify.service.event.EventSimpleCallback;
import com.civify.service.event.ListEventsSimpleCallback;

public interface EventAdapter {

    void getEvents(ListEventsSimpleCallback callback);

    void getEvent(String eventAuthToken, EventSimpleCallback callback);

    void claimEvent(String eventToken, RewardCallback callback);
}
