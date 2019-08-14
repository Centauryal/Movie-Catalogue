package com.centaury.mcatalogue.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.db.AppDatabase;
import com.centaury.mcatalogue.data.db.entity.MovieEntity;
import com.centaury.mcatalogue.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Centaury on 8/12/2019.
 */
public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<MovieEntity> resultsItems = new ArrayList<>();
    private final Context context;
    private int appWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
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
        AppWidgetTarget widgetTarget = new AppWidgetTarget(context, R.id.iv_widget, remoteViews, appWidgetId);

        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(AppConstants.IMAGE_WIDGET_URL + resultsItems.get(position).getPosterPath())
                .into(widgetTarget);

        pushWidgetUpdate(context, remoteViews);

        Bundle bundle = new Bundle();
        bundle.putInt(FavoriteWidget.EXTRA_ITEM, position);
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
        return resultsItems.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, StackRemoteViewsFactory.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}
