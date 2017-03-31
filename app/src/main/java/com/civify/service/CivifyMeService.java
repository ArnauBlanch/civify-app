package com.civify.service;

import com.civify.civify.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface CivifyMeService {
    @Headers({
            "Accept: application/json"
    })
    @GET("/me")
    Call<User> getUser(@Header("Authorization") String authToken);
}
