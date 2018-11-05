package jports.data;

import jports.data.DataAspect;
import jports.data.DataAspectMember;
import jports.data.DataColumn;
import jports.reflection.AspectMemberAccessor;

public class ListAspect<TClass> extends DataAspect<TClass, DataAspectMember<TClass>> {

	protected ListAspect(Class<TClass> dataType) {
		super(dataType);
	}

	@Override
	protected DataAspectMember<TClass> visit(AspectMemberAccessor<TClass> accessor) {
		DataColumn dcol = accessor.getAnnotation(DataColumn.class);
		return dcol == null
				? new DataAspectMember<>(accessor)
				: new DataAspectMember<>(accessor, dcol);
	}

}
