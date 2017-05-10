package com.civify.service.award;

import com.civify.model.award.Award;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AwardService {

    @Headers({
            "Content-Type: application/json"
    })

    @GET("/awards")
    Call<List<Award>> getAwards(@Header("Authorization") String authToken);

    @GET("/awards/{award_auth_token}")
    Call<Award> getAward(@Header("Authorization") String authToken, @Path("award_auth_token")
            String awardAuthToken);
}
