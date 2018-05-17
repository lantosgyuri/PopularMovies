package com.example.lanto.popularmovies;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;


import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    //networking
    private static final String baseUrl = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PLACE ="?api_key=";
    private static final String API_KEY = "c1a1b7ead07ec4f90469511a62359911";
    private static final String VIDEOS = "/videos";
    private static final String REVIEWS = "/reviews";
    public static final String POPULAR = "popular";

    //notification
    private static final String CHANNEL_1_ID = "Movies Channel";
    private static final String NOTIFICATION_TAG ="notif-tag";
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;


    //get saved movie category
    public static String getPrefCategory(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String prefCategory = sharedPref.getString(context.getString(R.string.pref_list_key), POPULAR);
        return prefCategory;
    }

    //make URLS
    public static String makeSearchUrl(Context context) {
        Log.e("httpString:", baseUrl + getPrefCategory(context));
        return baseUrl + getPrefCategory(context) + API_KEY_PLACE + API_KEY;
    }

    public static String makeTrailerUrl(String id){
        return baseUrl + id + VIDEOS + API_KEY_PLACE + API_KEY;
    }

    public static String makeReviewUrl(String id){
        return baseUrl + id + REVIEWS + API_KEY_PLACE + API_KEY;
    }

    //notification methods
    public static void createNotificationChannel (Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    Resources.getSystem().getString(R.string.notification_chanel_desc),
            NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is Channel 1");

            NotificationManager manager =  context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
    
    public static void createNotification(Context context){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_favorite_border)
                .setContentTitle(context.getString(R.string.notification_titel))
                .setContentText(context.getString(R.string.notification_content))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .build();

        manager.notify(1, notification);
    }

    public static void setNotification(Context context){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job notificationJob = dispatcher.newJobBuilder()
                .setService(com.example.lanto.popularmovies.Services.NotificationServices.class)
                .setTag(NOTIFICATION_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(notificationJob);
    }




}
