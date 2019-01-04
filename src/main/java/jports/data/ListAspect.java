package jports.data;

import jports.reflection.AspectMemberAccessor;

public class ListAspect<T> extends DataAspect<T, DataAspectMember<T>> {

	protected ListAspect(Class<T> dataType) {
		super(dataType);
	}

	@Override
	protected DataAspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		DataColumn dcol = accessor.getAnnotation(DataColumn.class);
		return dcol == null
				? new DataAspectMember<>(accessor)
				: new DataAspectMember<>(accessor, dcol);
	}

}
