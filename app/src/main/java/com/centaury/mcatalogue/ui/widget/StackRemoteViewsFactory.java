package com.centaury.mcatalogue.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.db.AppDatabase;
import com.centaury.mcatalogue.data.db.entity.MovieEntity;
import com.centaury.mcatalogue.utils.AppConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Centaury on 8/12/2019.
 */
public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<MovieEntity> resultsItems = new ArrayList<>();
    private final Context context;

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        resultsItems.clear();
        resultsItems.addAll(AppDatabase.getDatabase(context).movieDao().getAllWidgetMovies());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return resultsItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_widget);

        if (resultsItems.size() != 0) {
            URL url;
            Bitmap bitmap = null;
            try {
                url = new URL(AppConstants.IMAGE_WIDGET_URL + resultsItems.get(position).getPosterPath());
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();
                InputStream stream = urlConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(stream);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("getViewAtWidget: ", e.getMessage());
            }
            remoteViews.setImageViewBitmap(R.id.iv_widget, bitmap);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(FavoriteWidget.EXTRA_ITEM, resultsItems.get(position).getId());
        Intent intent = new Intent();
        intent.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.iv_widget, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
