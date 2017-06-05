package com.civify.service.event;

import com.civify.model.event.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface EventService {
    @GET("/events")
    Call<List<Event>> getEvents(@Header("Authorization") String authToken);

    @GET("events/{event_token}")
    Call<Event> getEvent(@Header("Authorization") String authToken,
            @Path("event_token") String eventAuthToken);
}
