package jports.validations;

import java.util.regex.Pattern;

/**
 * The email validation based on a common email regular expression pattern;
 * 
 * @author rportela
 *
 */
public class EmailValidation extends AbstractValidation {

	public static final String DEFAULT_EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private final Pattern pattern;

	/**
	 * Creates a new instance of the email validation class using a specific message
	 * when e-mails are invalid and having a specific email pattern;
	 * 
	 * @param message
	 * @param pattern
	 */
	public EmailValidation(String message, String pattern) {
		super(message);
		this.pattern = Pattern.compile(pattern);
	}

	/**
	 * Creates a new instance of the email validation class using a specific message
	 * when e-mails are invalid;
	 * 
	 * @param message
	 */
	public EmailValidation(String message) {
		this(message, DEFAULT_EMAIL_PATTERN);
	}

	/**
	 * Creates a new instance of the email validation class using the default
	 * message when e-mails are invalid;
	 */
	public EmailValidation() {
		this("Invalid e-mail address");
	}

	/**
	 * Tells if an object, converted to string, matches the email pattern defined on
	 * this instance;
	 */
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
