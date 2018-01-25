package jports.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class AspectField<T> implements AspectMember<T> {

	public final Aspect<T, ?> aspect;
	public final Field field;

	public AspectField(Aspect<T, ?> aspect, Field field) {
		this.aspect = aspect;
		this.field = field;
	}

	public final Aspect<T, ?> getAspect() {
		return this.aspect;
	}

	public Object getValue(T source) {
		try {
			return this.field.get(source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setValue(T target, Object value) {
		try {
			this.field.set(target, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Class<?> getDataType() {
		return this.field.getType();
	}

	public String getName() {
		return this.field.getName();
	}

	public boolean isReadOnly() {
		return Modifier.isFinal(this.field.getModifiers());
	}
}
