package ru.rinastachel.emmenia.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import ru.rinastachel.emmenia.MainActivity.OnAddDialogListener;
import ru.rinastachel.emmenia.MainActivity.OnRemoveDialogListener;
import ru.rinastachel.emmenia.R;
import ru.rinastachel.emmenia.data.Entity;

public class Dialogs {


    public static final String TITLE_RESID = "key_title";
    public static final String MESSAGE_RESID = "key_message";
    public static final String BUNDLE = "key_bundle";
    public static final String POSITIVE_BUTTON_RESID = "positive_button_resid";
    public static final String NEGATIVE_BUTTON_RESID = "negative_button_resid";


    public static Dialog dialogAddEntity (Activity activity, final OnAddDialogListener listener) {
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

	public static Dialog dialogRemoveEntity (Activity activity, final Entity entity, final OnRemoveDialogListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    builder.setTitle(R.string.remove_dialog_title);
	    builder.setMessage(activity.getString(R.string.remove_dialog_message, entity.getDate().getFullString()));

	    builder.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	listener.removeEntity(entity);
            }
        });
	    
	    builder.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
         	   dialog.cancel();
            }
        });      
	    return builder.create();
	}

	public static Dialog makeConfirmRestoreDialog(Activity activity, OnClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		
	    builder.setTitle(R.string.pref_restore_title);
	    builder.setMessage(R.string.pref_restore_message);

	    builder.setPositiveButton(R.string.btn_yes, positiveListener);
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
