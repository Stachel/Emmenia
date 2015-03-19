package ru.rinastachel.emmenia.service;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class EmmeniaWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		for (int id : appWidgetIds) {
			EmmeniaWidget.updateWidget(context, id);
		}
	}
}