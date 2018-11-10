package jports.adapters;

public class BooleanAsSpecificString extends BooleanAdapter {

	public final String specificValue;

	public BooleanAsSpecificString(String specificValue) {
		this.specificValue = specificValue;
	}

	@Override
	public Boolean parse(String source) {
		return specificValue.equalsIgnoreCase(source);
	}

}
