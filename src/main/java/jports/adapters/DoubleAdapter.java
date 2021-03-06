package jports.adapters;

import java.util.Calendar;
import java.util.Date;

import jports.ShowStopper;

public class DoubleAdapter implements Adapter<Double> {

	public Double parse(String source) {
		return source == null || source.isEmpty()
				? null
				: Double.valueOf(source);
	}

	public String format(Double source) {
		return source == null
				? ""
				: Double.toString(source);
	}

	@Override
	public String formatObject(Object source) {
		return format((Double) source);
	}

	public Double convert(Object source) {
		if (source == null)
			return null;
		else if (source instanceof Number)
			return ((Number) source).doubleValue();
		else if (source instanceof String)
			return parse((String) source);
		else if (source instanceof Date)
			return (double) ((Date) source).getTime();
		else if (source instanceof Calendar)
			return (double) ((Calendar) source).getTimeInMillis();
		else
			throw new ShowStopper("Can't convert " + source.getClass() + " to " + getDataType());
	}

	public Class<Double> getDataType() {
		return Double.class;
	}

}
