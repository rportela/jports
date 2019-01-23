package jports.validations;

public class LengthValidation extends AbstractValidation {

	private int min;
	private int max;

	public LengthValidation(int min, int max, String message) {
		super(String.format(message, min, max));
		this.min = min;
		this.max = max;
	}

	public LengthValidation(Length annotation) {
		this(annotation.min(), annotation.max(), annotation.message());
	}

	public int getMin() {
		return this.min;
	}

	public int getMax() {
		return this.max;
	}

	@Override
	public boolean isValid(Object value) {
		if (value == null)
			return min > 0;
		else {
			String s = (String) value;
			return s.length() >= min && s.length() <= max;
		}
	}

}
