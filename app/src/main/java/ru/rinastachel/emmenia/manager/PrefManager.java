package ru.rinastachel.emmenia.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import ru.rinastachel.emmenia.data.Date;

public class PrefManager {

	public static final String PREF_REMOVE_ALL = "pref_remove_all";
	public static final String PREF_SAVE_SDCARD = "pref_save_sdcard";
	public static final String PREF_RESTORE_SDCARD = "pref_restore_sdcard";
	public static final String PREF_ABOUT = "pref_about";

	public static final String PREF_THEME_WIDGET = "pref_theme_widget_";
	
	public static final String PREF_LAST_DATE = "pref_last_date";
	public static final String PREF_NEXT_DATE = "pref_next_date";
	
	public static void setWidgetTheme(Context ctx, int widgetID, int theme) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
	    Editor editor = sp.edit();
	    editor.putInt(PREF_THEME_WIDGET + widgetID, theme);
	    editor.apply();
	}

	public static int getThemeWidget(Context ctx, int widgetID) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		return sp.getInt(PREF_THEME_WIDGET + widgetID, 0);
	}
	
	public static void setDataForWidget(Context ctx, Date last, Date next) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
	    Editor editor = sp.edit();
	    editor.putLong(PREF_LAST_DATE, last.getMillisecs());
	    editor.putLong(PREF_NEXT_DATE, next.getMillisecs());
        editor.apply();
	}
	
	public static Date getWidgetLastDate (Context ctx) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		long millisec = sp.getLong(PREF_LAST_DATE, -1);
		if (millisec > 0) {
			return new Date(millisec);
		}
		return null;
	}
	
	public static Date getWidgetNextDate(Context ctx) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		long millisec = sp.getLong(PREF_NEXT_DATE, -1);
		if (millisec > 0) {
			return new Date(millisec);
		}
		return null;
	}
}
