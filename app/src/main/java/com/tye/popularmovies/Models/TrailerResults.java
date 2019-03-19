package com.tye.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * POJO for list of trailers received from server
 */
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
