package com.centaury.mcatalogue.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Centaury on 8/13/2019.
 */
public class ReminderPreference {

    private static final String PREFS_NAME = "reminder_prefs";

    private static final String DAILY_PREFS = "isDaily";
    private static final String TIME_DAILY = "timedaily";
    private static final String RELEASE_PREFS = "isRelease";
    private static final String TIME_RELEASE = "timerelease";

    private final SharedPreferences sharedPreferences;

    public ReminderPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setDailyReminder(Boolean reminder) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DAILY_PREFS, reminder);

        editor.apply();
    }

    public void setTimeDaily(String time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TIME_DAILY, time);

        editor.apply();
    }

    public void setReleaseReminder(Boolean reminder) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(RELEASE_PREFS, reminder);

        editor.apply();
    }

    public void setTimeRelease(String time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TIME_RELEASE, time);

        editor.apply();
    }

    public boolean getDailyReminder() {
        return sharedPreferences.getBoolean(DAILY_PREFS, false);
    }

    public boolean getReleaseReminder() {
        return sharedPreferences.getBoolean(RELEASE_PREFS, false);
    }

    public String getTimeDaily() {
        return sharedPreferences.getString(TIME_DAILY, "");
    }

    public String getTimeRelease() {
        return sharedPreferences.getString(TIME_RELEASE, "");
    }
}
