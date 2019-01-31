package com.tye.popularmoviesstage1;


public class Movie {

    private int id;
    private String original_title;
    private String poster_path;
    private String overview;
    private double vote_average;
    private String release_date;


    public int getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }
}
