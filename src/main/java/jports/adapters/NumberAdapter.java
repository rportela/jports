package jports.adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class NumberAdapter implements Adapter<Number> {

	public final NumberFormat format;

	public NumberAdapter(NumberFormat format) {
		this.format = format;
	}

	public NumberAdapter() {
		this(NumberFormat.getInstance());
	}

	public NumberAdapter(String format) {
		this(new DecimalFormat(format));
	}

	public Number parse(String source) {
		try {
			return source == null
					|| source.isEmpty()
							? null
							: format.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public String format(Number source) {
		return source == null
				? null
				: this.format.format(source);
	}

	public String formatObject(Object source) {
		return source == null
				? null
				: this.format.format(source);
	}

	public Number convert(Object source) {
		if (source == null)
			return null;
		else if (source instanceof Number)
			return (Number) source;
		else if (source instanceof CharSequence)
			return parse(source.toString());
		else if (source instanceof Date)
			return ((Date) source).getTime();
		else if (source instanceof Calendar)
			return ((Calendar) source).getTimeInMillis();
		else
			throw new RuntimeException("Can't convert "
					+ source.getClass()
					+ " to "
					+ getDataType());
	}

	public Class<Number> getDataType() {
		return Number.class;
	}

}
