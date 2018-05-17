package com.example.lanto.popularmovies.ViewAdapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lanto.popularmovies.MainActivity;
import com.example.lanto.popularmovies.R;
import com.example.lanto.popularmovies.SqlData.MoviesContract;
import com.squareup.picasso.Picasso;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MovieViewHolder>{
    private Context mContext;
    private Cursor mCursor;

    public FavoriteMovieAdapter (Context context){
        mContext = context;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private ImageView posterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.favorite_item_title);
            posterImageView = itemView.findViewById(R.id.favorite_item_poster);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorite_movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) return;

        String title = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITEL));
        String posterUrl = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_URL));

        if(MainActivity.NETWORK_FLAG == true){
            Picasso.with(mContext)
                    .load(posterUrl)
                    .into(holder.posterImageView);
        } else{
            holder.posterImageView.setVisibility(View.INVISIBLE);
            holder.titleTextView.setVisibility(View.VISIBLE);
            holder.titleTextView.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;

        if(newCursor != null)notifyDataSetChanged();
    }

    public void addAll (Cursor cursor){
        mCursor = cursor;
    }
}