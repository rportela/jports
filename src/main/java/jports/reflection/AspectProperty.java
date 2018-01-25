package jports.reflection;

import java.lang.reflect.Method;

/**
 * An aspect member that contains a getter and a setter for reading and writing
 * values to an entity; The return type of the getDatType method is the return
 * type of the getter;
 * 
 * @author rportela
 *
 * @param <T>
 */
public class AspectProperty<T> implements AspectMember<T> {

	/**
	 * The aspect associated with this property;
	 */
	public final Aspect<T, ?> aspect;

	/**
	 * The method that will be invoked for getting values from an entity;
	 */
	public final Method getter;

	/**
	 * The method that will be invoked for setting values onto an entity;
	 */
	public final Method setter;

	/**
	 * The name of the property;
	 */
	public final String name;

	/**
	 * Creates a new aspect property instance;
	 * 
	 * @param aspect
	 * @param getter
	 * @param setter
	 */
	public AspectProperty(Aspect<T, ?> aspect, String name, Method getter, Method setter) {
		this.aspect = aspect;
		this.getter = getter;
		this.setter = setter;
		this.name = name;
	}

	/**
	 * Gets the aspect associated with the getter and setter of this instance;
	 */
	public Aspect<T, ?> getAspect() {
		return this.aspect;
	}

	/**
	 * Gets a value from an entity by invoking the getter method of this aspect
	 * property;
	 */
	public Object getValue(T source) {
		try {
			return this.getter.invoke(source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the value from an entity by invoking the setter method of this aspect
	 * property;
	 */
	public void setValue(T target, Object value) {
		try {
			this.setter.invoke(target, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the returned data type of this property -commonly the return type of the
	 * getter method;
	 */
	public Class<?> getDataType() {
		return this.getter.getReturnType();
	}

	/**
	 * Gets the name of this property;
	 */
	public String getName() {
		return this.name;
	}
}
