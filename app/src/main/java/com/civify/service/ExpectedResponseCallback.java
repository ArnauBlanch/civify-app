package com.civify.service;

import com.civify.adapter.SimpleCallback;
import com.civify.model.MessageResponse;

import java.net.HttpURLConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpectedResponseCallback implements Callback<MessageResponse> {
    private SimpleCallback mCallback;
    private String mExpectedResponse;

    public ExpectedResponseCallback(String expectedResponse, SimpleCallback callback) {
        mExpectedResponse = expectedResponse;
        mCallback = callback;
    }

    @Override
    public void onResponse(Call<MessageResponse> call,
            Response<MessageResponse> response) {
        if (response.code() == HttpURLConnection.HTTP_OK
                && response.body().getMessage().equals(mExpectedResponse)) {
            mCallback.onSuccess();
        } else {
            mCallback.onFailure();
        }
    }

    @Override
    public void onFailure(Call<MessageResponse> call, Throwable t) {
        t.printStackTrace();
    }
}
