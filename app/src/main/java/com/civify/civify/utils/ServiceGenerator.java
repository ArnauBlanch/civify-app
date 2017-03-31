package com.civify.civify.utils;

import com.civify.civify.service.CivifyLoginService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class ServiceGenerator {
    private static final String BASE_URL = "http://10.4.41.152:3000/";
    // Put the server's IP

    private static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit sRetrofit = sBuilder.build();

    private static ServiceGenerator sInstance;

    private final Retrofit.Builder mLoginBuilder;
    private Retrofit mLoginRetrofit;
    private final OkHttpClient.Builder mHttpClient;
    private final HttpLoggingInterceptor mLogging;

    private ServiceGenerator() {
        // Login init
        mLoginBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        mLoginRetrofit = mLoginBuilder.build();
        mHttpClient = new OkHttpClient.Builder();
        mLogging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    }

    public static ServiceGenerator getInstance() {
        if (sInstance == null) {
            sInstance = new ServiceGenerator();
        }
        return sInstance;
    }

    public static <S> S createService(
            Class<S> serviceClass) {
        return sRetrofit.create(serviceClass);
    }

    public CivifyLoginService createLoginService() {
        if (!mHttpClient.interceptors().contains(mLogging)) {
            mHttpClient.addInterceptor(mLogging);
        }
        mLoginBuilder.client(mHttpClient.build());
        mLoginRetrofit = mLoginBuilder.build();
        return mLoginRetrofit.create(CivifyLoginService.class);
    }
}
