package jports.calendar.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jports.calendar.Holliday;

public class TiradentesDay implements Holliday {

	@Override
	public String getName() {
		return "Tiradentes Day";
	}

	@Override
	public String getReason() {
		return "Tiradentes Day";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 03, 21);
	}

}
