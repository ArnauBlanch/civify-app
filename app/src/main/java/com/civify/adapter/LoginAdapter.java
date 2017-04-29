package com.civify.adapter;

public interface LoginAdapter {
    void login(String firstCredential, String password,
               LoginFinishedCallback onLoginFinishedCallback);

    void isLogged(LoginFinishedCallback loginFinishedCallback);

    void logout();
}
