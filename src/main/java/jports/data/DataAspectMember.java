package jports.data;

import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

public class DataAspectMember<TClass> extends AspectMember<TClass> implements Column {

	private final ColumnType column_type;
	private final String column_name;

	public DataAspectMember(AspectMemberAccessor<TClass> accessor, ColumnType column_type, String column_name) {
		super(accessor);
		this.column_type = column_type == null ?
				ColumnType.REGULAR :
				column_type;
		this.column_name = column_name == null || column_name.isEmpty() ?
				accessor.getName() :
				column_name;
	}

	public DataAspectMember(AspectMemberAccessor<TClass> accessor, DataColumn column) {
		this(accessor, column.type(), column.name());
	}

	public DataAspectMember(AspectMemberAccessor<TClass> accessor) {
		this(accessor, null, null);
	}

	@Override
	public String getColumnName() {
		return this.column_name;
	}

	@Override
	public ColumnType getColumnType() {
		return this.column_type;
	}

}
