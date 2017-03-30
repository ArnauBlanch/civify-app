package com.civify.civify.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("CanBeFinal")
public final class ServiceGenerator {
    public static final String RAILS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String BASE_URL = "http://192.168.1.104:3000/";
    // Put the server's IP

    private static Gson sGson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setDateFormat(RAILS_DATE_FORMAT)
            .create();

    private static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(sGson));

    private static Retrofit sRetrofit = sBuilder.build();
    private ServiceGenerator() {

    }

    public static <S> S createService(
            Class<S> serviceClass) {
        return sRetrofit.create(serviceClass);
    }
}
