package com.civify.adapter.achievement;

import android.content.SharedPreferences;
import android.util.Log;

import com.civify.activity.fragments.AchievementsFragment;
import com.civify.adapter.LoginAdapterImpl;
import com.civify.model.achievement.Achievement;
import com.civify.service.achievement.AchievementService;
import com.civify.service.achievement.AchievementSimpleCallback;
import com.civify.service.achievement.ListAchievementsSimpleCallback;
import com.civify.utils.ServiceGenerator;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementAdapterImpl implements AchievementAdapter {

    private AchievementService mAchievementService;
    private String mAuthToken;

    public AchievementAdapterImpl(SharedPreferences sharedPreferences) {
        this(ServiceGenerator.getInstance()
                .createService(AchievementService.class), sharedPreferences);
    }

    AchievementAdapterImpl(AchievementService service, SharedPreferences sharedPreferences) {
        mAchievementService = service;
        mAuthToken = sharedPreferences.getString(LoginAdapterImpl.AUTH_TOKEN, "");
    }

    @Override
    public void getAchievements(final ListAchievementsSimpleCallback callback) {
        Call<List<Achievement>> call = mAchievementService.getAchievements(mAuthToken);
        call.enqueue(new Callback<List<Achievement>>() {
            @Override
            public void onResponse(Call<List<Achievement>> call,
                    Response<List<Achievement>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(response.body());
                } else {
                    Log.d(AchievementsFragment.API_TEST, "AuthToken = " + mAuthToken);
                    Log.d(AchievementsFragment.API_TEST, "Response code = " + response.code());
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Achievement>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void getAchievement(String achievementAuthToken,
            final AchievementSimpleCallback callback) {
        Call<Achievement> call = mAchievementService.getAchievement(mAuthToken,
                achievementAuthToken);
        call.enqueue(new Callback<Achievement>() {
            @Override
            public void onResponse(Call<Achievement> call, Response<Achievement> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSucces(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Achievement> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
