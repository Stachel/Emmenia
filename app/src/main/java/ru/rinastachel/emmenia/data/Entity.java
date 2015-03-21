package ru.rinastachel.emmenia.data;

import java.io.Serializable;
import java.util.Calendar;

public class Entity implements Serializable, Comparable<Entity>{
	
	private static final long serialVersionUID = 1L;
	
	private Date _date;
	private String _comment;
	private boolean _isNew;
	
	public Entity (Calendar date, String comment) {
		_date = new Date(date);
		_comment = comment;
	}
	
	public Entity(Calendar date, String comment, boolean isNew) {
		this(date, comment);
		_isNew = isNew;
	}

	public Date getDate() {
		return _date;
	}
	
	public String getComment() {
		return _comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_date == null) ? 0 : _date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (_date == null) {
			if (other._date != null)
				return false;
		} else if (!_date.equals(other._date))
			return false;
		return true;
	}

	@Override
	public int compareTo(Entity another) {
		return (-1) * getDate().compareTo(another.getDate());
	}

	public int getYear() {
		return _date.getCalendar().get(Calendar.YEAR);
	}

	public int calcDiff(Entity prev) {
		return _date.calcDiff(prev.getDate());
	}

	public void setOld() {
		_isNew = false;
	}
	
	public boolean isNew() {
		return _isNew;
	}
}
