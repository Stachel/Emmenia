package ru.rinastachel.emmenia.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import ru.rinastachel.emmenia.R;
import ru.rinastachel.emmenia.fragments.MainFragment;

public class Dialogs {

    public static Dialog dialogAddEntity (Activity activity, final MainFragment.OnAddDialogListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    builder.setTitle(R.string.add_dialog_title);

	    LayoutInflater inflater = activity.getLayoutInflater();
	    final View layout = inflater.inflate(R.layout.dialog_add, null);
	    
	    builder.setView(layout);
	    
	    builder.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	DatePicker datePicker = (DatePicker)layout.findViewById(R.id.dialog_add_date);
            	EditText editText = (EditText)layout.findViewById(R.id.dialog_add_comment);
            	listener.addEntity(getCalendarFrom(datePicker), getStringFrom(editText));
            }
        });

	    builder.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   dialog.cancel();
	               }
	           });      
	    return builder.create();
	}

	protected static String getStringFrom(EditText editText) {
		return editText.getText().toString();
	}

	protected static Calendar getCalendarFrom(DatePicker datePicker) {
		int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
	}
	

}
