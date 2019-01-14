package jports.adapters;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import jports.ShowStopper;

public class TimeAdapter implements Adapter<Time> {

	private DateFormat formatter;

	public TimeAdapter() {
		this(DateFormat.getTimeInstance());
	}

	public TimeAdapter(DateFormat format) {
		this.formatter = format;
	}

	public TimeAdapter(String pattern) {
		this(new SimpleDateFormat(pattern));
	}

	public TimeAdapter(String pattern, Locale locale) {
		this(new SimpleDateFormat(pattern, locale));
	}

	@Override
	public Time parse(String source) {
		try {
			return source == null || source.isEmpty()
					? null
					: new Time(formatter.parse(source).getTime());
		} catch (Exception e) {
			throw new ShowStopper(e);
		}
	}

	@Override
	public String format(Time source) {
		return source == null
				? null
				: formatter.format(source);
	}

	@Override
	public Time convert(Object source) {
		if (source == null)
			return null;
		Class<?> dataType = source.getClass();
		if (source instanceof Time)
			return (Time) source;
		else if (source instanceof Calendar)
			return new Time(((Calendar) source).getTimeInMillis());
		else if (dataType.equals(Long.TYPE))
			return new Time((long) source);
		else if (Number.class.isAssignableFrom(dataType))
			return new Time(((Number) source).longValue());
		else if (source instanceof String)
			return parse((String) source);
		else
			throw new ShowStopper("Can't convert to java.util.Time: " + source);
	}

	@Override
	public Class<Time> getDataType() {
		return Time.class;
	}
}
