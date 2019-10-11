package jports.reflection;

import java.util.HashMap;

public class DefaultAspect<T> extends Aspect<T, AspectMember<T>> {

	public DefaultAspect(Class<T> claz) {
		super(claz);
	}

	@Override
	protected AspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		return new AspectMember<>(accessor);
	}

	private static final HashMap<Class<?>, DefaultAspect<?>> INSTANCES = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static final synchronized <T> DefaultAspect<T> getInstance(Class<T> claz) {
		DefaultAspect<T> aspect = (DefaultAspect<T>) INSTANCES.get(claz);
		if (aspect == null) {
			aspect = new DefaultAspect<T>(claz);
			INSTANCES.put(claz, aspect);
		}
		return aspect;
	}
}
