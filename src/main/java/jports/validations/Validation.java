package jports.validations;

/**
 * The validation interface that exposes methods for validating objects and
 * building a validation result, or simple tests of validity on objects;
 * 
 * @author rportela
 *
 */
public interface Validation {

	/**
	 * Validates a given name value pair and return the consolidated validation
	 * result;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public ValidationResult validate(String name, Object value);

	/**
	 * Tests if a specific value is valid against the properties of this validation;
	 * 
	 * @param value
	 * @return
	 */
	public boolean isValid(Object value);
}
