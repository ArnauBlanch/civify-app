package com.civify.civify.controller;


import com.civify.civify.model.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CivifyMeService {
    @GET("/me")
    Call<User> getUser();
}
