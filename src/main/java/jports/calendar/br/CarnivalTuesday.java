package jports.calendar.br;

import java.util.Calendar;

import jports.calendar.CalendarHelper;
import jports.calendar.Holliday;

public class CarnivalTuesday implements Holliday {

	@Override
	public String getName() {
		return "Carnival Tuesday";
	}

	@Override
	public String getReason() {
		return "Carnival Tuesday";
	}

	@Override
	public Calendar getCalendar(int year) {
		Calendar easter = CalendarHelper.getEasterSunday(year);
		easter.add(Calendar.DAY_OF_MONTH, -47);
		return easter;

	}

}
