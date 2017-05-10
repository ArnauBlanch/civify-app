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
    public static final String BASE_URL = "http://staging-api.civify.cf";
    private static ServiceGenerator sInstance;

    private final Retrofit.Builder mBuilder;
    private Retrofit mRetrofit;
    private final OkHttpClient.Builder mHttpClient;
    private final HttpLoggingInterceptor mLogging;

    private ServiceGenerator() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(RAILS_DATE_FORMAT)
                .create();
        mBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        mRetrofit = mBuilder.build();
        mHttpClient = new OkHttpClient.Builder();
        mLogging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
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
