package jports.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jports.calendar.br.AllSoulsDay;
import jports.calendar.br.BlackConsciousnessDay;
import jports.calendar.br.CarnivalMonday;
import jports.calendar.br.CarnivalTuesday;
import jports.calendar.br.CarnivalWednesday;
import jports.calendar.br.ChildrensDay;
import jports.calendar.br.ChristmasDay;
import jports.calendar.br.CorpusChristi;
import jports.calendar.br.GoodFriday;
import jports.calendar.br.IndependenceDay;
import jports.calendar.br.LaborDay;
import jports.calendar.br.NewYearsDay;
import jports.calendar.br.NewYearsEve;
import jports.calendar.br.RepublicProclamationDay;
import jports.calendar.br.TiradentesDay;

/**
 * A helper class that can determine if a given date is a business day or not;
 * It also knows every holliday in Brazil;
 * 
 * @author rportela
 *
 */
public class BrazilianCalendar {

	private BrazilianCalendar() {
	}

	/**
	 * Holds a static list of brazilian hollidays;
	 */
	protected static final List<Holliday> HOLLIDAYS;

	static {
		HOLLIDAYS = new LinkedList<>();
		HOLLIDAYS.add(new AllSoulsDay());
		HOLLIDAYS.add(new BlackConsciousnessDay());
		HOLLIDAYS.add(new CarnivalMonday());
		HOLLIDAYS.add(new CarnivalTuesday());
		HOLLIDAYS.add(new CarnivalWednesday());
		HOLLIDAYS.add(new ChildrensDay());
		HOLLIDAYS.add(new ChristmasDay());
		HOLLIDAYS.add(new CorpusChristi());
		HOLLIDAYS.add(new GoodFriday());
		HOLLIDAYS.add(new IndependenceDay());
		HOLLIDAYS.add(new LaborDay());
		HOLLIDAYS.add(new NewYearsDay());
		HOLLIDAYS.add(new NewYearsEve());
		HOLLIDAYS.add(new RepublicProclamationDay());
		HOLLIDAYS.add(new TiradentesDay());
	}

	/**
	 * Indicates that a given calendar date is a business day or not;
	 * 
	 * @param calendar
	 * @return
	 */
	public static boolean isBusinessDay(final Calendar calendar) {
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY)
			return false;
		if (dayOfWeek == Calendar.SATURDAY)
			return false;
		for (Holliday holliday : HOLLIDAYS) {
			Calendar hol = holliday.getCalendar(calendar.get(Calendar.YEAR));
			if (hol.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
					hol.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
					hol.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
				return true;
		}
		return true;
	}

	/**
	 * Indicates that a given date is a business day or not based on the week day
	 * and the list of hollidays for Brazil;
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBusinessDay(final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return isBusinessDay(cal);
	}

}
