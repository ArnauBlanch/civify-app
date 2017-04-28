package com.civify.adapter;

import com.civify.model.User;

public interface UserSimpleCallback {

    void onSuccess(User user);

    void onFailure();

}
