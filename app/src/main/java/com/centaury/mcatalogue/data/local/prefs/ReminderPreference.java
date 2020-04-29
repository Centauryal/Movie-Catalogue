package com.centaury.mcatalogue.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.centaury.mcatalogue.utils.AppConstants;

/**
 * Created by Centaury on 8/13/2019.
 */
public class ReminderPreference {

    private final SharedPreferences sharedPreferences;

    public ReminderPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getDailyReminder() {
        return sharedPreferences.getBoolean(AppConstants.PREFS_DAILY, false);
    }

    public void setDailyReminder(Boolean reminder) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConstants.PREFS_DAILY, reminder);

        editor.apply();
    }

    public boolean getReleaseReminder() {
        return sharedPreferences.getBoolean(AppConstants.PREFS_RELEASE, false);
    }

    public void setReleaseReminder(Boolean reminder) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConstants.PREFS_RELEASE, reminder);

        editor.apply();
    }

    public String getTimeDaily() {
        return sharedPreferences.getString(AppConstants.PREFS_TIME_DAILY, "");
    }

    public void setTimeDaily(String time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.PREFS_TIME_DAILY, time);

        editor.apply();
    }

    public String getTimeRelease() {
        return sharedPreferences.getString(AppConstants.PREFS_TIME_RELEASE, "");
    }

    public void setTimeRelease(String time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.PREFS_TIME_RELEASE, time);

        editor.apply();
    }
}
