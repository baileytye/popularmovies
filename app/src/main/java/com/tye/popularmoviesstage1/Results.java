package com.tye.popularmoviesstage1;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class Results {

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
