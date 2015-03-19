package ru.rinastachel.emmenia.service;

import ru.rinastachel.emmenia.MainActivity;
import ru.rinastachel.emmenia.R;
import ru.rinastachel.emmenia.data.Date;
import ru.rinastachel.emmenia.manager.PrefManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

public class EmmeniaWidget {
	
	public static Context _context;
	public static int _widgetID;	
	
	public static void updateWidget (Context context, int widgetID) {
		Log.e("MY", "updateWidget: " + widgetID);
		_context = context;
		_widgetID = widgetID;
		
		RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		setTheme(widgetView);
		
		setData(widgetView);
		
		setActions(widgetView);
		
		// update widget
		final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(widgetID, widgetView);
	}
	
	private static void setTheme (RemoteViews widgetView) {
		int theme = PrefManager.getThemeWidget(_context, _widgetID);
		switch (theme) {
			case R.id.themeBlack:
				widgetView.setInt(R.id.background, "setBackgroundResource", R.drawable.widget_bg);
				widgetView.setTextColor(R.id.widget_day, Color.WHITE);
				widgetView.setTextColor(R.id.widget_date, Color.WHITE);
				break;
			
			default:
				widgetView.setInt(R.id.background, "setBackgroundResource", R.drawable.widget_bg_white);
				widgetView.setTextColor(R.id.widget_day, Color.BLACK);
				widgetView.setTextColor(R.id.widget_date, Color.BLACK);
				break;
		}
	}
	
	private static void setData (RemoteViews widgetView) {
		Date last = PrefManager.getWidgetLastDate(_context); 
		Date next = PrefManager.getWidgetNextDate(_context); 
		
		String dayString = last != null ? String.valueOf(last.calcDiff(new Date()) + 1) : _context.getResources().getString(R.string.dash);
		String dateString = next != null ? next.getFullString() : _context.getResources().getString(R.string.no_data);

		// show data
		widgetView.setTextViewText(R.id.widget_day, dayString);
		widgetView.setTextViewText(R.id.widget_date, dateString);
	}
	
	private static void setActions (RemoteViews widgetView) {
        Intent intent = new Intent(_context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(_context, 0, intent, 0);
        widgetView.setOnClickPendingIntent(R.id.background, pendingIntent);
	}
	
	public static void updateAllWidgets(Context ctx) {
		AppWidgetManager man = AppWidgetManager.getInstance(ctx);
	    int[] ids = man.getAppWidgetIds(new ComponentName(ctx, EmmeniaWidgetProvider.class));
	    
	    for (int id : ids) {
	    	updateWidget(ctx, id);
	    }
	}
}
