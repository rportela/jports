package jports.validations;

public class MaxNumberInclusiveValidation extends AbstractValidation {

	private double max;

	/**
	 * Creates a new instance of the required validation using a specific error
	 * message;
	 * 
	 * @param message
	 */
	public MaxNumberInclusiveValidation(double max, String message) {
		super(String.format(message, max));
		this.max = max;
	}

	/**
	 * Creates a new instance of the required validation using the default error
	 * message;
	 * 
	 */
	public MaxNumberInclusiveValidation(double max) {
		this(max, "Value above the exclusive max of %f");
	}

	/**
	 * Creates a new instance of the required validation using annotated values on
	 * the Required annotation;
	 * 
	 * @param annotation
	 */
	public MaxNumberInclusiveValidation(MaxNumberInclusive annotation) {
		this(annotation.value(), annotation.message());
	}

	/**
	 * Validates an input object;
	 */
	@Override
	public boolean isValid(Object value) {
		if (value == null)
			return false;

		return ((Number) value).doubleValue() <= max;

	}

}
