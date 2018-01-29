package jports.validations;

import java.util.regex.Pattern;

public class EmailValidation extends AbstractValidation {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	public EmailValidation(String message) {
		super(message);
	}

	public EmailValidation() {
		super("Invalid e-mail address");
	}

	@Override
	public boolean isValid(Object value) {
		if (value == null)
			return true;

		String email = (String) value;
		if (email.isEmpty())
			return true;

		return pattern.matcher(email).matches();

	}

}
