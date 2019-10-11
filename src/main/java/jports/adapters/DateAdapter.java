package jports.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jports.ShowStopper;

public class DateAdapter implements Adapter<Date> {

	private DateFormat formatter;

	public DateAdapter() {
		this(DateFormat.getInstance());
	}

	public DateAdapter(DateFormat format) {
		this.formatter = format;
	}

	public DateAdapter(String pattern) {
		this(new SimpleDateFormat(pattern));
	}

	public DateAdapter(String pattern, Locale locale) {
		this(new SimpleDateFormat(pattern, locale));
	}

	@Override
	public Date parse(String source) {
		try {
			return source == null || source.isEmpty() || "00000000".equals(source)
					? null
						: formatter.parse(source);
		} catch (Exception e) {
			throw new ShowStopper(
					"unable to parse date " +
							source +
							" with format " +
							((SimpleDateFormat) this.formatter).toPattern(),
					e);
		}
	}

	@Override
	public String format(Date source) {
		return source == null
				? null
					: formatter.format(source);
	}

	@Override
	public Date convert(Object source) {
		if (source == null)
			return null;
		Class<?> dataType = source.getClass();
		if (source instanceof Date)
			return (Date) source;
		else if (source instanceof Calendar)
			return ((Calendar) source).getTime();
		else if (dataType.equals(Long.TYPE))
			return new Date((long) source);
		else if (Number.class.isAssignableFrom(dataType))
			return new Date(((Number) source).longValue());
		else if (source instanceof String)
			return parse((String) source);
		else
			throw new ShowStopper("Can't convert to java.util.Date: " + source);
	}

	@Override
	public Class<Date> getDataType() {
		return Date.class;
	}
}
