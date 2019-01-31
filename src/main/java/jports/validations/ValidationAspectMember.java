package jports.validations;

import java.lang.annotation.Annotation;

import jports.ShowStopper;
import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

public class ValidationAspectMember<T> extends AspectMember<T> {

	private final ValidationList validations = new ValidationList();

	public ValidationAspectMember(AspectMemberAccessor<T> member) {
		super(member);

		Annotation[] annotations = member.getAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			Class<? extends Annotation> annotationType = annotations[i].annotationType();

			ValidationImplementation implementation = annotationType.getAnnotation(ValidationImplementation.class);

			if (implementation != null) {
				try {
					Object instance = implementation
							.value()
							.getConstructor(annotationType)
							.newInstance(annotations[i]);
					this.validations.add((Validation) instance);
				} catch (Exception e) {
					throw new ShowStopper(e);
				}
			}
		}

	}

	public final ValidationList getValidations() {
		return this.validations;
	}

}
