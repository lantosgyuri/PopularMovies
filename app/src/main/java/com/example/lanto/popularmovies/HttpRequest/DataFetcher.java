package com.example.lanto.popularmovies.HttpRequest;

import android.util.Log;

import com.example.lanto.popularmovies.Data.Movie;

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

public class DataFetcher {

    private static final String LOG_TAG = DataFetcher.class.getSimpleName();
    private static final String RESULTS = "results";
    private static final String TITLE ="title";
    private static final String VOTE_AVARAGE ="vote_average";
    private static final String RELEASE_DATE ="release_date";
    private static final String PLOT ="overview";
    private static final String POSTER_URL ="poster_path";

    public static List<Movie> fetchData(String mUrl) {
        URL url;
        String baseJson = "";

        url = createUrl(mUrl);
        try {
            baseJson = makeHttpConnection(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with closing the inputStream or disconnection");
        }

        return extractJsonToList(baseJson);
    }

    private static List<Movie> extractJsonToList(String baseJson) {

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

                Movie newMovie = new Movie(title, releaseDate, posterUrl, voteAvarage, plot);

                movieList.add(newMovie);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with making the JSon from String");
        }

        return movieList;
    }


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
