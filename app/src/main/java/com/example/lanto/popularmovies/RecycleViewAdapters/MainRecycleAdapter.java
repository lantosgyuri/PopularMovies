package com.example.lanto.popularmovies.RecycleViewAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lanto.popularmovies.Data.Movie;
import com.example.lanto.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.ViewHolder> {

    private List<Movie> movieList = Collections.emptyList();
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public MainRecycleAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_item_layout, parent, false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie currentMovie = movieList.get(position);
        String posterUrl = currentMovie.getmPosterUrl();
        Picasso.with(mContext)
                .load(posterUrl)
                .into(holder.mPoster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public Movie getItem (int position){

        return movieList.get(position);
    }

    public void addAll(List<Movie> movies){
        movieList = movies;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mPoster;

        public ViewHolder(View view){
            super(view);
            mPoster = view.findViewById(R.id.item_poster);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }



}
