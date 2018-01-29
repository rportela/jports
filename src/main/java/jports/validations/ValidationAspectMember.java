package jports.validations;

import jports.reflection.AspectMemberAccessor;
import jports.reflection.AspectMember;

public class ValidationAspectMember<T> extends AspectMember<T> {

	private final ValidationList validations = new ValidationList();

	public ValidationAspectMember(AspectMemberAccessor<T> member) {
		super(member);
	}

	public final ValidationList getValidations() {
		return this.validations;
	}

}
