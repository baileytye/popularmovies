package com.tye.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ItemViewHolder> {

    final private ListItemClickListener mOnClickListener;

    private int mNumberOfItems;

    private static int viewHolderCount;

    private static List<Movie> mMovies;


    public MovieListAdapter(int numberOfItems, ListItemClickListener listener){
        mNumberOfItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }


    public void setMovies(List<Movie> movies){
        mMovies = movies;
        mNumberOfItems = mMovies.size();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,viewGroup,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);

        viewHolderCount++;


        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        if(mMovies != null)
            itemViewHolder.bind(mMovies.get(i));
        Log.v("Binding View", "View bind at " + String.valueOf(i));
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }



    public interface ListItemClickListener{
        void onListItemClick(int position);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView movieImage;

        public ItemViewHolder(View itemView){
            super(itemView);

            movieImage = itemView.findViewById(R.id.iv_movie_item_image);

            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie){

            String movieImagePath = "https://image.tmdb.org/t/p/" + "w342" + movie.getPoster_path();
            Picasso.get().load(movieImagePath).into(movieImage);
        }


        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition());
            Log.v("Adapter Position", "Position: " + getAdapterPosition());

        }
    }
}
