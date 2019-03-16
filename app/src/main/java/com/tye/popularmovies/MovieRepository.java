package com.tye.popularmovies;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.tye.popularmovies.Models.Movie;
import com.tye.popularmovies.database.AppDatabase;
import com.tye.popularmovies.database.FavoriteMoviesDao;

import java.util.List;

import androidx.lifecycle.LiveData;

class MovieRepository {


    private static FavoriteMoviesDao favoriteMoviesDao;

    private final LiveData<List<Movie>> favoriteMovies;

    MovieRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application.getApplicationContext());
        favoriteMoviesDao = database.favoriteMoviesDao();
        favoriteMovies = favoriteMoviesDao.loadFavoriteMoviesByName();
    }

    LiveData<List<Movie>> getFavoriteMovies(){
        Log.d("Repo ", "All data retrieved from database");
        return favoriteMovies;
    }




    void insert(final Movie movie){
        new InsertAsyncTask().execute(movie);
        Log.d("Repo ", "Movie " + movie.getOriginal_title() + " inserted into database ");
    }

    void remove(final Movie movie){
        Log.d("Repo ", "Movie " + movie.getOriginal_title() + " removed from database ");
        new RemoveAsyncTask().execute(movie);
    }

    void deleteTable(){
        Log.d("Repo ", "Database cleared ");
        new ClearAsyncTask().execute();
    }




    private static class InsertAsyncTask extends AsyncTask<Movie,Void,Void>{

        @Override
        protected Void doInBackground(Movie... movies) {
            favoriteMoviesDao.insertFavoriteMovie(movies[0]);
            return null;
        }
    }

    private static class RemoveAsyncTask extends AsyncTask<Movie,Void,Void>{

        @Override
        protected Void doInBackground(Movie... movies) {
            favoriteMoviesDao.deleteFavoriteMovie(movies[0]);
            return null;
        }
    }

    private static class ClearAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            favoriteMoviesDao.nukeTable();
            return null;
        }
    }


}
