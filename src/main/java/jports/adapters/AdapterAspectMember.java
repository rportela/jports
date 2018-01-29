package jports.adapters;

import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

public class AdapterAspectMember<TClass> extends AspectMember<TClass> {

	public final Adapter<?> adapter;

	public AdapterAspectMember(AspectMemberAccessor<TClass> accessor, Adapter<?> adapter) {
		super(accessor);
		this.adapter = adapter;
	}

	@Override
	public void setValue(TClass target, Object value) {
		Object adapterValue = this.adapter.convert(value);
		super.setValue(target, adapterValue);
	}

}
