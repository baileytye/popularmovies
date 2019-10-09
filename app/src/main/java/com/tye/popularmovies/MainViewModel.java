package com.tye.popularmovies;

import android.app.Application;
import android.util.Log;

import com.tye.popularmovies.Models.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private final MovieRepository movieRepository;
    private final LiveData<List<Movie>> favoriteMovies;


    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        movieRepository = new MovieRepository(application);
        favoriteMovies = movieRepository.getFavoriteMovies();
    }

    LiveData<List<Movie>> getFavoriteMovies(){
        return favoriteMovies;
    }

    void deleteTable(){
        movieRepository.deleteTable();
    }

}
