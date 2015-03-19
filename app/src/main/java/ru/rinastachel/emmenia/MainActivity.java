package ru.rinastachel.emmenia;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Calendar;

import ru.rinastachel.emmenia.adapter.MainListAdapter;
import ru.rinastachel.emmenia.data.Date;
import ru.rinastachel.emmenia.data.Entity;
import ru.rinastachel.emmenia.data.EntitySortedList;
import ru.rinastachel.emmenia.data.EntitySortedList.OnEntityListListener;
import ru.rinastachel.emmenia.exception.EntityExistException;
import ru.rinastachel.emmenia.exception.EntityFromFutureException;
import ru.rinastachel.emmenia.exception.NoSavedDataException;
import ru.rinastachel.emmenia.exception.ReadDataException;
import ru.rinastachel.emmenia.exception.SaveDataException;
import ru.rinastachel.emmenia.dialogs.Dialogs;

public class MainActivity extends Activity {
	
	private ListView _listView;
	private TextView _noData;
	private MainListAdapter _adapter;
	private EntitySortedList _entitySortedList;
	private TextView _nextDate;
	private TextView _nextDay;
	private TextView _nextComment;

	public interface OnAddDialogListener {
		public void addEntity (Calendar calendar, String string);
	}
	
	public interface OnRemoveDialogListener {
		public void removeEntity (Entity entity);
	}
	
	OnAddDialogListener _addDialogListener = new OnAddDialogListener(){
		@Override
		public void addEntity(Calendar date, String comment) {
			addNewEntity(date, comment);
		}
	};
	
	OnRemoveDialogListener _removeDialogListener = new OnRemoveDialogListener(){
		@Override
		public void removeEntity(Entity entity) {
			try {
				_entitySortedList.remove(entity);
			} catch (SaveDataException e) {
				Toast.makeText(MainActivity.this, getString(R.string.error_save_data), Toast.LENGTH_LONG).show();
			}
		}
	};
	
	OnItemLongClickListener _onItemLongClickListener = new OnItemLongClickListener(){
		@Override
		public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
			Entity entity = (Entity) view.getTag();
			if (entity != null) {
				Dialog removeDialog = Dialogs.dialogRemoveEntity(MainActivity.this, entity, _removeDialogListener);
				removeDialog.show();
			}
			return false;
		}
	};
	
	OnEntityListListener _onEntityListListener = new OnEntityListListener () {
		@Override
		public void onEntityListChanged() {
			
			if (!_entitySortedList.isEmpty()) {
				showListView();
				
			} else {
				showHelpView();
			}			
		}
	};
	
	OnLongClickListener _onImageLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View view) {
			if (addNewEntity (Calendar.getInstance(), null))
				Toast.makeText(MainActivity.this, getString(R.string.add_current_date), Toast.LENGTH_LONG).show();
			return false;
		}
	};

	private boolean addNewEntity(Calendar date, String comment) {
		try {
			_entitySortedList.add(date, comment);
		} catch (SaveDataException e) {
			Toast.makeText(MainActivity.this, getString(R.string.error_save_data), Toast.LENGTH_LONG).show();
			return false;
		} catch (EntityFromFutureException e) {
			Toast.makeText(MainActivity.this, getString(R.string.error_add_later), Toast.LENGTH_LONG).show();
			return false;
		} catch (EntityExistException e) {
			Date d = new Date(date);
			Toast.makeText(MainActivity.this, getString(R.string.error_add_exist, d.getFullString()), Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	protected void showListView() {
		
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

	protected void showHelpView() {
		_noData.setVisibility(View.VISIBLE);
		_listView.setVisibility(View.INVISIBLE);
		
		_nextDate.setText(R.string.no_date);
		_nextDay.setText(getString(R.string.day, 0));
		_nextComment.setText(getString(R.string.day_cycle, 0));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setShowActionMenuMore();
		
		setContentView(R.layout.activity_main);
		
		// init adapter
		_adapter = new MainListAdapter(this);
		
		// imageview
		ImageView imageView = (ImageView)findViewById(R.id.next_image);
		imageView.setOnLongClickListener(_onImageLongClickListener);
								
		// listview
		_listView = (ListView)findViewById(R.id.main_list);
		_listView.setAdapter(_adapter);
		_listView.setOnItemLongClickListener(_onItemLongClickListener);
		
		// no data
		_noData = (TextView)findViewById(R.id.no_data);
		
		// next data
		_nextDate = (TextView)findViewById(R.id.next_date);
		_nextDay = (TextView)findViewById(R.id.next_days);
		_nextComment = (TextView)findViewById(R.id.next_comment);
										
		// init data array
		_entitySortedList = new EntitySortedList (this, _onEntityListListener);
	}
	
	private void setShowActionMenuMore() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
		}
	}

	@Override
	public void onResume () {
		super.onResume();
		if (_entitySortedList == null)
			_entitySortedList = new EntitySortedList (this, _onEntityListListener);
		
		try {
			_entitySortedList.readSavedData();
		} catch (NoSavedDataException e) {

		} catch (ReadDataException e) {
			Toast.makeText(this, getString(R.string.error_read_data), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add:
				Dialog addDialog = Dialogs.dialogAddEntity(this, _addDialogListener);
				addDialog.show();
				return true;
			
			case R.id.action_settings:
				Intent settingsActivity = new Intent(this, PrefActivity.class);
				startActivity(settingsActivity);
				return true;
		}
		return false;
	}
}
