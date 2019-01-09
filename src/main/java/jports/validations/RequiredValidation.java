package jports.validations;

import java.util.UUID;

/**
 * This class implements a required validation that checks if a given value is
 * null or if it's a string, if it's empty; It also passes the message provided
 * in a constructor in an annotation;
 * 
 * @author rportela
 *
 */
public class RequiredValidation extends AbstractValidation {

	/**
	 * Creates a new instance of the required validation using a specific error
	 * message;
	 * 
	 * @param message
	 */
	public RequiredValidation(String message) {
		super(message);
	}

	/**
	 * Creates a new instance of the required validation using the defautl error
	 * message;
	 * 
	 */
	public RequiredValidation() {
		super("Required");
	}

	/**
	 * Creates a new instance of the required validation using annotated values on
	 * the Required annotation;
	 * 
	 * @param annotation
	 */
	public RequiredValidation(Required annotation) {
		super(annotation.value());
	}

	/**
	 * Validates an input object;
	 */
	@Override
	public boolean isValid(Object value) {
		if (value == null)
			return false;
		else if (value instanceof String)
			return !((String) value).isEmpty();
		else if (value instanceof UUID)
			return ((UUID) value).getLeastSignificantBits() != 0L &&
					((UUID) value).getMostSignificantBits() != 0L;
		else
			return true;

	}

}
