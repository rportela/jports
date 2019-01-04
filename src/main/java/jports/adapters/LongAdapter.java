package jports.adapters;

import jports.ShowStopper;

public class LongAdapter implements Adapter<Long> {

	@Override
	public Long parse(String source) {
		return source == null || source.isEmpty()
				? null
				: Long.valueOf(source);
	}

	@Override
	public String format(Long source) {
		return source.toString();
	}

	@Override
	public Long convert(Object source) {
		if (source == null)
			return null;
		else if (source instanceof Long)
			return (Long) source;
		else if (Number.class.isAssignableFrom(source.getClass()))
			return ((Number) source).longValue();
		else if (source instanceof String)
			return parse((String) source);
		else
			throw new ShowStopper("Can't convert " + source + " (" + source.getClass() + ") to Long.");
	}

	@Override
	public Class<Long> getDataType() {
		return Long.class;
	}

}
