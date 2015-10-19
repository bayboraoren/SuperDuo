package barqsoft.footballscores.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresWidgetProvider;
import barqsoft.footballscores.widget.ListProvider;

/**
 * Created by bora on 15.10.2015.
 */
public class UpdateWidgetService extends RemoteViewsService {
	/*
	 * So pretty simple just defining the Adapter of the listview
	 * here Adapter is ListProvider
	 * */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new ListProvider(this.getApplicationContext(), intent));
    }

}