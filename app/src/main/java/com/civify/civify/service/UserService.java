package com.civify.civify.service;

import com.civify.civify.model.MessageResponse;
import com.civify.civify.model.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {
    String AUTHORIZATION =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJleHBpcmF0"
            + "aW9uIjoxNDkwNDQ1MzgyfQ.H6ZNmOQP4AZJFJeSn5Noscv7FVGUoINaND3YkCcrKRc";

    @Headers("Authorization: " + AUTHORIZATION)
    @POST("users")
    Call<MessageResponse> registerUser(@Body User user);

    @Headers("Authorization: " + AUTHORIZATION)
    @POST("users/search")
    Call<MessageResponse> checkUnusedUsername(@Body JsonObject username);

    @Headers("Authorization: " + AUTHORIZATION)
    @POST("users/search")
    Call<MessageResponse> checkUnusedEmail(@Body JsonObject email);
}
