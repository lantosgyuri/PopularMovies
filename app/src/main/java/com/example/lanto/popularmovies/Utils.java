package com.example.lanto.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;


import static android.content.Context.MODE_PRIVATE;

public class Utils {

    private static final String PREF_NAME = "settingsPref";
    private static final String PREF_KEY = "searchTerm";

    private static final String baseUrl = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "";

    public static void setSearchTerm(String searchTerm, Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(PREF_KEY, searchTerm);
        editor.apply();
    }

    public static String makeSearchUrl(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String searchTerm = sharedPref.getString(PREF_KEY, MainActivity.POPULAR);

        return baseUrl + searchTerm + API_KEY;
    }


}
