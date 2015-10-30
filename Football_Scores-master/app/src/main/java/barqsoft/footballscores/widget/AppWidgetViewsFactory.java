package barqsoft.footballscores.widget;

/**
 * Created by bora on 19.10.2015.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

public class AppWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor cursor;

    private Context context = null;
    //private int appWidgetId;
    private ArrayList<String> arrayList = new ArrayList<String>();

    public AppWidgetViewsFactory(Context ctxt, Intent intent) {
        this.context = ctxt;
        /*appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
		Log.e(getClass().getSimpleName(), appWidgetId + "");*/
    }

    @Override
    public void onCreate() {
        arrayList.add("1");
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return (cursor.getCount());
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.row);

        cursor.moveToPosition(position);
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.DATE_COL));
        String home = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.HOME_COL));
        String away = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.AWAY_COL));
        String home_goals = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.HOME_GOALS_COL));

        if (home_goals.equals("-1")) home_goals = "";
        String away_goals = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.AWAY_GOALS_COL));
        if (away_goals.equals("-1")) away_goals = "";

        double match_id = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.MATCH_ID));
        int home_crest = Utilies.getTeamCrestByTeamName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.HOME_COL)));
        int away_crest = Utilies.getTeamCrestByTeamName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.scores_table.HOME_COL)));

        row.setImageViewResource(R.id.home_crest,home_crest);
        row.setImageViewResource(R.id.away_crest, away_crest);

        row.setTextViewText(R.id.date_textview, date);
        row.setTextViewText(R.id.home_name, home);
        row.setTextViewText(R.id.away_name, away);
        row.setTextViewText(R.id.score_textview, home_goals + " : " + away_goals);


        //Intent fillInIntent = new Intent();
        //fillInIntent.putExtra(WidgetProvider.EXTRA_LIST_VIEW_ROW_NUMBER, position);
        //row.setOnClickFillInIntent(R.id.widget_container, fillInIntent);


        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        Uri dateUri = DatabaseContract.scores_table.buildScoreWithDate();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        cursor = context.getContentResolver().query(dateUri, DatabaseContract.scores_table.SCORES_TABLE_COLUMNS, "date", new String[]{today}, null);
    }
}