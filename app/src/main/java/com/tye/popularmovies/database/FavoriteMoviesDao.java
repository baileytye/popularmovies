package com.tye.popularmovies.database;

import com.tye.popularmovies.Models.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FavoriteMoviesDao {

    @Query("SELECT * FROM movie ORDER BY original_title")
    LiveData<List<Movie>> loadFavoriteMoviesByName();

    @Query("DELETE FROM movie")
    void nukeTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(Movie movie);

    @Delete
    void deleteFavoriteMovie(Movie movie);

    @Query("SELECT * FROM movie WHERE id = :id")
    Movie loadFavoriteMovieById(int id);

}
