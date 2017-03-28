package com.civify.civify.service;

import com.civify.civify.model.Issue;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IssueService {
    String ENDPOINT = "http://localhost:3000";

    @POST("/users/{user_auth_token}/issues")
    Call<Issue> createIssue(@Header("Authorization") String authToken, @Body Issue issue, @Path
            ("user_auth_token") String userAuthToken);

    @GET("/issues")
    Call<List<Issue>> getIssues(@Header("Authorization") String authToken);

    @GET("/issues/{issue_auth_token}")
    Call<Issue> getIssue(@Header("Authorization") String authToken, @Path("issue_auth_token")
            String issueAuthToken);
}
