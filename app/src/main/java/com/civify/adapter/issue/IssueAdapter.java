package com.civify.adapter.issue;

import android.content.SharedPreferences;

import com.civify.adapter.LoginAdapterImpl;
import com.civify.adapter.UserAdapter;
import com.civify.model.issue.Issue;
import com.civify.service.issue.IssueService;
import com.civify.service.issue.IssueSimpleCallback;
import com.civify.service.issue.ListIssuesSimpleCallback;
import com.civify.utils.ServiceGenerator;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueAdapter {
    public static final String RECORD_DOES_NOT_EXIST = "Doesn’t exists record";
    private IssueService mIssueService;
    private String mAuthToken;

    public IssueAdapter(SharedPreferences sharedPreferences) {
        this(ServiceGenerator.getInstance().createService(IssueService.class), sharedPreferences);
    }

    public IssueAdapter(IssueService service, SharedPreferences sharedPreferences) {
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
                } else if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
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
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND
                        && getMessageFromError(response.errorBody())
                        .equals(RECORD_DOES_NOT_EXIST)) {
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
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND
                        && getMessageFromError(response.errorBody())
                        .equals(RECORD_DOES_NOT_EXIST)) {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Issue> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String getMessageFromError(ResponseBody errorBody) {
        try {
            return (new JsonParser().parse(errorBody.string()).getAsJsonObject()).get("message")
                    .getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
