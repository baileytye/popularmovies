package com.tye.popularmovies;

import android.app.ActionBar;
import android.app.Application;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.tye.popularmovies.Adapters.TrailersAdapter;
import com.tye.popularmovies.Models.Movie;
import com.tye.popularmovies.Models.Review;
import com.tye.popularmovies.Models.ReviewResults;
import com.tye.popularmovies.Models.Trailer;
import com.tye.popularmovies.Models.TrailerResults;
import com.tye.popularmovies.database.AppDatabase;
import com.tye.popularmovies.database.FavoriteMoviesDao;

import java.util.List;

//TODO: add bundle save state to save if its a favorite or not to prevent recalling database on rotation
//TODO: save instance state so that rotation does not recall the database
public class DetailsActivity extends AppCompatActivity implements TrailersAdapter.ListItemClickListener{

    private static final String TAG = "DetailsActivity";
    @BindView(R.id.tv_details_title) TextView mTitleTextView;
    @BindView(R.id.tv_details_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_details_rating) TextView mRatingTextView;
    @BindView(R.id.tv_details_synopsys) TextView mSynopsysTextView;
    @BindView(R.id.tv_details_error_message) TextView mErrorMessageTextView;
    @BindView(R.id.tv_details_review) ExpandableTextView mReviewTextView;
    //@BindView(R.id.tv_read_more_button) TextView mReadMoreTextView;
    @BindView(R.id.label_see_more) TextView mSeeMoreReviewsTextView;

    @BindView(R.id.pb_loading_trailers) ProgressBar mProgressBar;

    @BindView(R.id.iv_details_poster) ImageView mPosterImageView;

    @BindView(R.id.rv_details_trailers) RecyclerView mRecyclerView;

    private MenuItem mFavoritesMenuItem;
    private Movie mMovie;
    private List<Review> mReviews;
    private List<Trailer> mTrailers;
    private TrailersAdapter mAdapter;
    private MovieRepository mMovieRepository;

    public static final String EXTRA_ID = "extra_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        mMovieRepository = new MovieRepository(getApplication());

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


        retrieveReviews();
        retrieveTrailers();


    }

    private void retrieveReviews(){

        int id = mMovie.getId();

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
                    if(!mReviews.isEmpty()){
                        String reviewText = "Review by " + mReviews.get(0).getAuthor() + ":\n"  + mReviews.get(0).getContent();
                        mReviewTextView.setText(reviewText);
                        if(mReviews.size() > 1){
                            mSeeMoreReviewsTextView.setVisibility(View.VISIBLE);
                        }

                    } else {
                        mReviewTextView.setText(getString(R.string.details_no_reviews_yet));
                        mSeeMoreReviewsTextView.setVisibility(View.INVISIBLE);
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_details, menu);

        mFavoritesMenuItem = menu.findItem(R.id.menu_details_favorite);

        new CheckFavoriteTask(getApplication()).execute(mMovie);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.menu_details_favorite:
                if(mMovie.isFavorite()){
                    mMovie.setFavorite(false);
                    setFavoriteIconColor(R.style.white);
                    Toast.makeText(this,"Removed from favorites.", Toast.LENGTH_LONG).show();
                    mMovieRepository.remove(mMovie);
                }
                else{
                    setFavoriteIconColor(R.style.favorite);
                    mMovie.setFavorite(true);
                    Toast.makeText(this,"Added to favorites.", Toast.LENGTH_LONG).show();
                    mMovieRepository.insert(mMovie);
                }
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFavoriteIconColor(int colorId){
        final ContextThemeWrapper wrapper  = new ContextThemeWrapper(this, colorId);
        final Drawable star = VectorDrawableCompat.create(getResources(), R.drawable.ic_baseline_star_rate_18px, wrapper.getTheme());
        mFavoritesMenuItem.setIcon(star);
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
                    //TODO: Add if there are no trailers show message

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

    private class CheckFavoriteTask extends AsyncTask<Movie, Void, Boolean>{

        FavoriteMoviesDao favoriteMoviesDao;

        public CheckFavoriteTask(Application application){
            favoriteMoviesDao = AppDatabase.getInstance(application.getApplicationContext()).favoriteMoviesDao();
        }


        @Override
        protected Boolean doInBackground(Movie... movies) {
            Log.d("Database: ", "Loading favorite message by ID from database");
            return (favoriteMoviesDao.loadFavoriteMovieById(movies[0].getId()) != null);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean) {
                mMovie.setFavorite(true);
                setFavoriteIconColor(R.style.favorite);
            } else {
                setFavoriteIconColor(R.style.white);
            }
        }
    }


}
