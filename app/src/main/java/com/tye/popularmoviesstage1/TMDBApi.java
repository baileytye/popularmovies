package com.tye.popularmoviesstage1;


import retrofit2.Call;
import retrofit2.http.GET;

interface TMDBApi {

    //TODO: ADD API KEY HERE
    String API_KEY = "";

    @GET("3/movie/popular?api_key=" + API_KEY)
    Call<Results> getMoviesPopular();

    @GET("3/movie/top_rated?api_key=" + API_KEY)
    Call<Results> getMoviesRatings();



}
