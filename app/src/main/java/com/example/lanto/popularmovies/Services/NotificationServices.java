package com.example.lanto.popularmovies.Services;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.lanto.popularmovies.Utils;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationServices extends com.firebase.jobdispatcher.JobService {

    private AsyncTask <Void, Void, Void> notificationTask;

    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters job) {
            notificationTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Utils.createNotificationChannel(getApplicationContext());
                    Utils.createNotification(getApplicationContext());
                    jobFinished(job, false);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    jobFinished(job, false);
                }
            };
            notificationTask.execute();
            return true;
        }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if(notificationTask != null) notificationTask.cancel(true);
        return true;
    }
}
