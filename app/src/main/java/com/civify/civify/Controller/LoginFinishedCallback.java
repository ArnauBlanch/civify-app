package com.civify.civify.Controller;


import com.civify.civify.Model.User;

public interface LoginFinishedCallback {

    void onLoginSucceeded(User u);

    void onLoginFailed(Throwable t);
}
