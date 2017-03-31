package com.civify.civify.adapter;

import com.civify.civify.model.User;

public interface LoginFinishedCallback {

    void onLoginSucceeded(User u);

    void onLoginFailed(LoginError t);
}
