package com.tye.popularmoviesstage1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mRatingTextView;
    private TextView mSynopsysTextView;

    private ImageView mPosterImageView;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTitleTextView = findViewById(R.id.tv_details_title);
        mRatingTextView = findViewById(R.id.tv_details_rating);
        mReleaseDateTextView = findViewById(R.id.tv_details_release_date);
        mSynopsysTextView = findViewById(R.id.tv_details_synopsys);
        mPosterImageView = findViewById(R.id.iv_details_poster);

        Intent intent = getIntent();

        if(intent != null){
            if(intent.hasExtra(Intent.EXTRA_TEXT)){
                Gson gson = new Gson();
                mMovie = gson.fromJson(intent.getStringExtra(Intent.EXTRA_TEXT),Movie.class);
                setDetails();
            }
        }

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
}
