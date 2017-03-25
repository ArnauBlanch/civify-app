package com.civify.civify.adapter;

import com.civify.civify.model.Issue;
import com.civify.civify.service.IIssueResponseCallback;
import com.civify.civify.service.IssueService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IssueAdapter {

    public void createIssue(Issue issue) {
        // TODO: business

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IssueService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IssueService service = retrofit.create(IssueService.class);

        Call<Issue> responseCall = service.createIssue(issue);

        responseCall.enqueue(new IIssueResponseCallback<>());
    }
}
