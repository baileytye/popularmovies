package com.tye.popularmoviesstage1;


import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TMDBApi {

    //TODO: REMOVE BEFORE COMMITING TO GIT
    String API_KEY = "";

     String path = "3/movie/popular?api_key=" + API_KEY;

    @GET(path)
    Call<Results> getMovies();

}
