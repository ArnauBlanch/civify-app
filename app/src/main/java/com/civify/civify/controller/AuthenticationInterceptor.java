package com.civify.civify.controller;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String mAuthToken;

    public AuthenticationInterceptor(String token) {
        this.mAuthToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", mAuthToken)
                .header("Accept", "application/json");

        Request request = builder.build();
        return chain.proceed(request);
    }
}
