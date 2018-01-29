package jports.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class AspectMemberField<T> implements AspectMemberAccessor<T> {

	public final Aspect<T, ?> aspect;
	public final Field field;

	public AspectMemberField(Aspect<T, ?> aspect, Field field) {
		this.aspect = aspect;
		this.field = field;
	}

	public final Aspect<T, ?> getAspect() {
		return this.aspect;
	}

	public final Object getValue(T source) {
		try {
			return this.field.get(source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public final void setValue(T target, Object value) {
		try {
			this.field.set(target, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public final Class<?> getDataType() {
		return this.field.getType();
	}

	public final String getName() {
		return this.field.getName();
	}

	public final boolean isReadOnly() {
		return Modifier.isFinal(this.field.getModifiers());
	}

	public <G extends Annotation> G getAnnotation(Class<G> annotationClass) {
		return this.field.getAnnotation(annotationClass);
	}

	public final Annotation[] getAnnotations() {
		return this.field.getAnnotations();
	}

	public final Annotation[] getDeclaredAnnotations() {
		return this.field.getDeclaredAnnotations();
	}
}
