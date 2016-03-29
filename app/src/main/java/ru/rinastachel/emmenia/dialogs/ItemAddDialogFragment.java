package ru.rinastachel.emmenia.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import ru.rinastachel.emmenia.R;

public class ItemAddDialogFragment extends DialogFragment {

    private DatePicker _datePicker;
    private EditText _editText;


    public static ItemAddDialogFragment newInstance(Bundle args) {
        ItemAddDialogFragment frag = new ItemAddDialogFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_add, null);

        _datePicker = (DatePicker) layout.findViewById(R.id.dialog_add_date);
        _editText = (EditText) layout.findViewById(R.id.dialog_add_comment);

        builder.setView(layout);

        builder.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendResult(Activity.RESULT_OK);
            }
        });

        builder.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendResult(Activity.RESULT_CANCELED);
            }
        });
        return builder.create();
    }

    protected void sendResult (int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();

        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }

        args.putSerializable(Dialogs.KEY_DATE, getCalendar());
        args.putString(Dialogs.KEY_COMMENT, getComment());
        intent.putExtra(Dialogs.KEY_BUNDLE, args);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private String getComment() {
        return _editText.getText().toString();
    }

    private Calendar getCalendar() {
        int day = _datePicker.getDayOfMonth();
        int month = _datePicker.getMonth();
        int year =  _datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }
}
