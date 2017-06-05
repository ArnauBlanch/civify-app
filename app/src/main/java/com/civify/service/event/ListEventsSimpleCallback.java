package com.civify.service.event;

import com.civify.model.event.Event;

import java.util.List;

public interface ListEventsSimpleCallback {
    void onSuccess(List<Event> events);

    void onFailure();
}
