package jports.adapters;

public class IntegerAdapter implements Adapter<Integer> {

	@Override
	public Integer parse(String source) {
		return source == null || source.isEmpty()
				? null
				: Integer.valueOf(source);
	}

	@Override
	public String format(Integer source) {
		return source.toString();
	}

	@Override
	public Integer convert(Object source) {
		if (source == null)
			return null;
		else if (source instanceof Integer)
			return (Integer) source;
		else if (Number.class.isAssignableFrom(source.getClass()))
			return ((Number) source).intValue();
		else
			throw new RuntimeException("Can't convert " + source + " to Integer.");
	}

	@Override
	public Class<Integer> getDataType() {
		return Integer.class;
	}

}
