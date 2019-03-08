package com.tye.popularmovies.TMDB;


import com.tye.popularmovies.BuildConfig;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TMDBApi {

    String API_KEY = BuildConfig.API_KEY;

    @GET("3/movie/popular?api_key=" + API_KEY)
    Call<MovieResults> getMoviesPopular();

    @GET("3/movie/top_rated?api_key=" + API_KEY)
    Call<MovieResults> getMoviesRatings();

    @GET("3/movie/{id}/reviews?api_key=" + API_KEY)
    Call<ReviewResults> getReviews(@Path("id") int id);

    @GET("3/movie/{id}/videos?api_key=" + API_KEY)
    Call<TrailerResults> getTrailers(@Path("id") int id);



}
