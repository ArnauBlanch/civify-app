package com.civify.civify.controller;

import android.support.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public final class ServiceGenerator {

    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static ServiceGenerator sServiceGeneratorInstance;

    private Retrofit.Builder mBuilder;
    private Retrofit mRetrofit;
    private OkHttpClient.Builder mHttpClient;
    private HttpLoggingInterceptor mLogging;
    private HeaderInterceptor mHeaderInterceptor;


    private ServiceGenerator() {
        mBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        mRetrofit = mBuilder.build();
        mHttpClient = new OkHttpClient.Builder();
        mLogging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        mHeaderInterceptor = new HeaderInterceptor();
    }

    private void init() {
    }

    public static ServiceGenerator getInstance() {
        if (sServiceGeneratorInstance == null) {
            sServiceGeneratorInstance = new ServiceGenerator();
        }
        return sServiceGeneratorInstance;
    }

    private boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public <S> S createService(
            Class<S> serviceClass, @Nullable String authToken) {

        if (authToken != null) setHeader("Authorization", authToken);
        if (!mHttpClient.interceptors().contains(mLogging)) {
            mHttpClient.addInterceptor(mLogging);
        }
        if (!mHttpClient.interceptors().contains(mHeaderInterceptor)) {
            mHttpClient.addInterceptor(mHeaderInterceptor);
        }
        mBuilder.client(mHttpClient.build());
        mRetrofit = mBuilder.build();
        return mRetrofit.create(serviceClass);
    }

    private void setHeader(String headerName, String headerValue) {
        mHeaderInterceptor.setHeader(headerName, headerValue);
    }
}
