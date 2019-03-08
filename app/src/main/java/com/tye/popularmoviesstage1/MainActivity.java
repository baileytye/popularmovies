package com.tye.popularmoviesstage1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tye.popularmoviesstage1.TMDB.Movie;
import com.tye.popularmoviesstage1.TMDB.MovieResults;
import com.tye.popularmoviesstage1.TMDB.Review;
import com.tye.popularmoviesstage1.TMDB.ReviewResults;
import com.tye.popularmoviesstage1.TMDB.TMDBApi;
import com.tye.popularmoviesstage1.TMDB.TrailerResults;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements MovieListAdapter.ListItemClickListener{


    public static final String EXTRA_MOVIE = "extra-movie";

    /**
     * Tags used for determining which data to fetch
     */
    private static final int ORDER_POPULAR = 0;
    private static final int ORDER_RATINGS = 1;

    //Start current order none of above to force update data
    private static int currentOrder = 3;

    @BindView(R.id.rv_movie_list) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_movies) ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message) TextView mErrorMessageTextView;


    private MovieListAdapter mAdapter;
    private List<Movie> mMovies;

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

        retrieveMovieList(ORDER_POPULAR);
    }


    @Override
    protected void onStop() {
        super.onStop();
        currentOrder = 3;

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
                break;
            case R.id.menu_ratings:
                retrieveMovieList(ORDER_RATINGS);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onListItemClick(int position) {

        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);

        intent.putExtra(EXTRA_MOVIE, mMovies.get(position));
        startActivity(intent);
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
