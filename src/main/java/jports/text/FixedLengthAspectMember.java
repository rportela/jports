package jports.text;

import jports.adapters.Adapter;
import jports.adapters.AdapterFactory;
import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

public class FixedLengthAspectMember<T> extends AspectMember<T> {

	private final int start;
	private final int end;
	private final Adapter<?> adapter;

	public FixedLengthAspectMember(
			AspectMemberAccessor<T> accessor,
			int offset,
			FixedLengthColumn column) {
		super(accessor);
		this.start = offset + column.start();
		this.end = offset + column.end();
		this.adapter = AdapterFactory.createAdapter(
				accessor.getDataType(),
				column.adapter(),
				column.pattern());
	}

	public void parseAndApply(String line, T target) {
		String content = line.substring(start, end).trim();
		Object value = adapter.parse(content);
		if (value != null) {
			super.setValue(target, value);
		}
	}

}
