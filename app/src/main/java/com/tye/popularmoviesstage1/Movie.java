package com.tye.popularmoviesstage1;


import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
class Movie implements Parcelable {

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


    private Movie(Parcel in){
        original_title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(original_title);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeDouble(vote_average);
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
