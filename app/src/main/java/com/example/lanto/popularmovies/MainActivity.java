package com.example.lanto.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.lanto.popularmovies.Data.Movie;
import com.example.lanto.popularmovies.HttpRequest.DataLoader;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, MainRecycleAdapter.OnItemClickListener{

    private static final int LOADER_ID = 1;
    public static final String POPULAR = "popular";
    private static final String TOP_RATED= "top_rated";

    private MainRecycleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.title);

        RecyclerView recyclerView = findViewById(R.id.main_activity_recycle_view);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setHasFixedSize(true);

        mAdapter = new MainRecycleAdapter(this);
        mAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(mAdapter);

        checkHttpAndLoad();

    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new DataLoader(this, Utils.makeSearchUrl(this));
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mAdapter.addAll(data);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_menu,menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.spinner_items));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItemPosition() == 0){
                    Utils.setSearchTerm(TOP_RATED, MainActivity.this);
                    restartLoader();
                } else {
                    Utils.setSearchTerm(POPULAR, MainActivity.this);
                    restartLoader();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
    }

    private void checkHttpAndLoad() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().initLoader(LOADER_ID, null, this);
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
