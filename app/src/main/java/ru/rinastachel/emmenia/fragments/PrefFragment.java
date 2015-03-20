package ru.rinastachel.emmenia.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import ru.rinastachel.emmenia.R;
import ru.rinastachel.emmenia.data.DataListSaver;
import ru.rinastachel.emmenia.data.Entity;
import ru.rinastachel.emmenia.data.EntitySortedList;
import ru.rinastachel.emmenia.dialogs.AboutDialogFragment;
import ru.rinastachel.emmenia.dialogs.TwoButtonDialogFragment;
import ru.rinastachel.emmenia.exception.BackupDataException;
import ru.rinastachel.emmenia.exception.RestoreDataException;
import ru.rinastachel.emmenia.exception.SaveDataException;
import ru.rinastachel.emmenia.manager.PrefManager;

public class PrefFragment extends PreferenceFragment {

    private static final int CHOOSE_FILE_RESTORE = 1;
    private static final int DIALOG_ABOUT = 100;
    private static final int DIALOG_DELETE_ALL = 101;
    private static final int DIALOG_CONFIRM_RESTORE = 102;

    private DataListSaver _listSaver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Preference prefRemoveAll = findPreference(PrefManager.PREF_REMOVE_ALL);
        prefRemoveAll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                clickRemoveAll();
                return true;
            }
        });

        Preference  prefSaveSDCard = findPreference(PrefManager.PREF_SAVE_SDCARD);
        prefSaveSDCard.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                clickSaveSDCard();
                return true;
            }
        });

        Preference  prefRestoreSDCard = findPreference(PrefManager.PREF_RESTORE_SDCARD);
        prefRestoreSDCard.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                clickRestoreSDCard();
                return true;
            }
        });

        Preference  prefAbout = findPreference(PrefManager.PREF_ABOUT);
        prefAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openAboutDialog();
                return true;
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void openAboutDialog() {
        AboutDialogFragment dialog = AboutDialogFragment.newInstance();
        dialog.setTargetFragment(this, DIALOG_ABOUT);
        dialog.show(getFragmentManager(), "DIALOG_ABOUT");
    }

    protected void clickRestoreSDCard() {

        Bundle args = new Bundle();
        args.putInt(TwoButtonDialogFragment.MESSAGE_RESID, R.string.pref_restore_message);
        TwoButtonDialogFragment dialog = TwoButtonDialogFragment.newInstance(args);
        dialog.setTargetFragment(this, DIALOG_CONFIRM_RESTORE);
        dialog.show(getFragmentManager(), "DIALOG_CONFIRM_RESTORE");
    }

    protected void clickSaveSDCard() {
        try {
            String fileName = getDataListSaver().saveToSDCard();
            Toast.makeText(getActivity(), getString(R.string.toast_save_sdcard, fileName), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(getActivity(), R.string.error_no_file_for_read, Toast.LENGTH_LONG).show();
        }catch (BackupDataException e) {
            Toast.makeText(getActivity(), R.string.toast_save_sdcard_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case CHOOSE_FILE_RESTORE:
                Uri uri = data.getData();
                String path = uri.getPath();
                try {
                    ArrayList<Entity> list = getDataListSaver().restoreFromSDCard(path);
                    EntitySortedList esl = new EntitySortedList (getActivity(), null);
                    esl.setDateList(list);
                    Toast.makeText(getActivity(), R.string.toast_restore_sdcard, Toast.LENGTH_LONG).show();
                } catch (RestoreDataException e) {
                    Toast.makeText(getActivity(), R.string.toast_restore_sdcard_error, Toast.LENGTH_LONG).show();
                } catch (SaveDataException e) {
                    Toast.makeText(getActivity(), R.string.toast_restore_sdcard_error, Toast.LENGTH_LONG).show();
                }
                break;

            case DIALOG_DELETE_ALL:
                if (getDataListSaver().deleteFile()) {
                    Toast.makeText(getActivity(), R.string.toast_remove_all, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), R.string.toast_remove_all_error, Toast.LENGTH_LONG).show();
                }
                break;

            case DIALOG_CONFIRM_RESTORE:
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory() + "/Emmenia/"), "file/*");
                startActivityForResult(fileIntent, CHOOSE_FILE_RESTORE);
                break;
        }
    }

    private void clickRemoveAll() {
        Bundle args = new Bundle();
        args.putInt(TwoButtonDialogFragment.MESSAGE_RESID, R.string.pref_remove_all_message);
        TwoButtonDialogFragment dialog = TwoButtonDialogFragment.newInstance(args);
        dialog.setTargetFragment(this, DIALOG_DELETE_ALL);
        dialog.show(getFragmentManager(), "DIALOG_DELETE_ALL");
    }

    private DataListSaver getDataListSaver () {
        if (_listSaver == null)
            _listSaver = new DataListSaver(getActivity());
        return _listSaver;
    }
}
