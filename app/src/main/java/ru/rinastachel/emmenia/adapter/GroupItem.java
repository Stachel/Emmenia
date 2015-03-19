package ru.rinastachel.emmenia.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ru.rinastachel.emmenia.R;
import ru.rinastachel.emmenia.adapter.MainListAdapter.RowType;

public class GroupItem implements Item {

	private String _title;

	public GroupItem(String title) {
		_title = title;
	}

	@Override
	public int getViewType() {
		return RowType.GROUP_ITEM.ordinal(); // return 1
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			view = (View) inflater.inflate(R.layout.item_main_group, null);
		} else {
			view = convertView;
		}
	        
		TextView text = (TextView) view.findViewById(R.id.separator);
	    text.setText(_title);
	    return view;
	}
}
