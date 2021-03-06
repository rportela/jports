package jports.validations;

public class ValidationResult {

	private String name;
	private boolean isValid;
	private String message;
	private ValidationResult[] children;

	public ValidationResult(String name, boolean valid, String message, ValidationResult... children) {
		this.name = name;
		this.isValid = valid;
		this.message = message;
		this.children = children;
	}

	public String getName() {
		return this.name;
	}

	public boolean isValid() {
		return this.isValid;
	}

	public String getMessage() {
		return this.message;
	}

	public ValidationResult[] getChildren() {
		return this.children;
	}

	public ValidationResult get(int index) {
		return this.children != null && this.children.length > index
				? this.children[index]
				: null;
	}

	public ValidationResult get(String name) {
		if (this.children != null && name != null)
			for (int i = 0; i < this.children.length; i++)
				if (name.equalsIgnoreCase(this.children[i].name))
					return this.children[i];
		return null;
	}
}
