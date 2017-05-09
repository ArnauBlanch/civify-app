package com.civify.service.award;

import com.civify.model.MessageResponse;
import com.civify.model.award.Award;
import com.civify.model.issue.Issue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AwardService {

    @Headers({
            "Content-Type: application/json"
    })

    @GET("/awards")
    Call<List<Award>> getAwards(@Header("Authorization") String authToken);

    @GET("/users/{user_auth_token}/offered_awards")
    Call<List<Award>> getCommerceOfferedAwards(@Header("Authorization") String authToken,
            @Path("user_auth_token") String userAuthToken);

    @GET("/awards/{award_auth_token}")
    Call<Award> getAward(@Header("Authorization") String authToken, @Path("award_auth_token")
            String awardAuthToken);

    @POST("/users/{user_auth_token}/offered_awards")
    Call<MessageResponse> offerAward(@Header("Authorization") String authToken,
            @Path("user_auth_token") String userAuthToken, @Body Award award);

    @PATCH("/awards/{award_auth_token}")
    Call<Award> editAward(@Header("Authorization") String authToken, @Path("award_auth_token")
            String awardAuthToken, @Body Award award);

    @DELETE("/awards/{award_auth_token}")
    Call<MessageResponse> deleteAward(@Header("Authorization") String authToken,
            @Path("award_auth_token") String awardAuthToken);
}
