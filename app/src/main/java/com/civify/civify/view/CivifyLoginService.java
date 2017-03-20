package com.civify.civify.view;


import retrofit2.Call;
import retrofit2.http.POST;

public interface CivifyLoginService {
    @POST("/login")
    Call<User> basicLogin();
}