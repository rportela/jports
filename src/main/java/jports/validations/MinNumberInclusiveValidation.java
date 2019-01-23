package jports.validations;

public class MinNumberInclusiveValidation extends AbstractValidation {

	private double min;

	/**
	 * Creates a new instance of the required validation using a specific error
	 * message;
	 * 
	 * @param message
	 */
	public MinNumberInclusiveValidation(double min, String message) {
		super(message);
		this.min = min;
	}

	/**
	 * Creates a new instance of the required validation using the default error
	 * message;
	 * 
	 */
	public MinNumberInclusiveValidation(double min) {
		this(min, "Valor muito pequeno, abaixo do mínimo.");
	}

	/**
	 * Creates a new instance of the required validation using annotated values on
	 * the Required annotation;
	 * 
	 * @param annotation
	 */
	public MinNumberInclusiveValidation(MinNumberExclusive annotation) {
		this(annotation.value(), annotation.message());
	}

	/**
	 * Validates an input object;
	 */
	@Override
	public boolean isValid(Object value) {
		if (value == null)
			return false;

		return ((Number) value).doubleValue() >= min;

	}

}
