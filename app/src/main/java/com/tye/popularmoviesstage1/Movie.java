package com.tye.popularmoviesstage1;


@SuppressWarnings("unused")
class Movie {

    private String original_title;
    private String poster_path;
    private String overview;
    private double vote_average;
    private String release_date;


    String getOriginal_title() {
        return original_title;
    }

    String getPoster_path() {
        return poster_path;
    }

    String getOverview() {
        return overview;
    }

    double getVote_average() {
        return vote_average;
    }

    String getRelease_date() {
        return release_date;
    }
}
