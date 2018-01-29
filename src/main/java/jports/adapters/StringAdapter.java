package jports.adapters;

public class StringAdapter implements Adapter<String> {

	public String parse(String source) {
		return source;
	}

	public String format(String source) {
		return source;
	}

	public String formatObject(Object source) {
		return source == null
				? ""
				: source.toString();
	}

	public String convert(Object source) {
		return source == null
				? null
				: source.toString();
	}

	public Class<String> getDataType() {
		return String.class;
	}

}
