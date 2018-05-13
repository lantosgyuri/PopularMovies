package com.example.lanto.popularmovies.RecycleViewAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lanto.popularmovies.Data.Trailer;
import com.example.lanto.popularmovies.R;

import java.util.Collections;
import java.util.List;

public class TrailerRecycleAdapter extends RecyclerView.Adapter<TrailerRecycleAdapter.TrailerViewHodler> {

    private trailerClickListener mTrailerClickListener;
    private List<Trailer> trailerList = Collections.emptyList();

    public interface trailerClickListener{
        void onItemClick(int position);
        void onLongClick(int position);
    }

    public TrailerRecycleAdapter(){}

    @NonNull
    @Override
    public TrailerViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_recycle_view_item, parent, false);

        return new TrailerViewHodler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHodler holder, int position) {
        Trailer currentTrailer = trailerList.get(position);

        String name = currentTrailer.getmTrailerName();

        holder.mTrailerName.setText(name);
    }


    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public void addAll(List<Trailer> trailers){
        trailerList =trailers;
    }

    public Trailer getItem(int position){
        return trailerList.get(position);
    }

    public void setTrailerClickListener(trailerClickListener listener) {
        mTrailerClickListener = listener;
    }

    //View Holder
    public class TrailerViewHodler extends RecyclerView.ViewHolder{

        TextView mTrailerName;

        public TrailerViewHodler(View itemView) {
            super(itemView);
            mTrailerName = itemView.findViewById(R.id.trailer_recycle_name_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTrailerClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mTrailerClickListener.onItemClick(position);
                    }
                }
            }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mTrailerClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mTrailerClickListener.onLongClick(position);
                        }
                    }
                    return true;
                }
            });


            }
        }
    }