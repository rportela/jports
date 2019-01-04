package jports.adapters;

import java.util.Calendar;
import java.util.Date;

import jports.ShowStopper;

public class FloatAdapter implements Adapter<Float> {

	public Float parse(String source) {
		return source == null || source.isEmpty()
				? null
				: Float.valueOf(source);
	}

	public String format(Float source) {
		return source == null
				? ""
				: Float.toString(source);
	}

	@Override
	public String formatObject(Object source) {
		return format((Float) source);
	}

	public Float convert(Object source) {
		if (source == null)
			return null;
		else if (source instanceof Number)
			return ((Number) source).floatValue();
		else if (source instanceof String)
			return parse((String) source);
		else if (source instanceof Date)
			return (float) ((Date) source).getTime();
		else if (source instanceof Calendar)
			return (float) ((Calendar) source).getTimeInMillis();
		else
			throw new ShowStopper("Can't convert " + source.getClass() + " to " + getDataType());
	}

	public Class<Float> getDataType() {
		return Float.class;
	}

}
