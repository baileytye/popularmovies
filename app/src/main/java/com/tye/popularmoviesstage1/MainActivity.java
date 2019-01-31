package com.tye.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements MovieListAdapter.ListItemClickListener{

    private static final int ORDER_POPULAR = 0;
    private static final int ORDER_RATINGS = 1;

    private static int currentOrder = 3;

    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private ProgressBar mProgressBar;
    private List<Movie> mMovies;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.pb_loading_movies);
        mRecyclerView = findViewById(R.id.rv_movie_list);

        mAdapter = new MovieListAdapter(0,this);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        retrieveMovieList(ORDER_POPULAR);
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
        Gson gson = new Gson();
        String myJson = gson.toJson(mMovies.get(position));

        intent.putExtra(Intent.EXTRA_TEXT, myJson);

        startActivity(intent);
    }

    /**
     * Shows the progress bar and hides the recycler view
     */
    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the recycler view and hides the progress bar
     */
    private void showRecyclerView(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * Retrieves movie list from server and informs adapter that data has changed
     * @param order order for the movies to be retrieved
     */
    private void retrieveMovieList(int order){

        Log.i("ORDER", "Order in: " + order + ", current order: " + currentOrder);


        if(order == currentOrder){
            Toast.makeText(this,"Already in that order!",Toast.LENGTH_SHORT).show();
            return;
        } else {
            currentOrder = order;
        }
        showProgressBar();

        /*
         * Use retrofit to get data from movie database
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBApi tmdbApi = retrofit.create(TMDBApi.class);

        Call<Results> call;

        if (order == ORDER_POPULAR)
            call = tmdbApi.getMoviesPopular();
        else
            call = tmdbApi.getMoviesRatings();

        /*
         * Retrieve data from server and then tell the adapter that data has changed
         */
        call.enqueue(new Callback<Results>() {

            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if(!response.isSuccessful()){

                    Log.w("HTTP Request Error", String.valueOf(response.code()));
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
            public void onFailure(Call<Results> call, Throwable t) {
                Log.e("TMDB Call Error", t.getMessage());
            }
        });
    }


}
