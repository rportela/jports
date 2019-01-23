package jports.validations;

public class MinNumberExclusiveValidation extends AbstractValidation {

	private double min;

	/**
	 * Creates a new instance of the required validation using a specific error
	 * message;
	 * 
	 * @param message
	 */
	public MinNumberExclusiveValidation(double min, String message) {
		super(message);
		this.min = min;
	}

	/**
	 * Creates a new instance of the required validation using the default error
	 * message;
	 * 
	 */
	public MinNumberExclusiveValidation(double min) {
		this(min, "Valor muito pequeno, abaixo do mínimo.");
	}

	/**
	 * Creates a new instance of the required validation using annotated values on
	 * the Required annotation;
	 * 
	 * @param annotation
	 */
	public MinNumberExclusiveValidation(MinNumberExclusive annotation) {
		this(annotation.value(), annotation.message());
	}

	/**
	 * Validates an input object;
	 */
	@Override
	public boolean isValid(Object value) {
		if (value == null)
			return false;

		return ((Number) value).doubleValue() > min;

	}

}
