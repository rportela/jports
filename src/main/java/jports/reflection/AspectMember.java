package jports.reflection;

import java.lang.annotation.Annotation;

public class AspectMember<T> implements AspectMemberAccessor<T> {

	private final AspectMemberAccessor<T> accessor;

	public AspectMember(AspectMemberAccessor<T> accessor) {
		this.accessor = accessor;
	}

	public boolean isReadOnly() {
		return this.accessor.isReadOnly();
	}

	public Aspect<T, ?> getAspect() {
		return this.accessor.getAspect();
	}

	public Object getValue(T source) {
		return this.accessor.getValue(source);
	}

	public void setValue(T target, Object value) {
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

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return this.accessor.getAnnotation(annotationClass);
	}

	public Annotation[] getAnnotations() {
		return this.accessor.getAnnotations();
	}

	public Annotation[] getDeclaredAnnotations() {
		return this.accessor.getDeclaredAnnotations();
	}
}
