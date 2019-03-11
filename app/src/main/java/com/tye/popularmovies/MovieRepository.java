package com.tye.popularmovies;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.tye.popularmovies.Models.Movie;
import com.tye.popularmovies.database.AppDatabase;
import com.tye.popularmovies.database.FavoriteMoviesDao;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MovieRepository {


    private static FavoriteMoviesDao favoriteMoviesDao;

    private LiveData<List<Movie>> favoriteMovies;

    private Movie favorite;


    public MovieRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application.getApplicationContext());
        favoriteMoviesDao = database.favoriteMoviesDao();
        favoriteMovies = favoriteMoviesDao.loadFavoriteMoviesByName();
    }

    public LiveData<List<Movie>> getFavoriteMovies(){
        Log.d("DATABASE ", "All data retrieved from database");
        return favoriteMovies;
    }




    public void insert(final Movie movie){
        new InsertAsyncTask().execute(movie);
        Log.d("DATABASE ", "Movie " + movie.getOriginal_title() + " inserted into database ");
    }

    public void remove(final Movie movie){
        new RemoveAsyncTask().execute(movie);
    }

    public void deleteTable(){
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
