package jports.text;

import jports.adapters.Adapter;
import jports.adapters.AdapterFactory;
import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

public class CsvAspectMember<T> extends AspectMember<T> {

	public final String columnName;
	private int position;
	public final Adapter<?> adapter;

	public CsvAspectMember(AspectMemberAccessor<T> accessor, CsvColumn csv, int index) {
		super(accessor);
		columnName = csv.name().isEmpty()
				? accessor.getName()
				: csv.name();
		position = csv.position() >= 0
				? csv.position()
				: index;
		adapter = AdapterFactory.createAdapter(accessor.getDataType(), csv.adapter(), csv.pattern());
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int value) {
		position = value;
	}

	public void parseAndApply(String[] source, T entity) {
		if (position < 0 || position >= source.length)
			return;

		String text = source[position];

		// remove quotes
		if (text != null &&
				text.length() > 1 &&
				text.charAt(0) == '"' &&
				text.charAt(text.length() - 1) == '"') {
			text = text.substring(1, text.length() - 1);
		}

		Object value = adapter.parse(text);
		super.setValue(entity, value);
	}

	public void formatAndApply(T source, String[] target) {
		if (position < 0 || position >= target.length)
			return;
		Object value = super.getValue(source);
		String text = adapter.formatObject(value);
		target[position] = text;
	}

	public void setPositionFrom(String[] colNames) {
		int pos = -1;
		for (int i = 0; i < colNames.length && pos < 0; i++) {
			if (colNames[i].equalsIgnoreCase(columnName))
				pos = i;
		}
		this.position = pos;
	}

}
