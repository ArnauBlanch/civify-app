package com.civify.civify.Controller;


public interface LoginAdapter {
    void login(String email, String password, LoginFinishedCallback onLoginFinishedCallback);
}
