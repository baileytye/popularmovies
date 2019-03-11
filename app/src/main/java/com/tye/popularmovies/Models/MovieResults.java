package com.tye.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
