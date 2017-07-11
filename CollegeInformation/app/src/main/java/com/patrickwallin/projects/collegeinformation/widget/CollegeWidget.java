package com.patrickwallin.projects.collegeinformation.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.patrickwallin.projects.collegeinformation.MainActivity;
import com.patrickwallin.projects.collegeinformation.R;

import timber.log.Timber;

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
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.college_widget);

            Intent intent = new Intent(context,CollegeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);

            views.setRemoteAdapter(R.id.college_list_view,intent);

            Intent clickIntentTemplate = new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.college_list_view,clickPendingIntentTemplate);

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

}

