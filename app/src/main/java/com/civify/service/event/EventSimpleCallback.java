package com.civify.service.event;

import com.civify.model.event.Event;

public interface EventSimpleCallback {
    void onSucces(Event event);

    void onFailure();
}
