package com.tye.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * POJO for list of reviews from server
 */
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
