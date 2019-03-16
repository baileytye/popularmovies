package com.tye.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.tye.popularmovies.Adapters.ReviewsAdapter;
import com.tye.popularmovies.Models.Review;
import com.tye.popularmovies.Models.ReviewResults;

import java.util.List;

public class ReviewsActivity extends AppCompatActivity{

    @BindView(R.id.rv_reviews) RecyclerView mRecyclerView;

    private int id;
    private ReviewsAdapter mAdapter;
    private List<Review> mReviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        ButterKnife.bind(this);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        id = -1;

        Intent intent = getIntent();

        if(intent != null){
            if(intent.hasExtra(DetailsActivity.EXTRA_ID)){
                id = intent.getIntExtra(DetailsActivity.EXTRA_ID, -1);
            }
        }

        mAdapter = new ReviewsAdapter(0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        retrieveReviews();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {

            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void retrieveReviews(){

        //showProgressBar();

        //Use retrofit to get data from movie database
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_movies))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBApi tmdbApi = retrofit.create(TMDBApi.class);

        if(id == -1){
            return;
        }
        Call<ReviewResults> call = tmdbApi.getReviews(id);


        //Retrieve data from server and then tell the adapter that data has changed
        call.enqueue(new Callback<ReviewResults>() {

            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                if(!response.isSuccessful()){

                    Log.e("HTTP Request Error", String.valueOf(response.code()));
                    //showErrorMessage(String.valueOf(response.code()));
                    return;
                }
                if (response.body() != null) {
                    mReviews = response.body().getReviews();
                    mAdapter.setReviews(mReviews);
                    mAdapter.notifyDataSetChanged();
                    //showRecyclerView();

                } else {
                    Log.e("Movie List Error", "Movie list is null");
                }
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {
                //showErrorMessage(t.getMessage());
                Log.e("TMDB Call Error", t.getMessage());
            }
        });


    }




}
