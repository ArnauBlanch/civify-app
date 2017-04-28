package com.civify.service;

import com.civify.model.MessageResponse;
import com.civify.model.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @GET("/users/{user_auth_token}")
    Call<User> getUser(@Header("Authorization") String authToken, @Path("user_auth_token")
            String userAuthToken);

    @POST("/users")
    Call<MessageResponse> registerUser(@Body User user);

    @POST("/users/search")
    Call<MessageResponse> checkUnusedUsername(@Body JsonObject username);

    @POST("/users/search")
    Call<MessageResponse> checkUnusedEmail(@Body JsonObject email);
}
