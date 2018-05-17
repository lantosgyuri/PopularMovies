package com.example.lanto.popularmovies.SqlData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lanto.popularmovies.SqlData.MoviesContract.MoviesEntry;

class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE "
                + MoviesEntry.TABLE_NAME + " ("
                + MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviesEntry.COLUMN_TITEL + " TEXT NOT NULL, "
                + MoviesEntry.COLUMN_POSTER_URL + " TEXT, "
                + MoviesEntry.COLUMN_PLOT + " TEXT NOT NULL, "
                + MoviesEntry.COLUMN_RELEASE_DATE + " TEXT, "
                + MoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT);";
        Log.e("SQLITESTRING:", SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
