package ru.rinastachel.emmenia;

import ru.rinastachel.emmenia.manager.PrefManager;
import ru.rinastachel.emmenia.service.EmmeniaWidget;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

public class WidgetSettingsActivity extends Activity {
	
	int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
	Intent resultValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	    	widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
	    }
	    
	    if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
	    	finish();
	    }
	    
	    resultValue = new Intent();
	    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
	    
	    setResult(RESULT_CANCELED, resultValue);
	    
	    setContentView(R.layout.activity_widget_config);
	    
	    Button btnOk = (Button)findViewById(R.id.btn_widget_ok);
	    btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int theme = ((RadioGroup) findViewById(R.id.widgetTheme)).getCheckedRadioButtonId();
				
				PrefManager.setWidgetTheme(WidgetSettingsActivity.this, widgetID, theme);
			    setResult(RESULT_OK, resultValue);
			    
			    EmmeniaWidget.updateWidget(WidgetSettingsActivity.this, widgetID);
			    
			    finish();
			}
	    });
	}
}
