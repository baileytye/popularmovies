package com.tye.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    TextView mTitleTextView;
    TextView mReleaseDateTextView;
    TextView mRatingTextView;
    TextView mSysnopsysTextView;
    ImageView mPosterImageView;

    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTitleTextView = findViewById(R.id.tv_details_title);
        mRatingTextView = findViewById(R.id.tv_details_rating);
        mReleaseDateTextView = findViewById(R.id.tv_details_release_date);
        mSysnopsysTextView = findViewById(R.id.tv_details_synopsys);
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

    private void setDetails(){
        String movieImagePath = "https://image.tmdb.org/t/p/" + "w342" + mMovie.getPoster_path();
        Picasso.get().load(movieImagePath).into(mPosterImageView);
        mTitleTextView.setText(mMovie.getOriginal_title());
        mSysnopsysTextView.setText(mMovie.getOverview());
        mReleaseDateTextView.setText(mMovie.getRelease_date());
        mRatingTextView.setText(String.valueOf(mMovie.getVote_average()) + "/10");
    }
}
