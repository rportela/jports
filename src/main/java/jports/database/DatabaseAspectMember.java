package jports.database;

import jports.data.ColumnType;
import jports.reflection.AspectMember;
import jports.reflection.AspectMemberAccessor;

public class DatabaseAspectMember<TClass> extends AspectMember<TClass> {

	private final String columnName;
	private final ColumnType columnType;

	public DatabaseAspectMember(AspectMemberAccessor<TClass> accessor, DatabaseColumn column) {
		super(accessor);
		this.columnName = column.name().isEmpty()
				? accessor.getName()
				: column.name();
		this.columnType = column.type();
	}

	public String getColumnName() {
		return this.columnName;
	}

	public ColumnType getColumnType() {
		return this.columnType;
	}

}
