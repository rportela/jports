package jports.adapters;

import jports.ShowStopper;

public class VoidAdapter implements Adapter<Void> {

	public Void parse(String source) {
		throw new ShowStopper("Can't parse void: " + source);
	}

	public String format(Void source) {
		throw new ShowStopper("Can't format void: " + source);
	}

	public Void convert(Object source) {
		throw new ShowStopper("Can't convert to void, sorry: " + source);
	}

	public Class<Void> getDataType() {
		return Void.class;
	}

}
