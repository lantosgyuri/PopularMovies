package com.example.lanto.popularmovies.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{

    private final String mTitle;
    private final String mReleaseDate;
    private final String mPosterUrl;
    private final String mVoteAvarage;
    private final String mPlot;
    private final String mId;
    private final boolean mIsFavorite;
    private int mSqlId;

    //base constructor
    public Movie(String title, String releaseDate, String posterUrl, String voteAvarage, String plot, String Id) {
        mTitle = title;
        mReleaseDate = releaseDate;
        mPosterUrl =  posterUrlMaker(posterUrl);
        mVoteAvarage = voteAvarage;
        mPlot = plot;
        mId = Id;
        mIsFavorite = false;

    }

    //second constructor for the SQLite
    public Movie(boolean isFavorite, String title, String releaseDate, String posterUrl,
                 String voteAverage, String plot, String id, int sqlId){
        mIsFavorite = isFavorite;
        mTitle = title;
        mReleaseDate = releaseDate;
        mPosterUrl = posterUrl;
        mVoteAvarage = voteAverage;
        mPlot = plot;
        mId = id;
        mSqlId = sqlId;
    }

    private Movie(Parcel in) {
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mPosterUrl = in.readString();
        mVoteAvarage = in.readString();
        mPlot = in.readString();
        mId = in.readString();
        mSqlId = in.readInt();
        mIsFavorite = in.readByte() != 0;     //myIsFavorite == true if byte != 0
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getmPosterUrl() {
        return mPosterUrl;
    }

    private String posterUrlMaker(String url){
        final String baseUrl = "http://image.tmdb.org/t/p/";
        final String imageSize = "w185";

        return baseUrl + imageSize + url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mPosterUrl);
        dest.writeString(mVoteAvarage);
        dest.writeString(mPlot);
        dest.writeString(mId);
        dest.writeInt(mSqlId);
        dest.writeByte((byte) (mIsFavorite ? 1 : 0));     //if mIsFavorite == true, byte == 1
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmVoteAvarage() {
        return mVoteAvarage;
    }

    public String getmPlot() {
        return mPlot;
    }

    public String getmId(){return mId;}

    public boolean getIsFavorite() { return mIsFavorite;}

    public int getmSqlId(){ return mSqlId;}
}
