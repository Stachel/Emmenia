package ru.rinastachel.emmenia.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;

import ru.rinastachel.emmenia.R;

public class ItemEditDialogFragment extends DialogFragment {

    private String _title = null;
    private String _comment = null;
    private EditText _editText;

    public static ItemEditDialogFragment newInstance(Bundle args) {
        ItemEditDialogFragment frag = new ItemEditDialogFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        parseArguments(getArguments());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(_title);

        _editText = new EditText (getActivity());
        _editText.setText(_comment);
        _editText.setHint(R.string.comment);
        _editText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

        builder.setView(_editText);

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

    private void parseArguments(Bundle args) {
        if (args.containsKey(Dialogs.KEY_TITLE)) {
            _title = args.getString(Dialogs.KEY_TITLE);
        } else {
            // this is impossible, but...
            _title = getString(R.string.edit_dialog_title);
        }

        if (args.containsKey(Dialogs.KEY_COMMENT)) {
            _comment = args.getString(Dialogs.KEY_COMMENT);
        }
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
        args.putString(Dialogs.KEY_COMMENT, getComment());
        intent.putExtra(Dialogs.KEY_BUNDLE, args);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private String getComment() {
        return _editText.getText().toString();
    }
}