package com.civify.civify.controller;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {


    private static final String TAG = LoginAdapterImpl.class.getSimpleName();
    private HashMap<String, String> mHeaders;


    public HeaderInterceptor() {
        mHeaders = new HashMap<>();
        setDefaultHeaders();
    }

    private void setDefaultHeaders() {
        mHeaders.put("Accept", "application/json");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();
        for (HashMap.Entry<String, String> entry : mHeaders.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
            Log.d(TAG, "Header added: " + entry.getKey() + ", " + entry.getValue());
        }
        Request request = builder.build();
        return chain.proceed(request);
    }

    public void setHeader(String headerName, String headerValue) {
        mHeaders.put(headerName, headerValue);
    }

    public boolean hasHeader(String headerName) {
        return mHeaders.containsKey(headerName);
    }
}