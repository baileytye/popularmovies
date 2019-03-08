package com.tye.popularmoviesstage1;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    @PrimaryKey
    private int id;
    private String original_title;
    private String poster_path;
    private String overview;
    private String release_date;
    private double vote_average;
    private double popularity;

    public Movie(int id, String original_title, String poster_path, String overview,
                 double vote_average, String release_date, double popularity) {
        this.id = id;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.popularity = popularity;
    }


    public int getId(){
        return id;
    }

    public void setId(int i){
        id = i;
    }

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

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @Ignore
    private Movie(Parcel in){
        id = in.readInt();
        original_title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
        popularity = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(original_title);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeDouble(vote_average);
        parcel.writeDouble(popularity);
    }


    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>(){

                @Override
                public Movie createFromParcel(Parcel parcel) {
                    return new Movie(parcel);
                }

                @Override
                public Movie[] newArray(int i) {
                    return new Movie[i];
                }
            };

}
