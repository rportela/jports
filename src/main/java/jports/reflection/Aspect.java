package jports.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class represents an uniform way of interacting with entities in this
 * framework; It easily allows to get and set values to entities based on the
 * member names. It also provides helpful methods to translating entities to and
 * from Maps. It's more useful as a lazy loaded singleton;
 * 
 * @author rportela
 *
 * @param <TClass>
 * @param <TMember>
 */
public abstract class Aspect<TClass, TMember extends AspectMember<TClass>> implements AnnotatedType, Iterable<TMember> {

	/**
	 * Locates a setter based on a name and an expected parameter type;
	 * 
	 * @param methods
	 * @param name
	 * @param paramType
	 * @return
	 */
	private Method findSetter(Method[] methods, String name, Class<?> paramType) {
		for (int i = 0; i < methods.length; i++) {
			Method setter = methods[i];
			int modifiers = setter.getModifiers();
			if (!Modifier.isAbstract(modifiers)
					&& !Modifier.isStatic(modifiers)
					&& !Modifier.isTransient(modifiers)
					&& name.equalsIgnoreCase(setter.getName())
					&& setter.getReturnType().equals(Void.class)) {
				Class<?>[] parameterTypes = setter.getParameterTypes();
				if (parameterTypes != null
						&& parameterTypes.length == 1
						&& parameterTypes[0].equals(paramType))
					return setter;
			}
		}
		return null;
	}

	/**
	 * Holds a list of members of this class;
	 */
	private final ArrayList<TMember> members;

	/**
	 * The actual class definition;
	 */
	private final Class<TClass> dataType;

	/**
	 * Tells the aspect that it should inspect fields;
	 * 
	 * @return
	 */
	protected boolean canHaveFields() {
		return true;
	}

	/**
	 * Tells the aspect that it should inspect getters and setters as properties;
	 * 
	 * @return
	 */
	protected boolean canHaveProperties() {
		return true;
	}

	/**
	 * Constructs a new aspect based on a given Class.
	 * 
	 * @param dataType
	 */
	protected Aspect(Class<TClass> dataType) {

		this.dataType = dataType;

		final Field[] fields = dataType.getFields();
		final Method[] methods = dataType.getMethods();
		AspectMemberAccessor<TClass> accessor;
		TMember member;
		int modifiers;

		this.members = new ArrayList<TMember>(fields.length
				+ methods.length);

		if (this.canHaveFields())
			for (int i = 0; i < fields.length; i++) {
				modifiers = fields[i].getModifiers();
				if (!Modifier.isStatic(modifiers)
						&& !Modifier.isTransient(modifiers)) {
					accessor = new AspectMemberField<TClass>(this, fields[i]);
					member = this.visit(accessor);
					if (member != null)
						members.add(member);
				}
			}

		if (this.canHaveProperties())
			for (int i = 0; i < methods.length; i++) {
				Method getter = methods[i];
				modifiers = getter.getModifiers();
				if (!Modifier.isStatic(modifiers)
						&& !Modifier.isTransient(modifiers)
						&& !Modifier.isAbstract(modifiers)) {
					String name = getter.getName();
					if (name.startsWith("get")) {
						Class<?>[] parameterTypes = getter.getParameterTypes();
						if (parameterTypes == null
								|| parameterTypes.length == 0) {
							name = name.substring(4);
							Method setter = findSetter(methods, "set"
									+ name, getter.getReturnType());
							accessor = new AspectMemberProperty<TClass>(this, name, getter, setter);
							member = this.visit(accessor);
							if (member != null)
								members.add(member);
						}
					}
				}
			}

		this.members.trimToSize();

	}

	/**
	 * This method allows the implementer to define custom members or return null if
	 * no member based on the given field should be added to the member list of this
	 * aspect;
	 * 
	 * @param field
	 * @return
	 */
	protected abstract TMember visit(AspectMemberAccessor<TClass> accessor);

	/**
	 * The expected type of entity in this aspect;
	 * 
	 * @return
	 */
	public final Class<TClass> getDataType() {
		return this.dataType;
	}

	/**
	 * The name of the entities in this aspect;
	 * 
	 * @return
	 */
	public String getName() {
		return this.dataType.getSimpleName();
	}

	/**
	 * The number of members associated with this aspect;
	 * 
	 * @return
	 */
	public int size() {
		return this.members.size();
	}

	/**
	 * Gets a member by it's index position in the list of members;
	 * 
	 * @param index
	 * @return
	 */
	public TMember get(int index) {
		return this.members.get(index);
	}

	/**
	 * Gets the index (ordinal position) of a given name in the list of members of
	 * this aspect;
	 * 
	 * @param name
	 * @return
	 */
	public int indexOf(String name) {
		if (name != null)
			for (int i = 0; i < this.members.size(); i++)
				if (name.equalsIgnoreCase(members.get(i).getName()))
					return i;
		return -1;
	}

	/**
	 * Gets a member by it's name;
	 * 
	 * @param name
	 * @return
	 */
	public TMember get(String name) {
		int index = this.indexOf(name);
		if (index < 0)
			throw new RuntimeException(name
					+ " is not a member of "
					+ this.dataType);
		else
			return this.members.get(index);
	}

	/**
	 * Gets a value from a source entity using the member at a specific index
	 * position on this aspect;
	 * 
	 * @param source
	 * @param index
	 * @return
	 */
	public Object getValue(TClass source, int index) {
		return this.members.get(index).getValue(source);
	}

	/**
	 * Gets the value from a source entity using the name of a specific member in
	 * this aspect;
	 * 
	 * @param source
	 * @param name
	 * @return
	 */
	public Object getValue(TClass source, String name) {
		return this.get(name).getValue(source);
	}

	/**
	 * Sets the value on a target entity using the member at a specific index
	 * position in this aspect;
	 * 
	 * @param target
	 * @param index
	 * @param value
	 */
	public void setValue(TClass target, int index, Object value) {
		this.members.get(index).setValue(target, value);
	}

	/**
	 * Sets the value on a target entity using the member with a specific name;
	 * 
	 * @param target
	 * @param name
	 * @param value
	 */
	public void setValue(TClass target, String name, Object value) {
		this.get(name).setValue(target, value);
	}

	/**
	 * Gets the values from a source entity and puts them using the member names in
	 * a specific map;
	 * 
	 * @param source
	 * @param target
	 */
	public void getValues(TClass source, Map<String, Object> target) {
		for (TMember member : this.members)
			target.put(member.getName(), member.getValue(source));
	}

	/**
	 * Gets the values from a source entity and use the member names to build a map;
	 * 
	 * @param entity
	 * @return
	 */
	public Map<String, Object> toMap(final TClass entity) {
		final LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		getValues(entity, map);
		return map;
	}

	/**
	 * Builds an entity of the data type specified in this Aspect (an empty
	 * constructor is expected) and populates the members with values from a map
	 * indexed by their names;
	 * 
	 * @param map
	 * @return
	 */
	public TClass fromMap(final Map<String, Object> map) {
		try {
			TClass entity = this.dataType.newInstance();
			setValues(entity, map);
			return entity;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets the values on a target entity using a map and indexing aspect members by
	 * their names;
	 * 
	 * @param target
	 * @param values
	 */
	public void setValues(final TClass target, final Map<String, Object> values) {
		for (Entry<String, Object> entry : values.entrySet()) {
			get(entry.getKey()).setValue(target, entry.getValue());
		}
	}

	/**
	 * Returns this element's annotation for the specified type if such an
	 * annotation is present, else null.
	 */
	public final <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return this.dataType.getAnnotation(annotationClass);
	}

	/**
	 * Returns annotations that are present on this element. If there are no
	 * annotations present on this element, the return value is an array of length
	 * 0. The caller of this method is free to modify the returned array; it will
	 * have no effect on the arrays returned to other callers.
	 */
	public Annotation[] getAnnotations() {
		return this.dataType.getAnnotations();
	}

	/**
	 * Returns this element's annotation for the specified type if such an
	 * annotation is directly present, else null. This method ignores inherited
	 * annotations. (Returns null if no annotations are directly present on this
	 * element.)
	 */
	public Annotation[] getDeclaredAnnotations() {
		return this.dataType.getDeclaredAnnotations();
	}

	/**
	 * Gets the generic super class of this aspect;
	 */
	public Type getType() {
		return this.dataType.getGenericSuperclass();
	}

	/**
	 * Gets an iterator for members of this aspect;
	 */
	public Iterator<TMember> iterator() {
		return this.members.iterator();
	}

}
