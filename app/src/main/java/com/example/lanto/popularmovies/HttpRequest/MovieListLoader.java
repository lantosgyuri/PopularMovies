package com.example.lanto.popularmovies.HttpRequest;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.lanto.popularmovies.Data.Movie;

import java.util.List;

public class MovieListLoader extends AsyncTaskLoader<List<Movie>> {

    private final String mUrl;
    private List<Movie> mMovies;

    public MovieListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        if(mMovies != null) deliverResult(mMovies);
        else forceLoad();

    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl.length() == 0 ) return null;

        return DataFetcher.getMovieList(mUrl);
    }

    @Override
    public void deliverResult(List<Movie> data) {
        mMovies = data;
        super.deliverResult(data);
    }
}
