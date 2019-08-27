package com.centaury.mcatalogue.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ui.detail.DetailMovieActivity;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {

    private static final String TOAST_ACTION = "com.centaury.mcatalogue.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.centaury.mcatalogue.EXTRA_ITEM";
    public static final String UPDATE_WIDGET = "Update Widget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteWidget.class);
        toastIntent.setAction(FavoriteWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        toastIntent.setData(Uri.parse(toastIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);

                Intent intentDetail = new Intent(context, DetailMovieActivity.class);
                intentDetail.putExtra(DetailMovieActivity.EXTRA_FAV_MOVIE, viewIndex);
                intentDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentDetail);
            }

            if (intent.getAction().equals(UPDATE_WIDGET)) {
                AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
                ComponentName name = new ComponentName(context, FavoriteWidget.class);
                int[] ints = widgetManager.getAppWidgetIds(name);
                for (int id : ints) {
                    widgetManager.notifyAppWidgetViewDataChanged(id, R.id.stack_view);
                }
            }
        }

        super.onReceive(context, intent);
    }
}

