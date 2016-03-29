package ru.rinastachel.emmenia.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import ru.rinastachel.emmenia.R;
import ru.rinastachel.emmenia.fragments.MainFragment;

public class Dialogs {

	public static final String KEY_BUNDLE = "key_bundle";
	public static final String KEY_TITLE = "key_title";
	public static final String KEY_DATE = "key_date";
	public static final String KEY_COMMENT = "key_comment";

	public static void addEntity(Fragment fragment, int requestCode, Bundle args) {
		ItemAddDialogFragment dialog = ItemAddDialogFragment.newInstance(args);
		dialog.setTargetFragment(fragment, requestCode);
		dialog.show(fragment.getFragmentManager(), "DIALOG_ADD_ENTITY");
	}

	public static void removeEntity(Fragment fragment, int requestCode, Bundle args) {
		TwoButtonDialogFragment dialog = TwoButtonDialogFragment.newInstance(args);
		dialog.setTargetFragment(fragment, requestCode);
		dialog.show(fragment.getFragmentManager(), "DIALOG_REMOVE_ENTITY");
	}

	public static void updateEntity(Fragment fragment, int requestCode, Bundle args) {
		ItemEditDialogFragment dialog = ItemEditDialogFragment.newInstance(args);
		dialog.setTargetFragment(fragment, requestCode);
		dialog.show(fragment.getFragmentManager(), "DIALOG_EDIT_ENTITY");
	}
}
