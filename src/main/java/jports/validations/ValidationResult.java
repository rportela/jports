package jports.validations;

import java.util.Map;

public class ValidationResult {

	public boolean isValid;
	public String message;
	public Map<String, ValidationResult> children;

	public ValidationResult(boolean valid, String message) {
		this.isValid = valid;
		this.message = message;
	}
}
