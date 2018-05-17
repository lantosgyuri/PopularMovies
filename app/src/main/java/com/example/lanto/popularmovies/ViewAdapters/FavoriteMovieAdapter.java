package com.example.lanto.popularmovies.ViewAdapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lanto.popularmovies.MainActivity;
import com.example.lanto.popularmovies.R;
import com.example.lanto.popularmovies.SqlData.MoviesContract;
import com.squareup.picasso.Picasso;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MovieViewHolder> {
    private final Context mContext;
    private Cursor mCursor;
    private OnItemClickListenerCursor mListener;

    public interface OnItemClickListenerCursor {
        void onItemClickCursor(String title, String posterUrl, String plot, String avarage,
                               String id, String releaseDate, int sqlId);
    }

    public FavoriteMovieAdapter(Context context) {
        mContext = context;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final ImageView posterImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.favorite_item_title);
            posterImageView = itemView.findViewById(R.id.favorite_item_poster);

            // send the movie details form sql to main activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            int sqlId = mCursor.getInt(mCursor.getColumnIndex(MoviesContract.MoviesEntry._ID));
                            String title = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITEL));
                            String posterUrl = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_URL));
                            Log.e("cursor movie adatok", posterUrl);
                            String plot = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_PLOT));
                            String avarage = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE));
                            String id = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID));
                            String releaseDate = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE));
                            Log.e("cursor movie adatok", title + posterUrl + plot + avarage + id + releaseDate);
                            mListener.onItemClickCursor(title, posterUrl, plot, avarage, id, releaseDate, sqlId);
                        }
                    }
                }
            });
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
        if (!mCursor.moveToPosition(position)) return;

        String title = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITEL));
        String posterUrl = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_URL));

        if (MainActivity.NETWORK_FLAG == true) {
            Picasso.with(mContext)
                    .load(posterUrl)
                    .into(holder.posterImageView);
        } else {
            holder.posterImageView.setVisibility(View.INVISIBLE);
            holder.titleTextView.setVisibility(View.VISIBLE);
            holder.titleTextView.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void addAll(Cursor cursor) {
        mCursor = cursor;
    }

    public void setOnClickListener(OnItemClickListenerCursor listener) {
        mListener = listener;
    }
}