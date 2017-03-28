package com.civify.civify.adapter;

import android.support.annotation.NonNull;

import com.civify.civify.model.Issue;
import com.civify.civify.service.IssueService;
import com.civify.civify.service.ResponseCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IssueAdapter {
    private static final String RAILS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private IssueService mIssueService;

    public IssueAdapter() {
        this(getRetrofit().create(IssueService.class));
    }

    public IssueAdapter(IssueService service) {
        this.mIssueService = service;
    }

    @NonNull
    private static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(RAILS_DATE_FORMAT)
                .create();

        return new Retrofit.Builder()
                .baseUrl(IssueService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public boolean createIssue(String authToken, Issue issue) {
        Call<Issue> call = mIssueService.createIssue(authToken, issue, issue.getUserAuthToken());
        ResponseCallback<Issue> issueResponseCallback = new ResponseCallback<>();
        call.enqueue(issueResponseCallback);

        Response<Issue> response = issueResponseCallback.getResponse();
        return response.isSuccessful();
    }

    public List<Issue> getIssues(String authToken) {
        Call<List<Issue>> call = mIssueService.getIssues(authToken);
        ResponseCallback<List<Issue>> issueResponseCallback = new ResponseCallback<>();
        call.enqueue(issueResponseCallback);

        Response<List<Issue>> response = issueResponseCallback.getResponse();
        return response.body();
    }

    public Issue getIssue(String authToken, String issueAuthToken) {
        Call<Issue> call = mIssueService.getIssue(authToken, issueAuthToken);
        ResponseCallback<Issue> issueResponseCallback = new ResponseCallback<>();
        call.enqueue(issueResponseCallback);

        Response<Issue> response = issueResponseCallback.getResponse();
        return response.body();
    }
}
