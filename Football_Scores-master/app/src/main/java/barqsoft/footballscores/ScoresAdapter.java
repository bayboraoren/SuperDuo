package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter
{
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_STATUS = 10;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
    public ScoresAdapter(Context context, Cursor cursor, int flags)
    {
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor)
    {

        String home=cursor.getString(COL_HOME);
        String away=cursor.getString(COL_AWAY);
        int homeGoals = cursor.getInt(COL_HOME_GOALS);
        int awayGoals = cursor.getInt(COL_AWAY_GOALS);
        String matchTime = cursor.getString(COL_MATCHTIME);
        String status = cursor.getString(COL_STATUS);


        final ViewHolder mHolder = (ViewHolder) view.getTag();
        mHolder.home_name.setText(home);
        mHolder.away_name.setText(away);

        mHolder.date.setText(cursor.getString(COL_MATCHTIME));
        mHolder.score.setText(Utilies.getScores(homeGoals,awayGoals));
        mHolder.match_id = cursor.getDouble(COL_ID);

        mHolder.home_crest.setImageResource(Utilies.getTeamCrestByTeamName(context,
                home));

        mHolder.away_crest.setImageResource(Utilies.getTeamCrestByTeamName(context,
                away));




        //Log.v(FetchScoreTask.LOG_TAG,mHolder.home_name.getText() + " Vs. " + mHolder.away_name.getText() +" id " + String.valueOf(mHolder.match_id));
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(detail_match_id));
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);

        //Content Description
        String contentDescription="";
        String statusDescription ="";
        String matchDescription ="";

        if(status.equals(DatabaseContract.scores_table.STATUS_FINISHED)){

            statusDescription = "played";

            if(homeGoals>awayGoals){
                matchDescription=context.getString(R.string.won);
            }else if(homeGoals<awayGoals){
                matchDescription=context.getString(R.string.lost);
            }else{
                matchDescription=context.getString(R.string.draws);
            }

            contentDescription = context.getString(R.string.score_accessibility_finished,home,away,statusDescription,matchTime,home,matchDescription,home,homeGoals,away,awayGoals);

        }else{
            statusDescription = context.getString(R.string.will_play);
            contentDescription = context.getString(R.string.score_accessibility_timed,home,away,statusDescription,matchTime);
        }

        view.setContentDescription(contentDescription);



        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if(mHolder.match_id == detail_match_id)
        {
            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");

            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Utilies.getMatchDay(context, cursor.getInt(COL_MATCHDAY),
                    cursor.getInt(COL_LEAGUE)));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utilies.getLeague(context,cursor.getInt(COL_LEAGUE)));
            Button share_button = (Button) v.findViewById(R.id.share_button);


            String shareContentDescription = context.getString(R.string.share_text) + " " + context.getString(R.string.widget_text);
            share_button.setContentDescription(shareContentDescription + " " + home + " " + homeGoals + " " + away + " " + awayGoals);

            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(mHolder.home_name.getText() + " "
                            + mHolder.score.getText() + " " + mHolder.away_name.getText() + " "));
                }
            });


        }
        else
        {
            container.removeAllViews();
        }


    }
    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

}
