package com.example.lanto.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lanto.popularmovies.Data.Movie;
import com.example.lanto.popularmovies.HttpRequest.MovieListLoader;
import com.example.lanto.popularmovies.RecycleViewAdapters.MainRecycleAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,
        MainRecycleAdapter.OnItemClickListener{

    private static final int LOADER_ID = 1;

    private MainRecycleAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set recycleView
        setRecyclerView();

        /*
        Check movie category.
        Check is there any internet connection.
        If yes load the data.
        If not set empty view visible
         */
        checkHttpAndPreftoLoad();

        //set notification
        Utils.setNotification(this);


    }

    private void setRecyclerView(){
        /*
        emptyView in case if there is no internet connection
        and there is no data to load into recycleView
         */
        emptyView = findViewById(R.id.main_activity_empty_view);

        recyclerView = findViewById(R.id.main_activity_recycle_view);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setHasFixedSize(true);

        mAdapter = new MainRecycleAdapter(this);
        mAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(mAdapter);
    }


    //loader to load the Movies
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieListLoader(this, Utils.makeSearchUrl(this));
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mAdapter.addAll(data);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item = menu.findItem(R.id.main_menu_icon);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.main_menu_icon){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkHttpAndPreftoLoad() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        Movie currentMovie = mAdapter.getItem(position);
        intent.putExtra(getString(R.string.intent_movie_tag), currentMovie);
        startActivity(intent);
    }

}
