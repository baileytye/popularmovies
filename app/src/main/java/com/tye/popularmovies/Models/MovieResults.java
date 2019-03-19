package com.tye.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * POJO to retrieve list of Movies from server
 */
public class MovieResults {

    @SerializedName("results")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    @SuppressWarnings("unused")
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
