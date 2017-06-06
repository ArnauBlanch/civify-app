package com.civify.adapter.badge;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.civify.adapter.LoginAdapterImpl;
import com.civify.adapter.UserAdapter;
import com.civify.model.badge.Badge;
import com.civify.service.badge.BadgeService;
import com.civify.service.badge.ListBadgesSimpleCallback;
import com.civify.utils.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BadgeAdapterImpl implements BadgeAdapter {

    private BadgeService mBadgeService;
    private SharedPreferences mSharedPreferences;

    public BadgeAdapterImpl(SharedPreferences sharedPreferences) {
        this(ServiceGenerator.getInstance().createService(BadgeService.class), sharedPreferences);
    }

    BadgeAdapterImpl(BadgeService badgeService, SharedPreferences sharedPreferences) {
        this.mBadgeService = badgeService;
        this.mSharedPreferences = sharedPreferences;
    }

    @Override
    public void getUserBadges(@NonNull String authToken,
            @NonNull final ListBadgesSimpleCallback callback) {
        Call<List<Badge>> serviceCall = mBadgeService.getUserBadges(getToken(), UserAdapter
                .getCurrentUser().getUserAuthToken());
        serviceCall.enqueue(new Callback<List<Badge>>() {
            @Override
            public void onResponse(Call<List<Badge>> call, Response<List<Badge>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Badge>> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    private String getToken() {
        return mSharedPreferences.getString(LoginAdapterImpl.AUTH_TOKEN, "");
    }
}
