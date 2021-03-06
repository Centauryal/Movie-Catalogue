package com.centaury.mcatalogue.services;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.centaury.mcatalogue.BuildConfig;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.local.prefs.ReminderPreference;
import com.centaury.mcatalogue.data.remote.ApiEndPoint;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResponse;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.ui.main.MainActivity;
import com.centaury.mcatalogue.utils.Helper;
import com.google.gson.Gson;

import org.json.JSONObject;

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
    private ReminderPreference preference = null;

    public ReleaseReminder() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                setReleaseReminder(context, preference.getTimeRelease());
                return;
            }
        }
        getReleaseMovie(context);
    }

    private void showReleaseReminderNotification(Context context) {
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "Release Reminder";
        int REQUEST_RELEASE = 111;
        Intent intent;
        PendingIntent pendingIntent;
        int jmlMovie;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        Uri soundAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        jmlMovie = Math.max(releaseMovieList.size(), 0);

        if (jmlMovie == 0) {
            intent = new Intent(context, MainActivity.class);

            pendingIntent = PendingIntent.getActivity(context, REQUEST_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String message = context.getString(R.string.txt_release_no_movie);
            builder.setSmallIcon(R.drawable.ic_notif_logo)
                    .setContentTitle(context.getString(R.string.txt_release_title_no_movie))
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000})
                    .setSound(soundAlarm)
                    .setAutoCancel(true);
        } else {
            intent = new Intent(context, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, REQUEST_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String message = context.getString(R.string.txt_release_movie) + " " + TextUtils.join(", ", releaseMovieList);
            builder.setSmallIcon(R.drawable.ic_notif_logo)
                    .setContentTitle(releaseMovieList.size() + " " + context.getString(R.string.txt_release_title_movie))
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000})
                    .setSound(soundAlarm)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setSound(soundAlarm, attributes);
            channel.setVibrationPattern(new long[]{1000, 1000});

            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(RELEASE_REMINDER, builder.build());
        }
    }

    public void setReleaseReminder(Context context, String time) {

        cancelReleaseReminder(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        String[] timeArray = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_REMINDER, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, context.getString(R.string.txt_release_toast), Toast.LENGTH_SHORT).show();
    }

    public void cancelReleaseReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_REMINDER, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, context.getString(R.string.txt_release_cancel), Toast.LENGTH_SHORT).show();
    }

    private void getReleaseMovie(Context context) {
        Date date = Calendar.getInstance().getTime();
        String dateRelease = Helper.inputDate().format(date);

        AndroidNetworking.get(ApiEndPoint.ENDPOINT_DISCOVERY_MOVIE)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("primary_release_date.gte", dateRelease)
                .addQueryParameter("primary_release_date.lte", dateRelease)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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
