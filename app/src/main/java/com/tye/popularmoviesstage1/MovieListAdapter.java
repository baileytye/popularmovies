package com.tye.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ItemViewHolder> {

    final private ListItemClickListener mOnClickListener;

    private int mNumberOfItems;

    private static int viewHolderCount;

    public MovieListAdapter(int numberOfItems, ListItemClickListener listener){
        mNumberOfItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
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
        itemViewHolder.bind();
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }


    public interface ListItemClickListener{
        void onListItemClick();
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView movieImage;

        public ItemViewHolder(View itemView){
            super(itemView);

            movieImage = itemView.findViewById(R.id.iv_movie_item_image);

            itemView.setOnClickListener(this);
        }

        public void bind(){
            Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(movieImage);
        }


        @Override
        public void onClick(View view) {

        }
    }
}
