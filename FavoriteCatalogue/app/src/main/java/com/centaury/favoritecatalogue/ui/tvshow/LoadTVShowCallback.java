package com.centaury.favoritecatalogue.ui.tvshow;

import android.database.Cursor;

/**
 * Created by Centaury on 8/24/2019.
 */
public interface LoadTVShowCallback {
    void preExecuteTVShow();

    void postExecuteTVShow(Cursor tvshow);
}
