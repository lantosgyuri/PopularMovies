package com.example.lanto.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import static android.content.Context.MODE_PRIVATE;

public class Utils {

    private static final String PREF_NAME = "settingsPref";
    private static final String PREF_KEY = "searchTerm";

    private static final String baseUrl = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PLACE ="?api_key=";
    private static final String API_KEY = "c1a1b7ead07ec4f90469511a62359911";
    private static final String VIDEOS = "/videos";
    private static final String REVIEWS = "/reviews";

    public static void setSearchTerm(String searchTerm, Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(PREF_KEY, searchTerm);
        editor.apply();
    }

    public static String makeSearchUrl(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String searchTerm = sharedPref.getString(PREF_KEY, MainActivity.POPULAR);

        return baseUrl + searchTerm + API_KEY_PLACE + API_KEY;
    }

    public static String makeTrailerUrl(String id){
        return baseUrl + id + VIDEOS + API_KEY_PLACE + API_KEY;
    }

    public static String makeReviewUrl(String id){
        return baseUrl + id + REVIEWS + API_KEY_PLACE + API_KEY;
    }

}
