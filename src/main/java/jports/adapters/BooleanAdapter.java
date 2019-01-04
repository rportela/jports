package jports.adapters;

import jports.ShowStopper;

public class BooleanAdapter implements Adapter<Boolean> {

	@Override
	public Boolean parse(String source) {
		return source == null || source.isEmpty()
				? null
				: Boolean.valueOf(source);
	}

	@Override
	public String format(Boolean source) {
		return source == null
				? null
				: source.toString();
	}

	@Override
	public Boolean convert(Object source) {
		if (source == null)
			return false;
		else if (source instanceof Boolean)
			return ((Boolean) source);
		else if (source instanceof Number)
			return ((Number) source).intValue() != 0;
		else if (source instanceof String)
			return parse((String) source);
		else
			throw new ShowStopper("Can't convert " + source + " to Boolean.");
	}

	@Override
	public Class<Boolean> getDataType() {
		return Boolean.class;
	}

}
