package jports.validations;

/**
 * This is a helper abstract validation that implements the Validation interface
 * and exposes methods for testing whether a value is valid or not;
 * 
 * @author rportela
 *
 */
public abstract class AbstractValidation implements Validation {

	private final String message;

	/**
	 * Creates a new instance of a validation with a default error message for when
	 * things go south;
	 * 
	 * @param message
	 */
	public AbstractValidation(String message) {
		this.message = message;
	}

	/**
	 * Validates an input value and, if things go south, produces a validation
	 * result with the passed name and an error message;
	 */
	public ValidationResult validate(String name, Object value) {
		try {
			return isValid(value)
					? new ValidationResult(name, true, null)
					: new ValidationResult(name, false, message);
		} catch (Exception e) {
			return new ValidationResult(name, false, e.getMessage());
		}
	}

}
