package com.civify.adapter.event;

import android.content.SharedPreferences;

import com.civify.adapter.LoginAdapterImpl;
import com.civify.model.RewardContainer;
import com.civify.model.event.Event;
import com.civify.service.award.RewardCallback;
import com.civify.service.event.EventService;
import com.civify.service.event.EventSimpleCallback;
import com.civify.service.event.ListEventsSimpleCallback;
import com.civify.utils.ServiceGenerator;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAdapterImpl implements EventAdapter {

    private EventService mEventService;
    private String mAuthToken;

    public EventAdapterImpl(SharedPreferences sharedPreferences) {
        this(ServiceGenerator.getInstance()
                .createService(EventService.class), sharedPreferences);
    }

    EventAdapterImpl(EventService service, SharedPreferences sharedPreferences) {
        mEventService = service;
        mAuthToken = sharedPreferences.getString(LoginAdapterImpl.AUTH_TOKEN, "");
    }

    @Override
    public void getEvents(final ListEventsSimpleCallback callback) {
        Call<List<Event>> call = mEventService.getEvents(mAuthToken);
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call,
                    Response<List<Event>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void getEvent(String eventAuthToken,
            final EventSimpleCallback callback) {
        Call<Event> call = mEventService.getEvent(mAuthToken, eventAuthToken);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSucces(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void claimEvent(String eventToken, final RewardCallback callback) {
        Call<RewardContainer> call = mEventService.claimEvent(mAuthToken, eventToken);
        call.enqueue(new Callback<RewardContainer>() {
            @Override
            public void onResponse(Call<RewardContainer> call, Response<RewardContainer> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(response.body().getReward());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<RewardContainer> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
