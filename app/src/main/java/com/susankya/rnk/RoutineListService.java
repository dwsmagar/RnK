package com.susankya.rnk;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class RoutineListService extends RemoteViewsService {
    public RoutineListService() {
    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new NewRoutineListProvider(this.getApplicationContext(), intent));
    }
}
