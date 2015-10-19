package barqsoft.footballscores.widget;

/**
 * Created by bora on 19.10.2015.
 */
import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new AppWidgetViewsFactory(this.getApplicationContext(), intent));
    }
}