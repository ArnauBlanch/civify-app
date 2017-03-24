package com.civify.civify.controller;

import com.civify.civify.model.User;

public interface LoginFinishedCallback {

    void onLoginSucceeded(User u);

    void onLoginFailed(Throwable t);
}
