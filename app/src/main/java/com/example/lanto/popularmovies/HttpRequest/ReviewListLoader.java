package com.example.lanto.popularmovies.HttpRequest;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.lanto.popularmovies.Data.Movie;
import com.example.lanto.popularmovies.Data.Review;

import java.util.List;

public class ReviewListLoader extends AsyncTaskLoader<List<Review>> {

    private String mUrl;

    public ReviewListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Review> loadInBackground() {
        if (mUrl.length() == 0 ) return null;

        return DataFetcher.getReviews(mUrl);
    }


}
