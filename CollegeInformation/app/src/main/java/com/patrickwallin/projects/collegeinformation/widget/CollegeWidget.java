package com.patrickwallin.projects.collegeinformation.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.patrickwallin.projects.collegeinformation.R;

/**
 * Implementation of App Widget functionality.
 */
public class CollegeWidget extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction() == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widget_list_item);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.college_widget);

            Intent intent = new Intent(context,CollegeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);

            views.setRemoteAdapter(R.id.college_list_view,intent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.college_list_view);
        }
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /*
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list, new Intent(context, CollegeWidgetService.class));
    }

    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list, new Intent(context, CollegeWidgetService.class));
    }
    */
}

