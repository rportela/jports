package jports.calendar.br;

import java.util.Calendar;

import jports.calendar.CalendarHelper;
import jports.calendar.Holliday;

public class CarnivalWednesday implements Holliday {

	@Override
	public String getName() {
		return "Carnival Wednesday";
	}

	@Override
	public String getReason() {
		return "Carnival Wednesday";
	}

	@Override
	public Calendar getCalendar(int year) {
		Calendar easter = CalendarHelper.getEasterSunday(year);
		easter.add(Calendar.DAY_OF_MONTH, -46);
		return easter;
	}

}
