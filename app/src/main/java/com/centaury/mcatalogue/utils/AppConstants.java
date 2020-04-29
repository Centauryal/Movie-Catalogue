package com.centaury.mcatalogue.utils;

/**
 * Created by Centaury on 7/16/2019.
 */
public final class AppConstants {

    public static final String EXTRA_FIRST_RUN = "first_run";

    public static final String DATABASE_NAME = "movie_database";
    public static final String DATABASE_AUTHORITY = "com.centaury.mcatalogue";
    public static final String DATABASE_SCHEME = "content";

    public static final String PREFS_NAME = "reminder_prefs";
    public static final String PREFS_DAILY = "isDaily";
    public static final String PREFS_TIME_DAILY = "timedaily";
    public static final String PREFS_RELEASE = "isRelease";
    public static final String PREFS_TIME_RELEASE = "timerelease";

    public static final int MOVIE = 1;
    public static final int MOVIE_ID = 2;
    public static final int TVSHOW = 3;
    public static final int TVSHOW_ID = 4;

    public static final String OBSERVER_MOVIE = "DataObserver";
    public static final String OBSERVER_TVSHOW = "Observer";

    public static final String EXTRA_TVSHOW = "extra_tvshow";
    public static final String EXTRA_FAV_TVSHOW = "extra_favtvshow";
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_FAV_MOVIE = "extra_favmovie";
    public static final String EXTRA_STATE_TVSHOW = "EXTRA_STATE_TVSHOW";
    public static final String EXTRA_STATE_MOVIE = "EXTRA_STATE_MOVIE";

    public static final String WIDGET_TOAST_ACTION = "com.centaury.mcatalogue.WIDGET_TOAST_ACTION";
    public static final String WIDGET_EXTRA_ITEM = "com.centaury.mcatalogue.WIDGET_EXTRA_ITEM";
    public static final String WIDGET_UPDATE_WIDGET = "Update Widget";
}
