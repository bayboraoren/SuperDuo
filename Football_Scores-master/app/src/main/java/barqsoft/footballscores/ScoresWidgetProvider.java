package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(TAG, "onUpdate()");
        AlarmManager alarmManager;
        Intent intent = new Intent(context, UpdateWidgetService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        //cal.add(Calendar.SECOND, 10);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10 * 1000, pendingIntent);

        // Change the text in the widget
        int layoutId = R.layout.scores_widget;
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), layoutId);

        // update time text
        updateViews.setTextViewText(R.id.widget_high_temperature, cal.getTime().toString());
        appWidgetManager.updateAppWidget(appWidgetIds, updateViews);
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }


   @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onRecieve()");


        if (intent.getAction().equals(ACTION_AUTO_UPDATE)) {
            Log.d(TAG, "do something here");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), ScoresWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

            onUpdate(context, appWidgetManager, appWidgetIds);
        }

        super.onReceive(context, intent);
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