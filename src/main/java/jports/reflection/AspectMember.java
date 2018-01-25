package jports.reflection;

/**
 * This class represents an aspect member that's usually a wrapper for a Field
 * or a Method. It enables implementers to get and set values onto entities and
 * to have a name;
 * 
 * @author rportela
 *
 * @param <TClass>
 */
public interface AspectMember<TClass> {

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
	public Aspect<TClass, ?> getAspect();

	/**
	 * Gets the value of this member from a specific source entity;
	 * 
	 * @param source
	 * @return
	 */
	public Object getValue(TClass source);

	/**
	 * Sets the value of this member onto a specific target entity;
	 * 
	 * @param target
	 * @param value
	 */
	public void setValue(TClass target, Object value);

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
