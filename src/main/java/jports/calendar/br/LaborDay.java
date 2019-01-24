package jports.calendar.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jports.calendar.Holliday;

public class LaborDay implements Holliday {

	@Override
	public String getName() {
		return "LaborDay";
	}

	@Override
	public String getReason() {
		return "LaborDay";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 4, 1);
	}

}
