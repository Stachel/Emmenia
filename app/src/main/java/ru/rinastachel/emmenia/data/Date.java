package ru.rinastachel.emmenia.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Date implements Comparable<Date>, Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	private Calendar _calendar;
	
	private DateFormat _dateFormatSimple = new SimpleDateFormat("dd MMMM", Locale.getDefault());
	private DateFormat _dateFormatFull = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
	private DateFormat _dateFormatDash = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

	public Date () {
		_calendar = Calendar.getInstance();
	}
	
	public Date (Calendar calendar) {
		_calendar = (Calendar) calendar.clone();
	}
	
	public Date (long millisec) {
		_calendar = Calendar.getInstance();
		_calendar.setTimeInMillis(millisec);
	}
	
	public Calendar getCalendar () {
		return _calendar;
	}

	@Override
	public int compareTo(Date another) {
		return _calendar.compareTo(another.getCalendar());
	}
	
	public int calcDiff(Date prev) {
		if (prev == null)
			return 0;
		long current = getMillisOnlyDate();
	    long previous = prev.getMillisOnlyDate();
	    long diff = previous - current;
	    long diffDays = diff / (24 * 60 * 60 * 1000);	    
	    return (int)diffDays;
	}
	
	public long getMillisOnlyDate () {
		// fix error with TimeZone
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, _calendar.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, _calendar.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, _calendar.get(Calendar.DAY_OF_MONTH));
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	public String getSimpleString() {
		return _dateFormatSimple.format(_calendar.getTime());
	}
	
	public String getFullString() {
		return _dateFormatFull.format(_calendar.getTime());
	}
	
	public String getDashString() {
		return _dateFormatDash.format(_calendar.getTime());
	}

	public Date getClone () {
		return new Date((Calendar) _calendar.clone());
	}

	public void add(int field, int value) {
		_calendar.add(field, value);		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_calendar == null) ? 0 : _calendar.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		
		Date otherDate = (Date) other;
		if (_calendar == null) {
			if (otherDate.getCalendar() != null)
				return false;
		} else {
			Calendar otherCalendar = otherDate.getCalendar();
			if (_calendar.get(Calendar.DAY_OF_MONTH) == otherCalendar.get(Calendar.DAY_OF_MONTH) &&
					_calendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH) &&
					_calendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR))
				return true;
		}
		return false;
	}
	
	public long getMillisecs () {
		return _calendar.getTimeInMillis();
	}
}
