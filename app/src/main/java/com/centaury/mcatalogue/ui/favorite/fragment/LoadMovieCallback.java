package com.centaury.mcatalogue.ui.favorite.fragment;

import android.database.Cursor;

/**
 * Created by Centaury on 8/22/2019.
 */
public interface LoadMovieCallback {
    void preExecute();

    void postExecute(Cursor movies);
}
