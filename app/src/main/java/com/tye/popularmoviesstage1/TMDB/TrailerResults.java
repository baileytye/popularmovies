package com.tye.popularmoviesstage1.TMDB;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResults {

    @SerializedName("results")
    private List<Trailer> trailers;

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
