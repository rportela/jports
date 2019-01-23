package jports.validations;

public class MaxLengthValidation extends AbstractValidation {

	private int max;

	public MaxLengthValidation(int max, String message) {
		super(String.format(message, max));
		this.max = max;
	}

	public MaxLengthValidation(MaxLength annotation) {
		this(annotation.value(), annotation.message());
	}

	@Override
	public boolean isValid(Object value) {
		return value == null || ((String) value).length() < max;
	}

}
