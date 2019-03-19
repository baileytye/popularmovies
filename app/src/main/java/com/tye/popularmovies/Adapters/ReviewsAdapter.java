package com.tye.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tye.popularmovies.R;
import com.tye.popularmovies.Models.Review;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ItemViewHolder> {

    private int mNumberOfItems;
    private static List<Review> mReviews;

    public ReviewsAdapter(int mNumberOfItems) {
        this.mNumberOfItems = mNumberOfItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if(mReviews != null){
            holder.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    /**
     * Setter for reviews, also sets numberOfItems
     * @param reviews reviews tos et
     */
    public void setReviews(List<Review> reviews){
        mReviews = reviews;
        mNumberOfItems = reviews.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        final TextView mAuthor;
        final TextView mContent;


        ItemViewHolder(View itemView) {
            super(itemView);

            mAuthor = itemView.findViewById(R.id.tv_author);
            mContent = itemView.findViewById(R.id.tv_review_item_content);
        }


        void bind(int position) {
            mAuthor.setText(mReviews.get(position).getAuthor());
            mContent.setText(mReviews.get(position).getContent());
        }
    }
}
