package ru.rinastachel.emmenia.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ru.rinastachel.emmenia.PrefActivity;
import ru.rinastachel.emmenia.R;
import ru.rinastachel.emmenia.adapter.MainListAdapter;
import ru.rinastachel.emmenia.data.Date;
import ru.rinastachel.emmenia.data.Entity;
import ru.rinastachel.emmenia.data.EntitySortedList;
import ru.rinastachel.emmenia.dialogs.Dialogs;
import ru.rinastachel.emmenia.dialogs.TwoButtonDialogFragment;
import ru.rinastachel.emmenia.exception.EntityExistException;
import ru.rinastachel.emmenia.exception.EntityFromFutureException;
import ru.rinastachel.emmenia.exception.NoSavedDataException;
import ru.rinastachel.emmenia.exception.ReadDataException;
import ru.rinastachel.emmenia.exception.SaveDataException;

public class MainFragment extends Fragment implements EntitySortedList.OnEntityListListener {

    public static MainFragment getInstance() {
        return new MainFragment();
    }

    private static final int DIALOG_REMOVE_ENTITY = 100;
    private static final int DIALOG_ADD_ENTITY = 101;

    public static final String KEY_POSITION = "key_position";

    private ListView _listView;
    private TextView _noData;
    private MainListAdapter _adapter;
    private EntitySortedList _entitySortedList;
    private TextView _nextDate;
    private TextView _nextDay;
    private TextView _nextComment;

    @Override
    public void onEntityListChanged() {
        if (!_entitySortedList.isEmpty()) {
            showListView();
        } else {
            showAdditionalView();
        }
    }

    public interface OnAddDialogListener {
        public void addEntity (Calendar calendar, String string);
    }

    OnAddDialogListener _addDialogListener = new OnAddDialogListener(){
        @Override
        public void addEntity(Calendar date, String comment) {
            addNewEntity(date, comment);
        }
    };

    private void showRemoveEntityConfirm(int position) {
        Entity entity = _adapter.getEntity(position);
        if (entity != null) {
            Bundle args = new Bundle();
            args.putString(TwoButtonDialogFragment.MESSAGE, getString(R.string.remove_dialog_message, entity.getDate().getFullString()));
            args.putInt(KEY_POSITION, position);
            TwoButtonDialogFragment dialog = TwoButtonDialogFragment.newInstance(args);
            dialog.setTargetFragment(this, DIALOG_REMOVE_ENTITY);
            dialog.show(getFragmentManager(), "DIALOG_REMOVE_ENTITY");
        }
    }

    private boolean addNewEntity(Calendar date, String comment) {
        try {
            _entitySortedList.add(date, comment);
        } catch (SaveDataException e) {
            Toast.makeText(getActivity(), getString(R.string.error_save_data), Toast.LENGTH_LONG).show();
            return false;
        } catch (EntityFromFutureException e) {
            Toast.makeText(getActivity(), getString(R.string.error_add_later), Toast.LENGTH_LONG).show();
            return false;
        } catch (EntityExistException e) {
            Date d = new Date(date);
            Toast.makeText(getActivity(), getString(R.string.error_add_exist, d.getFullString()), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void showListView() {

        _noData.setVisibility(View.INVISIBLE);
        _listView.setVisibility(View.VISIBLE);

        _adapter.notifyDataChanged(_entitySortedList);

        Date lastDate = _entitySortedList.getNextCalendar();
        int cycle = _entitySortedList.getCycle();
        int day = _entitySortedList.getCurrentDay();

        _nextDate.setText(lastDate.getSimpleString());
        _nextDay.setText(getString(R.string.day, day));
        _nextComment.setText(getString(R.string.day_cycle, cycle));
    }

    private void showAdditionalView() {
        _noData.setVisibility(View.VISIBLE);
        _listView.setVisibility(View.INVISIBLE);

        _nextDate.setText(R.string.no_date);
        _nextDay.setText(getString(R.string.day, 0));
        _nextComment.setText(getString(R.string.day_cycle, 0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // init adapter
        _adapter = new MainListAdapter(getActivity());

        // imageview
        ImageView imageView = (ImageView)view.findViewById(R.id.next_image);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (addNewEntity (Calendar.getInstance(), null))
                    Toast.makeText(getActivity(), getString(R.string.add_current_date), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // listview
        _listView = (ListView)view.findViewById(R.id.main_list);
        _listView.setAdapter(_adapter);
        _listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                showRemoveEntityConfirm(position);
                return false;
            }
        });

        // no data
        _noData = (TextView)view.findViewById(R.id.no_data);

        // next data
        _nextDate = (TextView)view.findViewById(R.id.next_date);
        _nextDay = (TextView)view.findViewById(R.id.next_days);
        _nextComment = (TextView)view.findViewById(R.id.next_comment);

        // init data array
        _entitySortedList = new EntitySortedList (getActivity(), this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onResume () {
        super.onResume();
        if (_entitySortedList == null)
            _entitySortedList = new EntitySortedList (getActivity(), this);

        try {
            _entitySortedList.readSavedData();
        } catch (NoSavedDataException e) {

        } catch (ReadDataException e) {
            Toast.makeText(getActivity(), getString(R.string.error_read_data), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Dialog addDialog = Dialogs.dialogAddEntity(getActivity(), _addDialogListener);
                addDialog.show();
                return true;

            case R.id.action_settings:
                Intent settingsActivity = new Intent(getActivity(), PrefActivity.class);
                startActivity(settingsActivity);
                return true;
        }
        return false;
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case DIALOG_REMOVE_ENTITY:
                Bundle args = data.getExtras().getBundle(TwoButtonDialogFragment.BUNDLE);
                if (args.containsKey(KEY_POSITION)) {
                    Entity entity = _adapter.getEntity(args.getInt(KEY_POSITION));
                    if (entity != null) {
                        try {
                            _entitySortedList.remove(entity);
                        } catch (SaveDataException e) {
                            Toast.makeText(getActivity(), getString(R.string.error_save_data), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }
    }
}
