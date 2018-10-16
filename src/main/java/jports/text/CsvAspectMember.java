package jports.text;

import jports.adapters.Adapter;
import jports.adapters.AdapterFactory;
import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

public class CsvAspectMember<TClass> extends AspectMember<TClass> {

	public final String columnName;
	private int position;
	public final Adapter<?> adapter;

	public CsvAspectMember(AspectMemberAccessor<TClass> accessor, CsvColumn csv, int index) {
		super(accessor);
		columnName = csv.name().isEmpty() ? accessor.getName() : csv.name();
		position = csv.position() >= 0 ? csv.position() : index;
		adapter = AdapterFactory.createAdapter(accessor.getDataType(), csv.adapter(), csv.pattern());
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int value) {
		position = value;
	}

	public void parseAndApply(String[] source, TClass entity) {
		if (position < 0 || position >= source.length)
			return;
		String text = source[position];
		Object value = adapter.parse(text);
		super.setValue(entity, value);
	}

	public void formatAndApply(TClass source, String[] target) {
		if (position < 0 || position >= target.length)
			return;
		Object value = super.getValue(source);
		String text = adapter.formatObject(value);
		target[position] = text;
	}

	public void setPositionFrom(String[] colNames) {
		position = -1;
		for (int i = 0; i < colNames.length && position < 0; i++) {
			if (colNames[i].equalsIgnoreCase(columnName))
				position = i;
		}
	}

}
