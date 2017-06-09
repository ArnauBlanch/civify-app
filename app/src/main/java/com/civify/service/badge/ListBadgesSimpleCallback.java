package com.civify.service.badge;

import com.civify.model.badge.Badge;

import java.util.List;

public interface ListBadgesSimpleCallback {
    void onSuccess(List<Badge> badges);

    void onFailure();

}
