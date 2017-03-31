package com.civify.controller;


public interface LoginAdapter {
    void login(String firstCredential, String password,
               LoginFinishedCallback onLoginFinishedCallback);
}
