package ru.rinastachel.emmenia.adapter;

import ru.rinastachel.emmenia.adapter.MainListAdapter.RowType;
import ru.rinastachel.emmenia.data.Entity;

import ru.rinastachel.emmenia.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class EntityItem implements Item {
	
	private Entity _entity;
	private int _day;

    public EntityItem(Entity entity, int day) {
    	_entity = entity;
    	_day = day;
    }

    @Override
    public int getViewType() {
    	return RowType.ENTITY_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {

        if (convertView == null) {
        	convertView = (View) inflater.inflate(R.layout.item_main_entity, null);
        } 

        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(_entity.getDate().getSimpleString());
        
        TextView comment = (TextView) convertView.findViewById(R.id.comment);
        comment.setText(_entity.getComment());
        
        TextView days = (TextView) convertView.findViewById(R.id.days);
        if (_day > 0) {
        	days.setText(inflater.getContext().getString(R.string.day, _day));
        } else {
        	days.setText(inflater.getContext().getString(R.string.empty_string));
        }
        
        if (_entity.isNew()) {
        	Animation anim = AnimationUtils.loadAnimation(inflater.getContext(), android.R.anim.slide_in_left);
        	convertView.setAnimation(anim);
        	_entity.setOld();
        }
        convertView.setTag(_entity);
        return convertView;
    }
}