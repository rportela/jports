package jports.validations;

import java.util.ArrayList;

public class ValidationList extends ArrayList<Validation> implements Validation {

	/**
	 * The general serial version UID;
	 */
	private static final long serialVersionUID = -7650905626018213290L;

	public ValidationResult validate(String name, Object value) {
		if (isEmpty())
			return new ValidationResult(name, true, null);
		ValidationResult[] children = new ValidationResult[size()];
		boolean isValid = true;
		for (int i = 0; i < children.length; i++) {
			children[i] = get(i).validate(name, value);
			if (isValid
					&& !children[i].isValid())
				isValid = false;
		}
		return new ValidationResult(name, isValid, isValid
				? null
				: "Some items failed to validate, check the list of children");
	}

	public boolean isValid(Object value) {
		for (Validation v : this)
			if (!v.isValid(value))
				return false;
		return true;
	}

}
