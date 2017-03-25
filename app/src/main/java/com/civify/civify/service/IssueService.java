package com.civify.civify.service;

import com.civify.civify.model.Issue;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IssueService {
    String ENDPOINT = "localhost:3000";

    @POST("/issues")
    Call<Issue> createIssue(@Body Issue issue);
}