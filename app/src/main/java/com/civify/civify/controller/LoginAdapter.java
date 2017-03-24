package com.civify.civify.controller;


public interface LoginAdapter {
    void login(String email, String password, LoginFinishedCallback onLoginFinishedCallback);
}
