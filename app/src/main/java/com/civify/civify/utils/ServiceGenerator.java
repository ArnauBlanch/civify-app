package com.civify.civify.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("CanBeFinal")
public final class ServiceGenerator {
    private static final String BASE_URL = "http://192.168.1.104:3000/";
    // Put the server's IP

    private static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit sRetrofit = sBuilder.build();
    private ServiceGenerator() {

    }

    public static <S> S createService(
            Class<S> serviceClass) {
        return sRetrofit.create(serviceClass);
    }
}
