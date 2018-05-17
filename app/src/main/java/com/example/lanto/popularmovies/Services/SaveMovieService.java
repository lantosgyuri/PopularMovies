package com.example.lanto.popularmovies.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.lanto.popularmovies.Data.Movie;
import com.example.lanto.popularmovies.DetailsActivity;
import com.example.lanto.popularmovies.SqlData.MoviesContract;
import com.example.lanto.popularmovies.SqlData.MoviesContract.MoviesEntry;

public class SaveMovieService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public SaveMovieService() {
        super("SaveMovieService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        if (intent != null) {
            Movie mMovie = intent.getExtras().getParcelable(DetailsActivity.MOVIE_INTENT_KEY);

            ContentValues values = new ContentValues();
            values.put(MoviesEntry.COLUMN_TITEL, mMovie.getmTitle());
            values.put(MoviesEntry.COLUMN_POSTER_URL, mMovie.getmPosterUrl());
            values.put(MoviesEntry.COLUMN_PLOT, mMovie.getmPlot());
            values.put(MoviesEntry.COLUMN_RELEASE_DATE, mMovie.getmReleaseDate());
            values.put(MoviesEntry.COLUMN_VOTE_AVERAGE, mMovie.getmVoteAvarage());

            getContentResolver().insert(MoviesContract.MOVIES_URI, values);

    }
}
}
