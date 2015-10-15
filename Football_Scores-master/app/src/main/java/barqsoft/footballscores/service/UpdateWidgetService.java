package barqsoft.footballscores.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresWidgetProvider;

/**
 * Created by bora on 15.10.2015.
 */
public class UpdateWidgetService extends Service {

    private final String TAG = this.getClass ().getName ();

    @Override
    public void onStart(Intent intent, int startId)
    {
        Log.d(TAG, "onStart()");

        //Change the text in the widget - initial update will put the current time in it
        int layoutId = R.layout.scores_widget;
        RemoteViews updateViews = new RemoteViews (this.getPackageName(), layoutId);
        updateViews.setTextViewText(R.id.widget_high_temperature, "fooo");

        ComponentName thisWidget = new ComponentName(this, ScoresWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, updateViews);
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        // no need to bind
        return null;
    }

}

