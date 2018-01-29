package jports.validations;

public interface Validation {

	public ValidationResult validate(String name, Object value);

	public boolean isValid(Object value);
}
