package ru.rinastachel.emmenia.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import ru.rinastachel.emmenia.exception.EntityExistException;
import ru.rinastachel.emmenia.exception.EntityFromFutureException;
import ru.rinastachel.emmenia.exception.NoSavedDataException;
import ru.rinastachel.emmenia.exception.ReadDataException;
import ru.rinastachel.emmenia.exception.SaveDataException;
import ru.rinastachel.emmenia.manager.PrefManager;
import ru.rinastachel.emmenia.service.EmmeniaWidget;
import android.content.Context;

public class EntitySortedList implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int CYCLE_DEFAULT = 28;
	private static final int CYCLE_CALC_PERIOD = 6;
	private OnEntityListListener _listener;
	private Context _context;
	private DataListSaver _listSaver;
	
	public interface OnEntityListListener {
		public void onEntityListChanged();
	}
	
	private ArrayList<Entity> _list;
	private int _cycle;
	
	public EntitySortedList (Context ctx, OnEntityListListener listener) {
		_context = ctx;
		_list = new ArrayList<Entity>();
		_listener = listener;
		_listSaver = new DataListSaver(_context);
	}
	
	public void add(Calendar date, String comment) throws SaveDataException, EntityFromFutureException, EntityExistException {
		if (date.compareTo(Calendar.getInstance()) > 0) {
			throw new EntityFromFutureException();
		}
		if (isContainDate(date)) {
			throw new EntityExistException();
		}

		_list.add(new Entity(date, comment, true));
		listChanged();
		writeSavedData();
	}
	
	public void remove (Entity entity) throws SaveDataException {
		_list.remove(entity);
		listChanged();
		writeSavedData();
	}
	
	private boolean isContainDate(Calendar date) {
		Date d = new Date(date);
		for (Entity entity : _list) {
			if (entity.getDate().equals(d)) {
				return true;
			}
		}
		return false;
	}

	private void listChanged() {
		
		Collections.sort(_list);
		_cycle = calcCycle();
		
		if (_listener != null)
			_listener.onEntityListChanged();
	}

	public void readSavedData() throws NoSavedDataException, ReadDataException {
		try {
			_list = _listSaver.readData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_list.clear();
			throw new NoSavedDataException();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			_list.clear();
			throw new ReadDataException();
		}catch (IOException e) {
			e.printStackTrace();
			_list.clear();
			throw new ReadDataException();
		} finally {
			listChanged();
		}
	}
	
	private void writeSavedData() throws SaveDataException {
		try {
			_listSaver.saveData(_list);
			PrefManager.setDataForWidget(_context, getLastCalendar(), getNextCalendar());
			EmmeniaWidget.updateAllWidgets(_context);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SaveDataException();			
		}
	}
	
	public int size() {
		return _list.size();
	}

	public Entity get(int position) {
		return _list.get(position);
	}
	
	private int calcCycle () {
		if (_list == null || _list.size() < 2) {
			return CYCLE_DEFAULT;
		}
		int size = Math.min(_list.size(), CYCLE_CALC_PERIOD);
		int sum = 0;
		Entity prev = _list.get(0);
		for (int i = 1; i < size; i++) {
			Entity entity = _list.get(i);
			sum += entity.calcDiff(prev);
			prev = entity;
		}
		return sum / (size - 1);
	}
	
	public int getCycle() {
		return _cycle;
	}

	public Date getLastCalendar() {
		if (_list == null || _list.size() == 0)
			return new Date();
		return _list.get(0).getDate().getClone();
	}

	public boolean isEmpty () {
		return _list == null || _list.isEmpty();
	}

	public int getCurrentDay() {
		Date lastDate = getLastCalendar();
		return lastDate.calcDiff(new Date()) + 1;
	}

	public Date getNextCalendar() {
		Date lastDate = getLastCalendar();
		lastDate.add(Calendar.DAY_OF_MONTH, getCycle());
		return lastDate;
	}

	public void setDateList(ArrayList<Entity> list) throws SaveDataException {
		_list = list;
		listChanged();
		writeSavedData();
	}
	
}
