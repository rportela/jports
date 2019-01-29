package jports.adapters;

import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

public class AdapterAspectMember<T> extends AspectMember<T> {

	private final Adapter<?> adapter;

	public AdapterAspectMember(AspectMemberAccessor<T> accessor, Adapter<?> adapter) {
		super(accessor);
		this.adapter = adapter;
	}

	public final Adapter<?> getAdapter() {
		return this.adapter;
	}

	@Override
	public void setValue(T target, Object value) {
		Object adapterValue = this.adapter.convert(value);
		super.setValue(target, adapterValue);
	}

	public void parseAndSet(T target, String value) {
		Object adapterValue = this.adapter.parse(value);
		if (adapterValue != null) {
			super.setValue(target, adapterValue);
		}
	}

}
