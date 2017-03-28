package com.civify.civify.service;

import static org.junit.Assert.assertEquals;

import com.civify.civify.model.Issue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

public class IssueServiceTest {

    private static final String USER_AUTH_TOKEN = "RKAW2TteN2MwM98fiVXHNayP";
    private static final String USERS_PATH = "/users";
    private static final String ISSUES_PATH = "/issues";
    private static final String FAKE_AUTH_TOKEN = "fake-auth-token";

    private Issue mIssue;

    @Before
    public void setUp() throws Exception {
        mIssue = new Issue();
        mIssue.setUserAuthToken(USER_AUTH_TOKEN);
    }

    @Test
    public void testMockService() throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IssueService.ENDPOINT + USERS_PATH + '/'
                        + mIssue.getUserAuthToken() + ISSUES_PATH + '/')
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NetworkBehavior behavior = NetworkBehavior.create();
        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();

        BehaviorDelegate<IssueService> delegate = mockRetrofit.create(IssueService.class);
        IssueMockService issueMockService = new IssueMockService(delegate);
        Call<Issue> response = issueMockService.createIssue(FAKE_AUTH_TOKEN, mIssue, mIssue
                .getUserAuthToken());
        Issue issue = response.execute().body();
        assertEquals(issue.getUserAuthToken(), USER_AUTH_TOKEN);
    }
}