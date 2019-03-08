package com.tye.popularmovies.TMDB;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResults {

    @SerializedName("results")
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }

    @SuppressWarnings("unused")
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
