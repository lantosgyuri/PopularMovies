package com.example.lanto.popularmovies.RecycleViewAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lanto.popularmovies.Data.Review;
import com.example.lanto.popularmovies.R;

import java.util.Collections;
import java.util.List;


public class ReviewRecycleAdapter extends RecyclerView.Adapter<ReviewRecycleAdapter.ReviewViewHolder> {

    private List<Review> mReviewList = Collections.EMPTY_LIST;

    public ReviewRecycleAdapter(){}


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_recycle_view_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review currentReview = mReviewList.get(position);

        String author = currentReview.getmAuthor();
        String content = currentReview.getmContent();

        holder.authorTextView.setText(author);
        holder.contentTextView.setText(content);

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public void addAll(List<Review> reviewList){
        mReviewList = reviewList;
    }

    //ViewHolder class
    public class ReviewViewHolder extends RecyclerView.ViewHolder{

        TextView authorTextView;
        TextView contentTextView;

        public ReviewViewHolder(View itemView){
            super(itemView);
            authorTextView = itemView.findViewById(R.id.review_recycle_author_text_view);
            contentTextView = itemView.findViewById(R.id.review_recycle_content_text_view);
        }
    }
}
