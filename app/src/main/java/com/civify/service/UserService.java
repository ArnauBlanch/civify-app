package com.civify.service;

import com.civify.model.MessageResponse;
import com.civify.model.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("/users")
    Call<MessageResponse> registerUser(@Body User user);

    @POST("/users/search")
    Call<MessageResponse> checkUnusedUsername(@Body JsonObject username);

    @POST("/users/search")
    Call<MessageResponse> checkUnusedEmail(@Body JsonObject email);
}
