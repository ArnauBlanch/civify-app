package com.civify.civify.controller;

import com.civify.civify.model.User;

import retrofit2.Call;
import retrofit2.http.POST;

public interface CivifyLoginService {
    @POST("/login")
    Call<User> basicLogin();
}
