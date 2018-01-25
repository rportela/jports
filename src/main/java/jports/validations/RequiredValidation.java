package jports.validations;

/**
 * This class implements a required validation that checks if a given value is
 * null or if it's a string, if it's empty; It also passes the message provided
 * in a constructor in an annotation;
 * 
 * @author rportela
 *
 */
public class RequiredValidation implements Validation {

	public String message;

	public RequiredValidation() {
		this("Required");
	}

	public RequiredValidation(String message) {
		this.message = message;
	}

	public RequiredValidation(Required annotation) {
		this(annotation.value());
	}

	public ValidationResult validate(Object source) {
		if (source == null)
			return new ValidationResult(false, this.message);
		else if (source instanceof String && ((String) source).isEmpty())
			return new ValidationResult(false, this.message);
		else
			return new ValidationResult(true, null);
	}

}
