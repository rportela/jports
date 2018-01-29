package jports.reflection;

import java.lang.annotation.Annotation;

public class AspectMember<TClass> implements AspectMemberAccessor<TClass> {

	private final AspectMemberAccessor<TClass> accessor;

	public AspectMember(AspectMemberAccessor<TClass> accessor) {
		this.accessor = accessor;
	}

	public boolean isReadOnly() {
		return this.accessor.isReadOnly();
	}

	public Aspect<TClass, ?> getAspect() {
		return this.accessor.getAspect();
	}

	public Object getValue(TClass source) {
		return this.accessor.getValue(source);
	}

	public void setValue(TClass target, Object value) {
		this.accessor.setValue(target, value);
	}

	public Class<?> getDataType() {
		return this.accessor.getDataType();
	}

	public String getName() {
		return this.accessor.getName();
	}

	@Override
	public String toString() {
		return this.accessor.toString();
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return this.accessor.getAnnotation(annotationClass);
	}

	public Annotation[] getAnnotations() {
		return this.accessor.getAnnotations();
	}

	public Annotation[] getDeclaredAnnotations() {
		return this.accessor.getDeclaredAnnotations();
	}
}
