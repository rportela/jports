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

	public RequiredValidation(String message) {
		super(message);
	}

	public RequiredValidation() {
		super("Required");
	}

	public RequiredValidation(Required annotation) {
		super(annotation.value());
	}

	@Override
	public boolean isValid(Object value) {
		if (value == null)
			return false;
		else if (value instanceof String
				&& ((String) value).isEmpty())
			return false;
		else if (value instanceof UUID
				&& ((UUID) value).getLeastSignificantBits() == 0L
				&& ((UUID) value).getMostSignificantBits() == 0L)
			return false;
		else
			return true;
	}

}
