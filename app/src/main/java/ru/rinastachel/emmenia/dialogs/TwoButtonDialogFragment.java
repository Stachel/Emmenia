package ru.rinastachel.emmenia.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import ru.rinastachel.emmenia.R;

public class TwoButtonDialogFragment extends DialogFragment {

    private String _title = null;
    private String _message = null;

	private String _positiveButton;
	private String _negativeButton;
	
	public static TwoButtonDialogFragment newInstance(Bundle args) {
		TwoButtonDialogFragment frag = new TwoButtonDialogFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        parseArguments(args);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(_title);
        dialog.setMessage(_message);

        createButtons(dialog);
        return dialog.create();
    }
	
	protected void parseArguments(Bundle args) {

        if (args.containsKey(Dialogs.TITLE_RESID)) {
            _title = getResources().getString(args.getInt(Dialogs.TITLE_RESID));
        }

        if (args.containsKey(Dialogs.MESSAGE_RESID)) {
            _message = getResources().getString(args.getInt(Dialogs.MESSAGE_RESID));
        }

        if (args.containsKey(Dialogs.POSITIVE_BUTTON_RESID)) {
            _positiveButton = getResources().getString(args.getInt(Dialogs.POSITIVE_BUTTON_RESID));
        } else {
            _positiveButton = getResources().getString(R.string.btn_yes);
        }

        if (args.containsKey(Dialogs.NEGATIVE_BUTTON_RESID)) {
            _negativeButton = getResources().getString(args.getInt(Dialogs.NEGATIVE_BUTTON_RESID));
        } else {
            _negativeButton = getResources().getString(R.string.btn_no);
        }
	}
	
	protected void createButtons(Builder dialog) {
    	dialog.setPositiveButton(_positiveButton, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_OK);
			}
		});
    	dialog.setNegativeButton(_negativeButton, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_CANCELED);
			}
		});
	}

    protected void sendResult (int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Dialogs.BUNDLE, getArguments());
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
