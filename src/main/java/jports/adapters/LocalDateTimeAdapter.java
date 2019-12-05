package jports.adapters;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import jports.ShowStopper;

public class LocalDateTimeAdapter implements Adapter<LocalDateTime> {

	private DateTimeFormatter formatter;

	public LocalDateTimeAdapter() {
		this(DateTimeFormatter.ISO_DATE_TIME);
	}

	public LocalDateTimeAdapter(DateTimeFormatter format) {
		this.formatter = format;
	}

	public LocalDateTimeAdapter(String pattern) {
		this(pattern, Locale.getDefault());
	}

	public LocalDateTimeAdapter(String pattern, Locale locale) {
		this(DateTimeFormatter.ofPattern(pattern, locale));
	}

	@Override
	public LocalDateTime parse(String source) {
		try {
			return source == null || source.isEmpty() || "00000000".equals(source) ?
					null :
					LocalDateTime.parse(source, formatter);
		} catch (Exception e) {
			throw new ShowStopper(
					"unable to parse LocalDateTime " +
							source +
							" with format " +
							this.formatter,
					e);
		}
	}

	@Override
	public String format(LocalDateTime source) {
		return source == null ? null : formatter.format(source);
	}

	@Override
	public LocalDateTime convert(Object source) {
		if (source == null)
			return null;
		Class<?> dataType = source.getClass();
		if (source instanceof LocalDateTime) {
			return (LocalDateTime) source;
		} else if (source instanceof java.util.Date) {
			return LocalDateTime.ofInstant(((java.util.Date) source).toInstant(), ZoneId.systemDefault());
		} else if (source instanceof Calendar) {
			Calendar cal = (Calendar) source;
			return LocalDateTime.of(
					cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH),
					cal.get(Calendar.HOUR_OF_DAY),
					cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
		} else if (dataType.equals(Long.TYPE)) {
			return LocalDateTime.ofEpochSecond((long) source, 0, ZoneOffset.UTC);
		} else if (Number.class.isAssignableFrom(dataType)) {
			return LocalDateTime.ofEpochSecond(((Number) source).longValue(), 0, ZoneOffset.UTC);
		} else if (source instanceof String) {
			return parse((String) source);
		} else {
			throw new ShowStopper("Can't convert to java.util.LocalDateTime: " + source);
		}
	}

	@Override
	public Class<LocalDateTime> getDataType() {
		return LocalDateTime.class;
	}
}
