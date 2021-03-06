package com.example.lanto.popularmovies;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Intent;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.lanto.popularmovies.Data.Movie;
import com.example.lanto.popularmovies.Data.Review;
import com.example.lanto.popularmovies.Data.Trailer;
import com.example.lanto.popularmovies.HttpRequest.ReviewListLoader;
import com.example.lanto.popularmovies.HttpRequest.TrailerListLoader;
import com.example.lanto.popularmovies.SqlData.MoviesContract;
import com.example.lanto.popularmovies.ViewAdapters.ReviewRecycleAdapter;
import com.example.lanto.popularmovies.ViewAdapters.TrailerRecycleAdapter;
import com.example.lanto.popularmovies.Services.SaveMovieService;
import com.example.lanto.popularmovies.databinding.ActivityDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements TrailerRecycleAdapter.trailerClickListener {

    private Movie mMovie;

    private ActivityDetailsBinding mBinding;
    private ReviewRecycleAdapter mReviewRecycleAdapter;
    private TrailerRecycleAdapter mTrailerRecycleAdapter;
    private String movieID ="";
    public final static String MOVIE_INTENT_KEY = "Movie";

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private static final int TRAILER_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        Intent intent = getIntent();

        //set text with movie details
        if (intent != null) {
            mMovie = intent.getExtras().getParcelable(getString(R.string.intent_movie_tag));
            mBinding.detailTitle.setText(mMovie.getmTitle());
            mBinding.detailPlot.setText(mMovie.getmPlot());
            mBinding.detailVote.setText(mMovie.getmVoteAvarage());
            mBinding.detailReleaseDatum.setText(mMovie.getmReleaseDate());
            Log.e(LOG_TAG, mMovie.getmPosterUrl());
            Picasso.with(this)
                    .load(mMovie.getmPosterUrl())
                    .into(mBinding.detailPoster);

            movieID = mMovie.getmId();

            getLoaderManager().initLoader(TRAILER_LOADER_ID, null, trailerListLoader);
            getLoaderManager().initLoader(REVIEW_LOADER_ID, null, reviewListLoader);
        }

        setReviewRecycleView();
        setTrailerRecycleView();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(getString(R.string.detail_save_instance_key),
                new int[]{ mBinding.detailScrollView.getScrollX(), mBinding.detailScrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray(getString(R.string.detail_save_instance_key));
        if(position != null)
            mBinding.detailScrollView.post(new Runnable() {
                public void run() {
                    mBinding.detailScrollView.scrollTo(position[0], position[1]);
                }
            });
    }

    private void setReviewRecycleView(){
        mBinding.reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.reviewsRecyclerView.setHasFixedSize(true);
        mReviewRecycleAdapter = new ReviewRecycleAdapter();
        mBinding.reviewsRecyclerView.setAdapter(mReviewRecycleAdapter);
    }

    private void setTrailerRecycleView() {
        mBinding.trailerRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBinding.trailerRecycleView.setHasFixedSize(true);
        mTrailerRecycleAdapter = new TrailerRecycleAdapter();
        mBinding.trailerRecycleView.setAdapter(mTrailerRecycleAdapter);
        mTrailerRecycleAdapter.setTrailerClickListener(this);

        if(mMovie.getIsFavorite()) {
            mBinding.fab.setImageResource(R.drawable.ic_remove_circle);
            mBinding.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri deleteUri = ContentUris.withAppendedId(MoviesContract.MOVIES_URI, mMovie.getmSqlId());
                    getContentResolver().delete(deleteUri, null, null );
                    Toast.makeText(DetailsActivity.this, R.string.deleted_movie_toast, Toast.LENGTH_LONG).show();
                }
            });

        } else {
            mBinding.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToSaveMovie = new Intent(DetailsActivity.this, SaveMovieService.class);
                    intentToSaveMovie.putExtra(MOVIE_INTENT_KEY, mMovie);
                    startService(intentToSaveMovie);
                    Toast.makeText(DetailsActivity.this, R.string.added_to_favorite_toast, Toast.LENGTH_LONG).show();
                }
            });
        }


    }


    //open link with youtube or browser
    @Override
    public void onItemClick(int position) {
        Trailer currentTrailer = mTrailerRecycleAdapter.getItem(position);
        String url = currentTrailer.getmTrailerUrl();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    //share Trailer link
    @Override
    public void onLongClick(int position) {
        Trailer currentTrailer = mTrailerRecycleAdapter.getItem(position);
        String url = currentTrailer.getmTrailerUrl();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    //loaderManager to get trailers
    private final LoaderManager.LoaderCallbacks<List<Trailer>> trailerListLoader =
            new LoaderManager.LoaderCallbacks<List<Trailer>>() {
                @Override
                public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
                    return new TrailerListLoader(DetailsActivity.this, Utils.makeTrailerUrl(movieID));
                }

                @Override
                public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
                    if (data.size() == 0) mBinding.trailerRecycleEmptyView.setVisibility(View.VISIBLE);
                    mTrailerRecycleAdapter.addAll(data);
                    mTrailerRecycleAdapter.notifyDataSetChanged();
                }


                @Override
                public void onLoaderReset(Loader<List<Trailer>> loader) {

                }
            };

    //loaderManager to get reviews
    private final LoaderManager.LoaderCallbacks<List<Review>> reviewListLoader =
            new LoaderManager.LoaderCallbacks<List<Review>>() {
                @Override
                public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
                    return new ReviewListLoader(DetailsActivity.this, Utils.makeReviewUrl(movieID));
                }

                @Override
                public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
                    if(data.size() == 0)mBinding.reviewRecycleEmptyView.setVisibility(View.VISIBLE);
                    mReviewRecycleAdapter.addAll(data);
                    mReviewRecycleAdapter.notifyDataSetChanged();
                }

                @Override
                public void onLoaderReset(Loader<List<Review>> loader) {

                }
            };


}
