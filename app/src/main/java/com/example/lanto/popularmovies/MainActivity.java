package com.example.lanto.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lanto.popularmovies.Data.Movie;
import com.example.lanto.popularmovies.HttpRequest.MovieListLoader;
import com.example.lanto.popularmovies.SqlData.MoviesContract;
import com.example.lanto.popularmovies.ViewAdapters.FavoriteMovieAdapter;
import com.example.lanto.popularmovies.ViewAdapters.MainRecycleAdapter;
import com.example.lanto.popularmovies.SqlData.MoviesContract.MoviesEntry;

import java.util.List;

import static com.example.lanto.popularmovies.R.string.pref_value_favorite;

public class MainActivity extends AppCompatActivity implements
        MainRecycleAdapter.OnItemClickListener, FavoriteMovieAdapter.OnItemClickListenerCursor {

    private static final int LOADER_ID = 1;
    private static final int CURSOR_LOADER_ID = 2;
    public static boolean NETWORK_FLAG = false;

    private MainRecycleAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private TextView emptyViewCursor;

    private FavoriteMovieAdapter favoriteMovieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(Utils.setMainActivityTitle(this));

        //set recycleView
        setRecyclerView();

        //Check is there any internet connection.
        checkHttp();
        loadDataToRecycleView();

        //set notification
        Utils.setNotification(this);


    }

    private void setRecyclerView() {
        /*
        emptyView in case if there is no internet connection
        and there is no data to load into recycleView, or cursor is empty
         */
        emptyView = findViewById(R.id.main_activity_empty_view);
        emptyViewCursor = findViewById(R.id.main_activity_empty_view_cursor);

        recyclerView = findViewById(R.id.main_activity_recycle_view);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setHasFixedSize(true);

        mAdapter = new MainRecycleAdapter(this);
        mAdapter.setOnItemClickListener(this);
        favoriteMovieAdapter = new FavoriteMovieAdapter(this);
        favoriteMovieAdapter.setOnClickListener(this);

    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.main_menu_icon);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu_icon) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //check Internet connection
    private void checkHttp() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //I need to know in more than one place is there internet or not.
        if (networkInfo != null && networkInfo.isConnected()) NETWORK_FLAG = true;
    }

    //check the pref and load the selected category
    private void loadDataToRecycleView() {
        String prefCategory = Utils.getPrefCategory(this);

        if (prefCategory.equals(getString(pref_value_favorite))) {
            getLoaderManager().initLoader(CURSOR_LOADER_ID, null, cursorLoader);
        } else if (!NETWORK_FLAG) {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            getLoaderManager().initLoader(LOADER_ID, null, httpLoader);
        }

    }

    //item Click on Top Rated or Popular movies
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        Movie currentMovie = mAdapter.getItem(position);
        intent.putExtra(getString(R.string.intent_movie_tag), currentMovie);
        startActivity(intent);
    }

    // item click on Favorite movies. Make new movie object from cursor and  pass to the detail activity
    @Override
    public void onItemClickCursor(String title, String posterUrl, String plot,
                                  String avarage, String id, String releaseDate, int sqlId) {
        Movie favoriteMovie = new Movie(true, title, releaseDate, posterUrl, avarage, plot, id, sqlId);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getString(R.string.intent_movie_tag), favoriteMovie);
        startActivity(intent);
    }

    //favorite movie cursor loader
    private final LoaderManager.LoaderCallbacks<Cursor> cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] projection = {
                    MoviesEntry._ID,
                    MoviesEntry.COLUMN_MOVIE_ID,
                    MoviesEntry.COLUMN_TITEL,
                    MoviesEntry.COLUMN_POSTER_URL,
                    MoviesEntry.COLUMN_RELEASE_DATE,
                    MoviesEntry.COLUMN_VOTE_AVERAGE,
                    MoviesEntry.COLUMN_PLOT};

            String sortOrder = MoviesEntry.COLUMN_MOVIE_ID + " ASC";
            return new CursorLoader(MainActivity.this,
                    MoviesContract.MOVIES_URI,
                    projection,
                    null, null, sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Log.e("Main cursor", String.valueOf(data.getCount()));
            if (data.getCount() == 0) {
                recyclerView.setVisibility(View.INVISIBLE);
                emptyViewCursor.setVisibility(View.VISIBLE);
            }
            favoriteMovieAdapter.addAll(data);
            favoriteMovieAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(favoriteMovieAdapter);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };

    //loader to load the top rated and popular movies
    private final LoaderManager.LoaderCallbacks<List<Movie>> httpLoader = new LoaderManager.LoaderCallbacks<List<Movie>>() {

        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            // Utils.makeSearchUrl makes the url from the saved preference
            return new MovieListLoader(MainActivity.this, Utils.makeSearchUrl(MainActivity.this));
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            mAdapter.addAll(data);
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);

        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {

        }


    };
}
