package com.tye.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tye.popularmovies.R;
import com.tye.popularmovies.Models.Trailer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ItemViewHolder> {

    final private TrailersAdapter.ListItemClickListener mOnClickListener;
    private int mNumberOfItems;
    private static List<Trailer> mTrailers;

    public TrailersAdapter(int mNumberOfItems, ListItemClickListener listItemClickListener) {
        this.mNumberOfItems = mNumberOfItems;
        this.mOnClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if(mTrailers != null){
            holder.bind(position);
        }
    }


    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    /**
     * Setter for trailers, also sets numberOfItems
     * @param trailers trailers to set
     */
    public void setTrailers(List<Trailer> trailers){
        mTrailers = trailers;
        mNumberOfItems = trailers.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView mTrailerTitle;


        ItemViewHolder(View itemView) {
            super(itemView);

            mTrailerTitle = itemView.findViewById(R.id.tv_trailer_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }

        void bind(int position) {
            mTrailerTitle.setText(mTrailers.get(position).getName());
        }
    }
}
