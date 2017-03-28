package com.civify.civify.service;

import com.civify.civify.model.Issue;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.mock.BehaviorDelegate;

public class IssueMockService implements IssueService {
    private final BehaviorDelegate<IssueService> mDelegate;

    public IssueMockService(BehaviorDelegate<IssueService> delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public Call<Issue> createIssue(@Header("Authorization") String authToken, @Body Issue issue,
            @Path("user_auth_token") String userAuthToken) {
        return mDelegate.returningResponse(issue).createIssue(authToken, issue, issue
                .getUserAuthToken());
    }

    @Override
    public Call<List<Issue>> getIssues(@Header("Authorization") String authToken) {
        return null;
    }

    @Override
    public Call<Issue> getIssue(@Header("Authorization") String authToken, @Path
            ("issue_auth_token") String issueAuthToken) {
        return null;
    }
}
