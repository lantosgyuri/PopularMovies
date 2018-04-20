package com.example.lanto.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lanto.popularmovies.Data.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private Movie mMovie;
    private TextView titleTextView;
    private TextView plotTextView;
    private TextView voteTextView;
    private TextView dateTextView;
    private ImageView posterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        titleTextView =findViewById(R.id.detail_title);
        plotTextView =findViewById(R.id.detail_plot);
        voteTextView =findViewById(R.id.detail_vote);
        dateTextView =findViewById(R.id.detail_release_datum);
        posterImage = findViewById(R.id.detail_poster);

        Intent intent = getIntent();

        if (intent != null) {
            mMovie = intent.getExtras().getParcelable(getString(R.string.intent_movie_tag));
            titleTextView.setText(mMovie.getmTitle());
            plotTextView.setText(mMovie.getmPlot());
            voteTextView.setText(mMovie.getmVoteAvarage());
            dateTextView.setText(mMovie.getmReleaseDate());
            Picasso.with(this)
                    .load(mMovie.getmPosterUrl())
                    .into(posterImage);
        }
    }
}
