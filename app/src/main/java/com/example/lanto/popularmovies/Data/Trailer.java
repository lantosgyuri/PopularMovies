package com.example.lanto.popularmovies.Data;

public class Trailer {

    private String mTrailerName;
    private String mTrailerUrl;

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    public Trailer(String trailerName, String trailerKey) {
        mTrailerName = trailerName;
        mTrailerUrl = YOUTUBE_BASE_URL + trailerKey;
    }

    public String getmTrailerName() {
        return mTrailerName;
    }

    public String getmTrailerUrl() {
        return mTrailerUrl;
    }

}
