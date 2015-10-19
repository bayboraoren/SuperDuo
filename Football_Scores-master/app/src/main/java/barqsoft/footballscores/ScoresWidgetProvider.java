package barqsoft.footballscores;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

import barqsoft.footballscores.service.UpdateWidgetService;

/**
 * Created by bora on 07.10.2015.
 */
public class ScoresWidgetProvider extends AppWidgetProvider {


    public static final String ACTION_AUTO_UPDATE = "AUTO_UPDATE";
    private final String TAG = this.getClass().getName();

    private void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, ScoresWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate()");
        AlarmManager alarmManager;
        Intent intent = new Intent(context, UpdateWidgetService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.SECOND, 10);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 5 * 1000, pendingIntent);

        int layoutId = R.layout.scores_widget;
        RemoteViews remoteViews = updateWidgetListView(context, layoutId);
        appWidgetManager.updateAppWidget(layoutId, remoteViews);

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_AUTO_UPDATE)) {

            /*AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), ScoresWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

            onUpdate(context, appWidgetManager, appWidgetIds);*/
            updateWidget(context);
        }


    }


    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.scores_widget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, UpdateWidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "start alarm");

        // start alarm
        ScoresWidgetAlarm appWidgetAlarm = new ScoresWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "stop alarm");

        // stop alarm
        ScoresWidgetAlarm appWidgetAlarm = new ScoresWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.stopAlarm();
    }



  /*  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int weatherArtResourceId = R.drawable.ic_launcher;
        String description = "Clear";
        double maxTemp = 24;
        String formattedMaxTemperature = "12/23";

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.scores_widget;
            RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setImageViewResource(R.id.widget_icon, weatherArtResourceId);
            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, description);
            }
            views.setTextViewText(R.id.widget_high_temperature, formattedMaxTemperature);

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget_icon, description);
    }*/
}