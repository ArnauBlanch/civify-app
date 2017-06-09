package com.civify.service.issue;

import com.civify.model.IssueReward;
import com.civify.model.MessageResponse;
import com.civify.model.issue.Issue;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IssueService {

    @POST("/users/{user_auth_token}/issues")
    Call<IssueReward> createIssue(@Header("Authorization") String authToken, @Body Issue issue,
            @Path("user_auth_token") String userAuthToken);

    @GET("/issues")
    Call<List<Issue>> getIssues(@Header("Authorization") String authToken);

    @GET("/issues")
    Call<List<Issue>> getIssues(@Header("Authorization") String authToken,
            @Query("resolved") String isResolved, @Query("categories[]") ArrayList<String>
            categories, @Query("risk") String isRisk);

    @GET("/can_create_issue")
    Call<MessageResponse> canCreateIssue(@Header("Authorization") String authToken);

    @GET("/issues/{issue_auth_token}")
    Call<Issue> getIssue(@Header("Authorization") String authToken,
            @Path("issue_auth_token") String issueAuthToken);

    @POST("/issues/{issue_auth_token}/resolve")
    Call<MessageResponse> issueResolution(@Header("Authorization") String authToken,
            @Body JsonObject userToken, @Path("issue_auth_token") String issueAuthToken);

    @POST("/issues/{issue_auth_token}/report")
    Call<MessageResponse> issueReport(@Header("Authorization") String authToken,
            @Body JsonObject userToken, @Path("issue_auth_token") String issueAuthToken);

    @POST("/issues/{issue_auth_token}/confirm")
    Call<MessageResponse> issueConfirmation(@Header("Authorization") String authToken,
            @Body JsonObject userToken, @Path("issue_auth_token") String issueAuthToken);

    @PATCH("/issues/{issue_auth_token}")
    Call<Issue> editIssue(@Header("Authorization") String authToken, @Body JsonObject editedIssue,
            @Path("issue_auth_token") String issueAuthToken);

    @DELETE("/issues/{issue_auth_token}")
    Call<Issue> deleteIssue(@Header("Authorization") String authToken,
            @Path("issue_auth_token") String issueAuthToken);
}
