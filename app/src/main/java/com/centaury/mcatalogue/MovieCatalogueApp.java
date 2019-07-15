package com.centaury.mcatalogue;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by Centaury on 7/15/2019.
 */
public class MovieCatalogueApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidNetworking.initialize(getApplicationContext());
    }
}
