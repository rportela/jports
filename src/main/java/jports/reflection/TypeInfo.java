package jports.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import jports.ShowStopper;

public class TypeInfo<T> {

	public final Class<T> placeholder = null;

	@SuppressWarnings("unchecked")
	public final Class<T> getGenericClass() {
		try {
			Field field = getClass().getDeclaredField("placeholder");
			return (Class<T>) field.getType();
		} catch (Exception e) {
			throw new ShowStopper(e);
		}
	}

	public final Type getGenericType(int index) {
		try {
			Field field = getClass().getDeclaredField("placeholder");
			ParameterizedType type = (ParameterizedType) field.getGenericType();
			Type[] actualTypeArguments = type.getActualTypeArguments();
			return actualTypeArguments[index];
		} catch (Exception e) {
			throw new ShowStopper(e);
		}
	}

}
