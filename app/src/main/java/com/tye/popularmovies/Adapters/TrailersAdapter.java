package com.tye.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tye.popularmovies.R;
import com.tye.popularmovies.TMDB.Trailer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ItemViewHolder> {

    private int mNumberOfItems;
    private static List<Trailer> mTrailers;
    private final Context mContext;


    public TrailersAdapter(int mNumberOfItems, Context mContext) {
        this.mNumberOfItems = mNumberOfItems;
        this.mContext = mContext;
    }


    public void setTrailers(List<Trailer> trailers){
        mTrailers = trailers;
        mNumberOfItems = trailers.size();
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
            holder.bind();
        }
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTrailerTitle;


        public ItemViewHolder(View itemView) {
            super(itemView);

            mTrailerTitle = itemView.findViewById(R.id.tv_trailer_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(),"Clicked",Toast.LENGTH_SHORT).show();
        }

        public void bind() {
            mTrailerTitle.setText("Test setting the title");
        }
    }
}
