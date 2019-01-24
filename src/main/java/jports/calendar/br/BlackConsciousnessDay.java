package jports.calendar.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jports.calendar.Holliday;

public class BlackConsciousnessDay implements Holliday {

	@Override
	public String getName() {
		return "Black Consciousness Day in Brazil";
	}

	@Override
	public String getReason() {
		return "No description";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 10, 20);
	}

}
