package com.tye.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements MovieListAdapter.ListItemClickListener{

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

        showProgressBar();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBApi tmdbApi = retrofit.create(TMDBApi.class);

        Call<Results> call = tmdbApi.getMovies();

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


    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, position);

        startActivity(intent);
    }

    /**
     * Shows the progress bar and hides the recycler view
     */
    public void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the recycler view and hides the progress bar
     */
    public void showRecyclerView(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


}
