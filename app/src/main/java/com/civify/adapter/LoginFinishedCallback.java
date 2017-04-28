package com.civify.adapter;

import com.civify.model.User;

public interface LoginFinishedCallback {

    void onLoginSucceeded(User u);

    void onLoginFailed(LoginError t);
}
