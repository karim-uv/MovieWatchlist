package com.karim.moviewatchlist.api;

import com.karim.moviewatchlist.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Injected at build time from local.properties (never committed to git)
    public static final String API_KEY = BuildConfig.TMDB_API_KEY;

    private static final String BASE_URL = "https://api.themoviedb.org/";

    private static Retrofit retrofit;

    public static TmdbApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(TmdbApi.class);
    }
}