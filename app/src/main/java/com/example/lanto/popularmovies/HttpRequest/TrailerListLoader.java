package com.example.lanto.popularmovies.HttpRequest;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.lanto.popularmovies.Data.Trailer;

import java.util.List;

public class TrailerListLoader extends AsyncTaskLoader<List<Trailer>> {

    private final String mUrl;

    public TrailerListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {
        if (mUrl.length() == 0 ) return null;

        return DataFetcher.getTrailers(mUrl);
    }


}
