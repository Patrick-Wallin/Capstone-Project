package com.patrickwallin.projects.collegeinformation.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by piwal on 7/8/2017.
 */

public class CollegeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CollegeWidgetRemoteViewsFactory(this.getApplicationContext());
    }
}
