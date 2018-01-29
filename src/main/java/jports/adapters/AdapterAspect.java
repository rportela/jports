package jports.adapters;

import java.util.HashMap;

import jports.reflection.Aspect;
import jports.reflection.AspectMemberAccessor;

/**
 * This class wraps the members of an entity with adapters for it's members.
 * 
 * @author rportela
 *
 * @param <TClass>
 */
public class AdapterAspect<TClass> extends Aspect<TClass, AdapterAspectMember<TClass>> {

	/**
	 * A protected constructor so you can extend the class;
	 * 
	 * @param dataType
	 */
	protected AdapterAspect(Class<TClass> dataType) {
		super(dataType);
	}

	/**
	 * Creates a new adapter aspect member if an adapter is found for that member;
	 */
	@Override
	protected AdapterAspectMember<TClass> visit(AspectMemberAccessor<TClass> accessor) {
		Adapter<?> adapter = AdapterFactory.getInstance(accessor.getDataType());
		return adapter == null
				? null
				: new AdapterAspectMember<TClass>(accessor, adapter);
	}

	/**
	 * A static map of instances for semi singleton fast loading;
	 */
	private static final HashMap<Class<?>, AdapterAspect<?>> INSTANCES = new HashMap<Class<?>, AdapterAspect<?>>();

	/**
	 * Gets an aspect based on a class from the instances cache or creates a new one
	 * if none is found;
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized final <T> AdapterAspect<T> getInstance(Class<T> claz) {

		AdapterAspect<T> aspect = (AdapterAspect<T>) INSTANCES.get(claz);
		if (aspect == null) {
			aspect = new AdapterAspect<T>(claz);
			INSTANCES.put(claz, aspect);
		}
		return aspect;
	}
}
