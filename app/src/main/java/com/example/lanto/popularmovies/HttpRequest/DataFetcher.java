package com.example.lanto.popularmovies.HttpRequest;

import android.util.Log;

import com.example.lanto.popularmovies.Data.Movie;
import com.example.lanto.popularmovies.Data.Review;
import com.example.lanto.popularmovies.Data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class DataFetcher {

    private static final String LOG_TAG = DataFetcher.class.getSimpleName();
    // constants for the JSON parsing
    private static final String RESULTS = "results";
    private static final String TITLE ="title";
    private static final String VOTE_AVARAGE ="vote_average";
    private static final String RELEASE_DATE ="release_date";
    private static final String PLOT ="overview";
    private static final String POSTER_URL ="poster_path";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String KEY = "key";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";


    // methods to make the Internet Connection and get different Objects
    public static List<Movie> getMovieList(String Url) {
        URL url;
        String baseJson = "";

        url = createUrl(Url);
        try {
            baseJson = makeHttpConnection(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with closing the inputStream or disconnection");
        }

        return extractJsonToMovieList(baseJson);
    }

    public static List<Trailer> getTrailers (String Url){

        URL url;
        String baseJson = "";

        url = createUrl(Url);
        try {
            baseJson = makeHttpConnection(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with closing the inputStream or disconnection");
        }

        return extractJsonToTrailerList(baseJson);
    }

    public static List<Review> getReviews (String Url){

        URL url;
        String baseJson = "";

        url = createUrl(Url);
        try {
            baseJson = makeHttpConnection(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with closing the inputStream or disconnection");
        }

        return extractJsonToReviewList(baseJson);
    }




    // methods to extract the JSON TEXT to get different Objects
    private static List<Movie> extractJsonToMovieList(String baseJson) {

        List<Movie> movieList = new ArrayList<>();

        try {
            JSONObject baseJsonObject = new JSONObject(baseJson);
            JSONArray results = baseJsonObject.getJSONArray(RESULTS);

            for (int i = 0; i<results.length(); i++){
                JSONObject currentItem = results.getJSONObject(i);

                String title = currentItem.getString(TITLE);
                String voteAvarage = currentItem.getString(VOTE_AVARAGE);
                String releaseDate = currentItem.getString(RELEASE_DATE);
                String plot = currentItem.getString(PLOT);
                String posterUrl = currentItem.getString(POSTER_URL);
                String id = currentItem.getString(ID);

                Movie newMovie = new Movie(title, releaseDate, posterUrl, voteAvarage, plot, id);

                movieList.add(newMovie);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with making the JSon from String");
        }

        return movieList;
    }

    private static List<Trailer> extractJsonToTrailerList(String baseJson){
        List<Trailer> trailerList = new ArrayList<>();

        try{
            JSONObject baseJsonObject = new JSONObject(baseJson);
            JSONArray results = baseJsonObject.getJSONArray(RESULTS);

            for (int i = 0; i< results.length(); i++){
                JSONObject currentItem = results.getJSONObject(i);

                String name = currentItem.getString(NAME);
                String key = currentItem.getString(KEY);

                Trailer trailer = new Trailer(name, key);
                trailerList.add(trailer);
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Problem with making the JSON from String(trailer");
        }
        return trailerList;
    }

    private static List<Review> extractJsonToReviewList(String baseJson){

        List<Review> reviewList = new ArrayList<>();

        try{
            JSONObject baseJsonObject = new JSONObject(baseJson);
            JSONArray results = baseJsonObject.getJSONArray(RESULTS);

            for (int i = 0; i< results.length(); i++){
                JSONObject currentItem = results.getJSONObject(i);

                String author = currentItem.getString(AUTHOR);
                String content = currentItem.getString(CONTENT);

                Review review = new Review(author, content);
                reviewList.add(review);
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Problem with making the JSON from String(review)");
        }
        return reviewList;
    }



    // helper methods to make the base Internet Connection to get the JSON TEXT

    private static String makeHttpConnection(URL url) throws IOException{
        String baseJson = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(1000);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                baseJson = readFromStream(inputStream);
            }

        }catch (IOException e){
            Log.e(LOG_TAG, "problem with making the HTTP CONNECTION");

        }finally{
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }

        return baseJson;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            while ( line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }

        } else Log.e(LOG_TAG, "InputStream is null");

        return output.toString();
    }

    private static URL createUrl(String mUrl) {
        URL url= null;

        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"PRoblem with making the URL from String");
        }

        return url;
    }

}
