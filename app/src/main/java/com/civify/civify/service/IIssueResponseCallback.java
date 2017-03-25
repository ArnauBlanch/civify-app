package com.civify.civify.service;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IIssueResponseCallback<ResponseBody> implements Callback<ResponseBody> {

    @Override
    public void onResponse(Call call, Response response) {
        // TODO: on response
        Log.d("RESPONSE", "Status code: " + response.code() + " ### " + "Response body: " +
                response.body());
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        // TODO: on failure
        Log.d("RESPONSE", "Response failure: " + t.getMessage());
    }
}
