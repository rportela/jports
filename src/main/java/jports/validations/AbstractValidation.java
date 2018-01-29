package jports.validations;

public abstract class AbstractValidation implements Validation {

	public String message;

	public AbstractValidation(String message) {
		this.message = message;
	}

	public ValidationResult validate(String name, Object value) {
		try {
			return isValid(value)
					? new ValidationResult(name, true, null)
					: new ValidationResult(name, false, message);
		} catch (Exception e) {
			return new ValidationResult(name, false, e.getMessage());
		}
	}

	public abstract boolean isValid(Object value);

}
