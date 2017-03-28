package com.civify.civify.service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseCallback<T> implements Callback<T> {

    private Response<T> mResponse;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        this.mResponse = response;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }

    public Response<T> getResponse() {
        return mResponse;
    }
}
