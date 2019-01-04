package jports.reflection;

import java.lang.reflect.AnnotatedElement;

/**
 * This class represents an aspect member that's usually a wrapper for a Field
 * or a Method. It enables implementers to get and set values onto entities and
 * to have a name;
 * 
 * @author rportela
 *
 * @param <T>
 */
public interface AspectMemberAccessor<T> extends AnnotatedElement {

	/**
	 * This aspect member is read only;
	 * 
	 * @return
	 */
	public boolean isReadOnly();

	/**
	 * Gets the owning aspect of this member;
	 * 
	 * @return
	 */
	public Aspect<T, ?> getAspect();

	/**
	 * Gets the value of this member from a specific source entity;
	 * 
	 * @param source
	 * @return
	 */
	public Object getValue(T source);

	/**
	 * Sets the value of this member onto a specific target entity;
	 * 
	 * @param target
	 * @param value
	 */
	public void setValue(T target, Object value);

	/**
	 * Gets the data type of this member;
	 * 
	 * @return
	 */
	public Class<?> getDataType();

	/**
	 * Gets the name of this member;
	 * 
	 * @return
	 */
	public String getName();
}
