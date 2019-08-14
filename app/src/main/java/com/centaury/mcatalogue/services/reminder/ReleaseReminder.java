package com.centaury.mcatalogue.services.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.movie.MovieResponse;
import com.centaury.mcatalogue.data.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.ui.main.MainActivity;
import com.centaury.mcatalogue.utils.AppConstants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Centaury on 8/13/2019.
 */
public class ReleaseReminder extends BroadcastReceiver {

    private final int RELEASE_REMINDER = 101;

    private List<MovieResultsItem> movieResultsItems = new ArrayList<>();
    private List<String> releaseMovieList = new ArrayList<>();

    public ReleaseReminder() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        getReleaseMovie(context);
    }

    private void showReleaseReminderNotification(Context context) {
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "Release Reminder";
        int REQUEST_RELEASE = 111;
        Intent intent;
        PendingIntent pendingIntent;
        int jmlmovie;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        Uri soundAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (releaseMovieList.size() > 0) {
            jmlmovie = releaseMovieList.size();
        } else {
            jmlmovie = 0;
        }


        if (jmlmovie == 0) {
            intent = new Intent(context, MainActivity.class);
            pendingIntent = TaskStackBuilder.create(context).addNextIntent(intent)
                    .getPendingIntent(REQUEST_RELEASE, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setSmallIcon(R.drawable.ic_notif_logo)
                    .setContentTitle(context.getString(R.string.txt_release_reminder))
                    .setContentText(context.getString(R.string.txt_nomovie_release))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(soundAlarm)
                    .setAutoCancel(true);
        } else {
            intent = new Intent(context, MainActivity.class);
            pendingIntent = TaskStackBuilder.create(context).addNextIntent(intent)
                    .getPendingIntent(REQUEST_RELEASE, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setSmallIcon(R.drawable.ic_notif_logo)
                    .setContentTitle(context.getString(R.string.txt_release_reminder))
                    .setContentText(context.getString(R.string.txt_movie_release) + " " + TextUtils.join(", ", releaseMovieList))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(soundAlarm)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(RELEASE_REMINDER, builder.build());
        }
    }

    public void setReleaseReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, context.getString(R.string.txt_toast_release), Toast.LENGTH_SHORT).show();
    }

    public void cancelReleaseReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_REMINDER, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, context.getString(R.string.txt_cancel_release), Toast.LENGTH_SHORT).show();
    }

    private void getReleaseMovie(Context context) {
        DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = Calendar.getInstance().getTime();
        String dateRelease = inputDate.format(date);

        AndroidNetworking.get(AppConstants.BASE_URL + "discover/movie")
                .addQueryParameter("api_key", AppConstants.API_KEY)
                .addQueryParameter("primary_release_date.gte", dateRelease)
                .addQueryParameter("primary_release_date.lte", dateRelease)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponseRelease: ", response + "");
                        MovieResponse movieResponse = new Gson().fromJson(response + "", MovieResponse.class);
                        movieResultsItems = movieResponse.getResults();
                        for (MovieResultsItem resultsItem : movieResultsItems) {
                            String movieDate = resultsItem.getReleaseDate();
                            if (movieDate.equals(dateRelease)) {
                                releaseMovieList.add(resultsItem.getTitle());
                            }
                        }
                        showReleaseReminderNotification(context);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("onError: ", anError.getErrorBody());
                    }
                });
    }
}
