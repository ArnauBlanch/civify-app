package com.civify.service.badge;

import com.civify.model.badge.Badge;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface BadgeService {

    @Headers({
            "Accept: application/json"
    })

    @GET("/users/{user_auth_token}/badges")
    Call<List<Badge>> getUserBadges(@Header("Authorization") String authToken, @Path
            ("user_auth_token") String userAuthToken);
}
