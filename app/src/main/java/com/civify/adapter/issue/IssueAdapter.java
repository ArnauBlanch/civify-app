package com.civify.adapter.issue;

import android.content.SharedPreferences;

import com.civify.adapter.LoginAdapterImpl;
import com.civify.adapter.SimpleCallback;
import com.civify.adapter.UserAdapter;
import com.civify.model.MessageResponse;
import com.civify.model.issue.Issue;
import com.civify.service.ExpectedResponseCallback;
import com.civify.service.issue.IssueService;
import com.civify.service.issue.IssueSimpleCallback;
import com.civify.service.issue.ListIssuesSimpleCallback;
import com.civify.utils.ServiceGenerator;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueAdapter {
    public static final String ISSUE_WITH_AUTH_TOKEN = "Issue with auth token ";
    public static final String CONFIRMED_BY_USER_WITH_AUTH_TOKEN =
            "confirmed by User with auth token ";
    public static final String REPORTED_BY_USER_WITH_AUTH_TOKEN =
            "reported by User with auth token ";
    public static final String UN = " un";
    public static final String USER = "user";
    public static final String RESOLUTION_ADDED = "Resolution added";
    public static final String RESOLUTION_DELETED = "Resolution deleted";
    static final String RECORD_DOES_NOT_EXIST = "Doesn’t exists record";
    private IssueService mIssueService;
    private String mAuthToken;

    public IssueAdapter(SharedPreferences sharedPreferences) {
        this(ServiceGenerator.getInstance().createService(IssueService.class), sharedPreferences);
    }

    IssueAdapter(IssueService service, SharedPreferences sharedPreferences) {
        this.mIssueService = service;
        this.mAuthToken = sharedPreferences.getString(LoginAdapterImpl.AUTH_TOKEN, "");
    }

    public void createIssue(Issue issue, final IssueSimpleCallback callback) {
        Call<Issue> call = mIssueService.createIssue(mAuthToken, issue, UserAdapter
                .getCurrentUser().getUserAuthToken());
        call.enqueue(new Callback<Issue>() {

            @Override
            public void onResponse(Call<Issue> call, Response<Issue> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Issue> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void editIssue(String issueAuthToken, Issue issue, final IssueSimpleCallback callback) {
        Call<Issue> call = mIssueService.editIssue(mAuthToken, issue, issueAuthToken);
        call.enqueue(new Callback<Issue>() {

            @Override
            public void onResponse(Call<Issue> call, Response<Issue> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Issue> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void getIssues(final ListIssuesSimpleCallback callback) {
        Call<List<Issue>> call = mIssueService.getIssues(mAuthToken);
        call.enqueue(new Callback<List<Issue>>() {

            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Issue>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getIssue(String issueAuthToken, final IssueSimpleCallback callback) {
        Call<Issue> call = mIssueService.getIssue(mAuthToken, issueAuthToken);
        call.enqueue(new Callback<Issue>() {

            @Override
            public void onResponse(Call<Issue> call, Response<Issue> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Issue> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteIssue(String issueAuthToken, final IssueSimpleCallback callback) {
        Call<Issue> call = mIssueService.deleteIssue(mAuthToken, issueAuthToken);
        call.enqueue(new Callback<Issue>() {

            @Override
            public void onResponse(Call<Issue> call, Response<Issue> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Issue> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // Reports

    private void issueReport(String issueAuthToken, final String expectedResponse,
            final SimpleCallback callback) {
        JsonObject userToken = new JsonObject();
        userToken.addProperty(USER, UserAdapter.getCurrentUser().getUserAuthToken());

        Call<MessageResponse> call = mIssueService.issueReport(mAuthToken,
                userToken, issueAuthToken);
        call.enqueue(new ExpectedResponseCallback(expectedResponse, callback));
    }

    public void reportIssue(String issueAuthToken, SimpleCallback callback) {
        String expMessage = ISSUE_WITH_AUTH_TOKEN + issueAuthToken
                + ' ' + REPORTED_BY_USER_WITH_AUTH_TOKEN
                + UserAdapter.getCurrentUser().getUserAuthToken();
        issueReport(issueAuthToken, expMessage, callback);
    }

    public void unreportIssue(String issueAuthToken, SimpleCallback callback) {
        String expMessage = ISSUE_WITH_AUTH_TOKEN + issueAuthToken
                + UN + REPORTED_BY_USER_WITH_AUTH_TOKEN
                + UserAdapter.getCurrentUser().getUserAuthToken();
        issueReport(issueAuthToken, expMessage, callback);
    }

    // Confirmations

    private void issueConfirmation(String issueAuthToken, final String expectedResponse, final
            SimpleCallback callback) {
        JsonObject userToken = new JsonObject();
        userToken.addProperty(USER, UserAdapter.getCurrentUser().getUserAuthToken());

        Call<MessageResponse> call = mIssueService.issueConfirmation(mAuthToken,
                userToken, issueAuthToken);
        call.enqueue(new ExpectedResponseCallback(expectedResponse, callback));
    }

    public void confirmIssue(String issueAuthToken, SimpleCallback callback) {
        String expMessage = ISSUE_WITH_AUTH_TOKEN + issueAuthToken
                + " " + CONFIRMED_BY_USER_WITH_AUTH_TOKEN
                + UserAdapter.getCurrentUser().getUserAuthToken();
        issueConfirmation(issueAuthToken, expMessage, callback);
    }

    public void unconfirmIssue(String issueAuthToken, SimpleCallback callback) {
        String expMessage = ISSUE_WITH_AUTH_TOKEN + issueAuthToken
                + UN + CONFIRMED_BY_USER_WITH_AUTH_TOKEN
                + UserAdapter.getCurrentUser().getUserAuthToken();
        issueConfirmation(issueAuthToken, expMessage, callback);
    }

    // Resolutions

    private void issueResolution(String issueAuthToken, final String expectedResponse,
            final SimpleCallback callback) {
        JsonObject userToken = new JsonObject();
        userToken.addProperty(USER, UserAdapter.getCurrentUser().getUserAuthToken());
        Call<MessageResponse> call = mIssueService.issueResolution(mAuthToken,
                userToken, issueAuthToken);
        call.enqueue(new ExpectedResponseCallback(expectedResponse, callback));
    }

    public void resolveIssue(String issueAuthToken, SimpleCallback callback) {
        String expMessage = RESOLUTION_ADDED;
        issueResolution(issueAuthToken, expMessage, callback);
    }

    public void unresolveIssue(String issueAuthToken, SimpleCallback callback) {
        String expMessage = RESOLUTION_DELETED;
        issueResolution(issueAuthToken, expMessage, callback);
    }

    public void setService(IssueService issueService) {
        mIssueService = issueService;
    }
}
