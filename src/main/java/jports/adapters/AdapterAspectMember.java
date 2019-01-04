package jports.adapters;

import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

public class AdapterAspectMember<T> extends AspectMember<T> {

	public final Adapter<?> adapter;

	public AdapterAspectMember(AspectMemberAccessor<T> accessor, Adapter<?> adapter) {
		super(accessor);
		this.adapter = adapter;
	}

	@Override
	public void setValue(T target, Object value) {
		Object adapterValue = this.adapter.convert(value);
		super.setValue(target, adapterValue);
	}

}
