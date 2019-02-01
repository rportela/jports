package jports.adapters;

import java.util.HashMap;

import jports.reflection.Aspect;
import jports.reflection.AspectMemberAccessor;

/**
 * This class wraps the members of an entity with adapters for it's members.
 * 
 * @author rportela
 *
 * @param <T>
 */
public class AdapterAspect<T> extends Aspect<T, AdapterAspectMember<T>> {

	/**
	 * A protected constructor so you can extend the class;
	 * 
	 * @param dataType
	 */
	protected AdapterAspect(Class<T> dataType) {
		super(dataType);
	}

	/**
	 * Creates a new adapter aspect member if an adapter is found for that member;
	 */
	@Override
	protected AdapterAspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		Adapter<?> adapter = AdapterFactory.createAdapter(accessor.getDataType());
		return adapter == null
				? null
				: new AdapterAspectMember<>(accessor, adapter);
	}

	/**
	 * A static map of instances for semi singleton fast loading;
	 */
	private static final HashMap<Class<?>, AdapterAspect<?>> INSTANCES = new HashMap<>();

	/**
	 * Gets an aspect based on a class from the instances cache or creates a new one
	 * if none is found;
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final synchronized <T> AdapterAspect<T> getInstance(Class<T> claz) {
		return (AdapterAspect<T>) INSTANCES.computeIfAbsent(claz, k -> new AdapterAspect<>(claz));
	}
}
