package com.civify.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class ServiceGenerator {
    public static final String RAILS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    // Alternative: http://34.210.6.67:3000
    public static final String BASE_URL = "http://10.0.2.2:3000";
    public static final String BASE_WEB_URL = "http://staging.civify.cf";

    private static ServiceGenerator sInstance;

    private final Retrofit.Builder mBuilder;
    private Retrofit mRetrofit;
    private final OkHttpClient.Builder mHttpClient;
    private final HttpLoggingInterceptor mLogging;
    private final Gson mGson;

    private ServiceGenerator() {
        mGson = new GsonBuilder()
                 .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(RAILS_DATE_FORMAT)
                .create();
        mBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson));
        mRetrofit = mBuilder.build();
        mHttpClient = new OkHttpClient.Builder();
        mLogging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public Gson getGson() {
        return mGson;
    }

    public static ServiceGenerator getInstance() {
        if (sInstance == null) {
            sInstance = new ServiceGenerator();
        }
        return sInstance;
    }

    public <S> S createService(Class<S> serviceClass) {
        if (!mHttpClient.interceptors().contains(mLogging)) {
            mHttpClient.addInterceptor(mLogging);
        }
        mBuilder.client(mHttpClient.build());
        mRetrofit = mBuilder.build();
        return mRetrofit.create(serviceClass);
    }
}
