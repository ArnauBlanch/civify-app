package com.civify.civify.Controller;


import com.civify.civify.Model.User;

import retrofit2.Call;
import retrofit2.http.POST;

public interface CivifyLoginService {
    @POST("/login")
    Call<User> basicLogin();
}