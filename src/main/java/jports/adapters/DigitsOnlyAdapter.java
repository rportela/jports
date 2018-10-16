package jports.adapters;

public class DigitsOnlyAdapter implements Adapter<String> {

	@Override
	public String parse(String source) {
		if (source == null || source.isEmpty())
			return source;
		StringBuilder builder = new StringBuilder(source.length());
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			if (Character.isDigit(c))
				builder.append(c);
		}
		return builder.toString();
	}

	@Override
	public String format(String source) {
		return parse(source);
	}

	@Override
	public String convert(Object source) {
		return parse(source == null
				? null
				: source.toString());
	}

	@Override
	public Class<String> getDataType() {
		return String.class;
	}

}
