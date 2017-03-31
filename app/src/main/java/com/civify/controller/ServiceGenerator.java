package com.civify.controller;

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


    private ServiceGenerator() {
        mBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        mRetrofit = mBuilder.build();
        mHttpClient = new OkHttpClient.Builder();
        mLogging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static ServiceGenerator getInstance() {
        if (sServiceGeneratorInstance == null) {
            sServiceGeneratorInstance = new ServiceGenerator();
        }
        return sServiceGeneratorInstance;
    }

    public <S> S createService(
            Class<S> serviceClass) {

        if (!mHttpClient.interceptors().contains(mLogging)) {
            mHttpClient.addInterceptor(mLogging);
        }
        mBuilder.client(mHttpClient.build());
        mRetrofit = mBuilder.build();
        return mRetrofit.create(serviceClass);
    }
}
