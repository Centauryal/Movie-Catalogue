package com.centaury.mcatalogue.ui.favorite.fragment;

import android.database.Cursor;

/**
 * Created by Centaury on 8/23/2019.
 */
public interface LoadTVShowCallback {
    void preExecuteTVShow();

    void postExecuteTVShow(Cursor tvshows);
}
