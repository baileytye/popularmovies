package com.tye.popularmoviesstage1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_details_title) TextView mTitleTextView;
    @BindView(R.id.tv_details_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_details_rating) TextView mRatingTextView;
    @BindView(R.id.tv_details_synopsys) TextView mSynopsysTextView;

    @BindView(R.id.iv_details_poster) ImageView mPosterImageView;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if(intent != null){
            if(intent.hasExtra(MainActivity.EXTRA_MOVIE)){

                mMovie = intent.getParcelableExtra(MainActivity.EXTRA_MOVIE);
                setDetails();
            }
        }

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
}
