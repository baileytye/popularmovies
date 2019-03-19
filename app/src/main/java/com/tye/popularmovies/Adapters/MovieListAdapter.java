package com.tye.popularmovies.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tye.popularmovies.R;
import com.tye.popularmovies.Models.Movie;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ItemViewHolder> {

    final private ListItemClickListener mOnClickListener;

    private int mNumberOfItems;

    private static List<Movie> mMovies;

    private final Context mContext;

    public MovieListAdapter(int numberOfItems, ListItemClickListener listener, Context context){
        mNumberOfItems = numberOfItems;
        mOnClickListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,viewGroup,false);

        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

        if(mMovies != null)
            itemViewHolder.bind(mMovies.get(i), mContext.getString(R.string.image_url));
        Log.v("Binding View", "View bind at " + String.valueOf(i));
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }


    /**
     * Setter for data list movies
     * @param movies data to assign
     */
    public void setMovies(List<Movie> movies){
        mMovies = movies;
        if(mMovies != null){
            mNumberOfItems = mMovies.size();
        } else {
            mNumberOfItems = 0;
        }
    }

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView movieImage;

        ItemViewHolder(View itemView){
            super(itemView);

            movieImage = itemView.findViewById(R.id.iv_movie_item_image);

            itemView.setOnClickListener(this);
        }

        void bind(Movie movie, String imageURL){

            String movieImagePath = imageURL + movie.getPoster_path();
            Picasso.get().load(movieImagePath).into(movieImage);
        }


        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition());
            Log.v("Adapter Position", "Position: " + getAdapterPosition());

        }
    }
}
