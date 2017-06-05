package com.civify.adapter;

import android.content.SharedPreferences;

import com.civify.model.AchievementsEventsContainer;
import com.civify.service.AchievementsEventsCallback;
import com.civify.service.AchievementsEventsService;
import com.civify.utils.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementsEventsAdapter {

    private AchievementsEventsService mService;
    private String mAuthToken;

    public AchievementsEventsAdapter(SharedPreferences sharedPreferences) {
        this(ServiceGenerator.getInstance().createService(AchievementsEventsService.class),
                sharedPreferences);
    }

    public AchievementsEventsAdapter(AchievementsEventsService service, SharedPreferences
            sharedPreferences) {
        this.mService = service;
        this.mAuthToken = sharedPreferences.getString(LoginAdapterImpl.AUTH_TOKEN, "");
    }

    public void getNewAchievementsEvents(final AchievementsEventsCallback callback) {
        Call<AchievementsEventsContainer> call = mService
                .getNewAchievementsEvents(mAuthToken);
        call.enqueue(new Callback<AchievementsEventsContainer>() {
            @Override
            public void onResponse(Call<AchievementsEventsContainer> call,
                    Response<AchievementsEventsContainer> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<AchievementsEventsContainer> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
