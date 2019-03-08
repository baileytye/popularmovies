package com.tye.popularmoviesstage1.database;

import com.tye.popularmoviesstage1.TMDB.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FavoriteMoviesDao {

    @Query("SELECT * FROM movie ORDER BY popularity")
    LiveData<List<Movie>> loadFavoriteMoviesByPopularity();

    @Query("SELECT * FROM movie ORDER BY vote_average")
    LiveData<List<Movie>> loadFavoriteMoviesByRating();

    @Insert
    void insertFavoriteMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(Movie movie);

    @Delete
    void deleteFavoriteMovie(Movie movie);

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> loadFavoriteMovieById(int id);

}
