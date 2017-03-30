package com.civify.civify.controller;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CivifyLoginService {

    @Headers({
            "Content-Type: application/json"
    })
    @POST("/login")
    Call<String> loginWithEmail(@Body CivifyEmailCredentials credentials);

    @POST("/login")
    Call<String> loginWithUsername(@Body CivifyUsernameCredentials credentials);
}
