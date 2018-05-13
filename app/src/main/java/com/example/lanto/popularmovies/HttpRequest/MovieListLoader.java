package com.example.lanto.popularmovies.HttpRequest;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.lanto.popularmovies.Data.Movie;

import java.util.List;

public class MovieListLoader extends AsyncTaskLoader<List<Movie>> {

    private String mUrl;

    public MovieListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl.length() == 0 ) return null;

        return DataFetcher.getMovieList(mUrl);
    }


}