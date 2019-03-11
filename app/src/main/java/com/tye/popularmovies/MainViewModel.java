package com.tye.popularmovies;

import android.app.Application;
import android.util.Log;

import com.tye.popularmovies.Models.Movie;
import com.tye.popularmovies.database.AppDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private MovieRepository movieRepository;
    private LiveData<List<Movie>> favoriteMovies;


    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        movieRepository = new MovieRepository(application);
        favoriteMovies = movieRepository.getFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies(){
        return favoriteMovies;
    }

    public void insert(Movie movie){
        movieRepository.insert(movie);
    }

    public void deleteTable(){
        movieRepository.deleteTable();
    }

}
