package com.civify.civify.controller;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = "http://10.0.2.2:8080/test/";
    private static ServiceGenerator sServiceGeneratorInstance;

    private Retrofit.Builder mBuilder;
    private Retrofit mRetrofit;
    private OkHttpClient.Builder mHttpClient;
    private HttpLoggingInterceptor mLogging;


    protected ServiceGenerator() {
        mBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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

    private boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!isEmpty(username)
                && !isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null);
    }

    private <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!mHttpClient.interceptors().contains(interceptor)) {
                mHttpClient.addInterceptor(interceptor);

                mBuilder.client(mHttpClient.build());
                mRetrofit = mBuilder.build();
            }
        }

        if (!mHttpClient.interceptors().contains(mLogging)) {
            mHttpClient.addInterceptor(mLogging);

            mBuilder.client(mHttpClient.build());
            mRetrofit = mBuilder.build();
        }

        return mRetrofit.create(serviceClass);
    }
}
