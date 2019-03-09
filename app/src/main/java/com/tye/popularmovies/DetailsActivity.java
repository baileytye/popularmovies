package com.tye.popularmovies;

import android.app.ActionBar;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tye.popularmovies.Adapters.TrailersAdapter;
import com.tye.popularmovies.TMDB.Movie;
import com.tye.popularmovies.TMDB.TMDBApi;
import com.tye.popularmovies.TMDB.Trailer;
import com.tye.popularmovies.TMDB.TrailerResults;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements TrailersAdapter.ListItemClickListener{

    @BindView(R.id.tv_details_title) TextView mTitleTextView;
    @BindView(R.id.tv_details_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_details_rating) TextView mRatingTextView;
    @BindView(R.id.tv_details_synopsys) TextView mSynopsysTextView;
    @BindView(R.id.tv_details_error_message) TextView mErrorMessageTextView;

    @BindView(R.id.pb_loading_trailers) ProgressBar mProgressBar;

    @BindView(R.id.iv_details_poster) ImageView mPosterImageView;

    @BindView(R.id.rv_details_trailers) RecyclerView mRecyclerView;

    private Movie mMovie;
    private List<Trailer> mTrailers;
    private TrailersAdapter mAdapter;

    public static final String EXTRA_ID = "extra_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if(intent != null){
            if(intent.hasExtra(MainActivity.EXTRA_MOVIE)){
                mMovie = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);
                setDetails();
            }
        }

        mAdapter = new TrailersAdapter(0, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        retrieveTrailers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_details, menu);

        MenuItem item = menu.findItem(R.id.menu_favorite);
        final ContextThemeWrapper wrapper  = new ContextThemeWrapper(this, R.style.unfavorite);
        final Drawable star = VectorDrawableCompat.create(getResources(), R.drawable.ic_baseline_star_rate_18px, wrapper.getTheme());
        item.setIcon(star);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.menu_favorite:
                final ContextThemeWrapper wrapper  = new ContextThemeWrapper(this, R.style.favorite);
                final Drawable star = VectorDrawableCompat.create(getResources(), R.drawable.ic_baseline_star_rate_18px, wrapper.getTheme());
                item.setIcon(star);
                Toast.makeText(this,getString(R.string.added_to_favorites),Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * Sets the detail views
     */
    private void setDetails(){

        String movieImagePath =  getString(R.string.image_url) + mMovie.getPoster_path();
        Picasso.get().load(movieImagePath).into(mPosterImageView);

        mTitleTextView.setText(mMovie.getOriginal_title());
        mSynopsysTextView.setText(mMovie.getOverview());
        mReleaseDateTextView.setText(mMovie.getRelease_date());
        mRatingTextView.setText(String.valueOf(mMovie.getVote_average()) + "/10");
    }


    private void retrieveTrailers(){

        showProgressBar();

        //Use retrofit to get data from movie database
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_movies))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBApi tmdbApi = retrofit.create(TMDBApi.class);


        Call<TrailerResults> call = tmdbApi.getTrailers(mMovie.getId());


        //Retrieve data from server and then tell the adapter that data has changed
        call.enqueue(new Callback<TrailerResults>() {

            @Override
            public void onResponse(Call<TrailerResults> call, Response<TrailerResults> response) {
                if(!response.isSuccessful()){

                    Log.e("HTTP Request Error", String.valueOf(response.code()));
                    showErrorMessage(String.valueOf(response.code()));
                    return;
                }
                if (response.body() != null) {
                    mTrailers = response.body().getTrailers();
                    mAdapter.setTrailers(mTrailers);
                    mAdapter.notifyDataSetChanged();
                    showRecyclerView();

                } else {
                    Log.e("Movie List Error", "Movie list is null");
                }
            }

            @Override
            public void onFailure(Call<TrailerResults> call, Throwable t) {
                showErrorMessage(t.getMessage());
                Log.e("TMDB Call Error", t.getMessage());
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
    }


    @Override
    public void onListItemClick(int position) {
        String url = "https://www.youtube.com/watch?v=" + mTrailers.get(position).getKey();
        Log.d("YOUTUBE: ", url);
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent chooser = Intent.createChooser(youtubeIntent, "Open With");

        if(youtubeIntent.resolveActivity(getPackageManager()) != null){
            startActivity(chooser);
        }

    }



    public void startReviewActivity(View view){
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra(EXTRA_ID, mMovie.getId());

        startActivity(intent);
    }
}
