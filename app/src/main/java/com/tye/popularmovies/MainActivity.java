package com.tye.popularmovies;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tye.popularmovies.Adapters.MovieListAdapter;
import com.tye.popularmovies.Models.Movie;
import com.tye.popularmovies.Models.MovieResults;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements MovieListAdapter.ListItemClickListener{

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Tags used for determining which data to fetch
     */
    private static final int ORDER_POPULAR = 0;
    private static final int ORDER_RATINGS = 1;
    private static final int ORDER_FAVORITES = 2;
    private static final int ORDER_NONE = 3;

    // Extra tags
    public static final String EXTRA_MOVIE = "extra-movie";
    private static final String EXTRA_CURRENT_ORDER = "current-order-extra";
    public static final String EXTRA_FAVORITES = "extra-favorites";

    //Start current order none of above to force update data
    private static int currentOrder;

    //Bind views
    @BindView(R.id.rv_movie_list) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_movies) ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message) TextView mErrorMessageTextView;

    private MovieListAdapter mAdapter;
    private List<Movie> mMovies;

    //Had to be array list to be put in bundle
    private ArrayList<Movie> mFavorites;
    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAdapter = new MovieListAdapter(0,this, this);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        currentOrder = ORDER_NONE;

        setupViewModel();

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(EXTRA_FAVORITES)) {
                mFavorites = savedInstanceState.getParcelableArrayList(EXTRA_FAVORITES);
            }
            if (savedInstanceState.containsKey(EXTRA_CURRENT_ORDER)) {
                retrieveMovieList(savedInstanceState.getInt(EXTRA_CURRENT_ORDER));
            }
        } else {
            retrieveMovieList(ORDER_POPULAR);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CURRENT_ORDER, currentOrder);
        outState.putParcelableArrayList(EXTRA_FAVORITES, mFavorites);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.menu_popular:
                retrieveMovieList(ORDER_POPULAR);
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.label_popular_movies));
                }
                break;
            case R.id.menu_ratings:
                retrieveMovieList(ORDER_RATINGS);
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.label_highest_rated_movies));
                }
                break;
            case R.id.menu_favorites:
                retrieveMovieList(ORDER_FAVORITES);
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.label_favorite_movies));
                }
                break;
            case R.id.menu_clear_favorites:
                mMainViewModel.deleteTable();
                showEmptyFavorites();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int position) {

        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);

        if(currentOrder == ORDER_FAVORITES){
            intent.putExtra(EXTRA_MOVIE, mFavorites.get(position));
        } else{
            intent.putExtra(EXTRA_MOVIE, mMovies.get(position));
        }
        startActivity(intent);
    }

    /**
     * Sets up the view model
     */
    private void setupViewModel() {
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getFavoriteMovies().observe(this, favoriteMovies -> {
            mFavorites = new ArrayList<>(favoriteMovies);
            Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
            if(currentOrder == ORDER_FAVORITES) {
                mAdapter.setMovies(favoriteMovies);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * Shows the progress bar and hides the recycler view/error message
     */
    private void showProgressBar(){
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the recycler view and hides the progress bar/error message
     */
    private void showRecyclerView(){
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Shows error view and hides recycler/progress bar
     */
    private void showErrorMessage(String errorCode){
        mErrorMessageTextView.setText(getString(R.string.error));
        mErrorMessageTextView.append(errorCode);
        mErrorMessageTextView.append(getString(R.string.click_to_retry));
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        //Reset order to none to prevent toasts during error
        currentOrder = 3;
    }

    /**
     * Show empty favorites
     */
    private void showEmptyFavorites(){

        mErrorMessageTextView.setText(getString(R.string.add_favorites_to_see));
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

    }

    /**
     * Retrieves movie list from server and informs adapter that data has changed
     * @param order order for the movies to be retrieved
     */
    private void retrieveMovieList(int order){

        //Check to see if order requested is same as displayed, and return if so
        if(order == currentOrder){
            Toast.makeText(this,getString(R.string.already_in_order),Toast.LENGTH_SHORT).show();
            return;
        } else {
            currentOrder = order;
        }

        showProgressBar();

        if(order == ORDER_FAVORITES){
            mAdapter.setMovies(mFavorites);
            mAdapter.notifyDataSetChanged();
            currentOrder = ORDER_FAVORITES;

            if(mFavorites.isEmpty()){
                showEmptyFavorites();
            } else {
                showRecyclerView();
            }
            return;
        }

        //Use retrofit to get data from movie database
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_movies))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBApi tmdbApi = retrofit.create(TMDBApi.class);

        Call<MovieResults> call;

        //Set call based on order requested
        if (order == ORDER_POPULAR)
            call = tmdbApi.getMoviesPopular();
        else
            call = tmdbApi.getMoviesRatings();

         //Retrieve data from server and then tell the adapter that data has changed
        call.enqueue(new Callback<MovieResults>() {

            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if(!response.isSuccessful()){

                    Log.e("HTTP Request Error", String.valueOf(response.code()));
                    showErrorMessage(String.valueOf(response.code()));
                    return;
                }
                if (response.body() != null) {
                    mMovies = response.body().getMovies();
                    mAdapter.setMovies(mMovies);
                    mAdapter.notifyDataSetChanged();
                    showRecyclerView();
                } else {
                    Log.e("Movie List Error", "Movie list is null");
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                showErrorMessage(t.getMessage());
                Log.e("TMDB Call Error", t.getMessage());
            }
        });

    }

    /**
     * Reset movie order, try to retrieve movies again
     * @param v clicked view
     */
    public void errorOnClick(View v){
        currentOrder = 3;
        retrieveMovieList(ORDER_POPULAR);
    }
    
}
