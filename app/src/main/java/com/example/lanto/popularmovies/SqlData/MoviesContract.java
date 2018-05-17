package com.example.lanto.popularmovies.SqlData;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    public MoviesContract () {}

    //make the BASE uri
    public static final String CONTENT_AUTHORITY = "com.example.lanto.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    //access the movies Uri
    public static final Uri MOVIES_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);


    //Movies Table
    public static final class MoviesEntry implements BaseColumns{

        public static final String TABLE_NAME = "movies";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITEL = "titel";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_POSTER_URL = "posterUrl";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_PLOT = "plot";


    }

}
