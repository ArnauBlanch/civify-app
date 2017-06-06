package com.civify.adapter.badge;

import android.support.annotation.NonNull;

import com.civify.service.badge.ListBadgesSimpleCallback;

public interface BadgeAdapter {

    void getUserBadges(@NonNull String authToken, @NonNull ListBadgesSimpleCallback
            callback);
}
