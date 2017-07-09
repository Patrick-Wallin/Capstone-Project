package com.patrickwallin.projects.collegeinformation.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by piwal on 7/9/2017.
 */

public class CollegeWidgetIntentService extends IntentService {
    public CollegeWidgetIntentService() {
        super("CollegeWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,CollegeWidget.class));
    }
}
