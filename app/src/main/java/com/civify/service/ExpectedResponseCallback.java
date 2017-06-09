package com.civify.service;

import android.util.Log;

import com.civify.adapter.SimpleCallback;
import com.civify.model.MessageResponse;

import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpectedResponseCallback implements Callback<MessageResponse> {
    public static final String TAG = ExpectedResponseCallback.class.getSimpleName();
    private SimpleCallback mCallback;
    private String mExpectedResponse;

    public ExpectedResponseCallback(String expectedResponse, SimpleCallback callback) {
        mExpectedResponse = expectedResponse;
        mCallback = callback;
    }

    @Override
    public void onResponse(Call<MessageResponse> call,
            Response<MessageResponse> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            mCallback.onSuccess();
        } else {
            if (response.errorBody() != null) {
                try {
                    JSONObject jsonError = new JSONObject(response.errorBody().string());
                    mCallback.onFailure(jsonError.getString("message"));
                } catch (java.io.IOException e) {
                    Log.e(TAG, e.getMessage());
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    @Override
    public void onFailure(Call<MessageResponse> call, Throwable t) {
        t.printStackTrace();
    }
}
