package jports.reflection;

public class DefaultAspect<T> extends Aspect<T, AspectMember<T>> {

	public DefaultAspect(Class<T> claz) {
		super(claz);
	}

	@Override
	protected AspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		return new AspectMember<>(accessor);
	}

}
