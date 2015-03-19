package ru.rinastachel.emmenia.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.ArrayList;

import ru.rinastachel.emmenia.R;
import ru.rinastachel.emmenia.data.Entity;
import ru.rinastachel.emmenia.data.EntitySortedList;

public class MainListAdapter implements ListAdapter {
    private LayoutInflater mInflater;
    ArrayList<Item> _items;
    DataSetObserver _observer;
    private Context _context;

    public enum RowType {
    	ENTITY_ITEM, GROUP_ITEM
    }

    public MainListAdapter(Context context) {
    	_items = new ArrayList<Item>();
        mInflater = LayoutInflater.from(context);
        _context = context;
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position) {
        return ((Item) getItem(position)).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return ((Item) getItem(position)).getView(mInflater, convertView);
    }

	@Override
	public int getCount() {
		return _items.size();
	}

	@Override
	public Object getItem(int position) {
		return _items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return _items.size() == 0;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		_observer = observer;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		_observer = null;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		if (getItem(position) instanceof GroupItem)
			return false;
		return true;
	}
	
	public void notifyDataChanged(EntitySortedList _list) {
		_items.clear();
		if (_list.size() == 0)
			return;
		
		Entity prev = _list.get(0);
		_items.add(new GroupItem(_context.getString(R.string.year, prev.getYear())));
		
		for (int i = 1; i < _list.size(); i++) {
			Entity entity = _list.get(i);
			_items.add(new EntityItem(prev, entity.calcDiff(prev)));
			
			if (prev.getYear() != entity.getYear())
				_items.add(new GroupItem(_context.getString(R.string.year, entity.getYear())));
			
			prev = entity;
		}
		_items.add(new EntityItem(prev, 0));
		
		if (_observer != null)
			_observer.onChanged();
	}
}
