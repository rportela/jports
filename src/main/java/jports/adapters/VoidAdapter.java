package jports.adapters;

public class VoidAdapter implements Adapter<Void> {

	public Void parse(String source) {
		throw new RuntimeException("Can't parse void: " + source);
	}

	public String format(Void source) {
		throw new RuntimeException("Can't format void: " + source);
	}

	public Void convert(Object source) {
		throw new RuntimeException("Can't convert to void, sorry: " + source);
	}

	public Class<Void> getDataType() {
		return Void.class;
	}

}
