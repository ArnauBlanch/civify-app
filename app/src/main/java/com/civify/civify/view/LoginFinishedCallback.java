package com.civify.civify.view;



public interface LoginFinishedCallback {

    void onLoginSucceeded(User u);

    void onLoginFailed(Throwable t);
}
