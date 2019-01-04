package jports.validations;

import java.util.HashMap;

import jports.reflection.Aspect;
import jports.reflection.AspectMemberAccessor;

public class ValidationAspect<T> extends Aspect<T, ValidationAspectMember<T>> implements Validation {

	private ValidationAspect(Class<T> dataType) {
		super(dataType);
	}

	private static final HashMap<Class<?>, ValidationAspect<?>> INSTANCES = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static final synchronized <T> ValidationAspect<T> getInstance(Class<T> claz) {
		return (ValidationAspect<T>) INSTANCES.computeIfAbsent(claz, ValidationAspect::new);
	}

	@SuppressWarnings("unchecked")
	public ValidationResult validate(String name, Object value) {
		T source = (T) value;
		boolean isValid = true;
		ValidationResult[] children = new ValidationResult[size()];
		for (int i = 0; i < children.length; i++) {
			ValidationAspectMember<T> member = get(i);
			try {
				Object mvalue = member.getValue(source);
				children[i] = member.getValidations().validate(member.getName(), mvalue);
			} catch (Exception e) {
				children[i] = new ValidationResult(member.getName(), false, e.getMessage());
			}
			if (isValid && !children[i].isValid())
				isValid = false;
		}
		return new ValidationResult(
				name,
				isValid,
				isValid
						? null
						: "Some children failed to validate.",
				children);

	}

	@SuppressWarnings("unchecked")
	public boolean isValid(Object value) {
		T item = (T) value;
		for (int i = 0; i < this.size(); i++) {
			ValidationAspectMember<T> member = get(i);
			Object mvalue = member.getValue(item);
			if (!member.getValidations().isValid(mvalue))
				return false;
		}
		return true;
	}

	@Override
	protected ValidationAspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		return new ValidationAspectMember<>(accessor);
	}

}
