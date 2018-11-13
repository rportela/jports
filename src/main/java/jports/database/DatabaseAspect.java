package jports.database;

import jports.data.DataAspect;
import jports.data.DataAspectMember;
import jports.reflection.AspectMemberAccessor;

public class DatabaseAspect<T> extends DataAspect<T, DataAspectMember<T>> {

	private String database_object;

	public DatabaseAspect(Class<T> dataType) {
		super(dataType);

		DatabaseObject dbo = dataType.getAnnotation(DatabaseObject.class);
		database_object = dbo == null || dbo.value().isEmpty()
				? dataType.getSimpleName()
				: dbo.value();
	}

	@Override
	protected DataAspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		DatabaseColumn col = accessor.getAnnotation(DatabaseColumn.class);
		return col == null
				? null
				: new DataAspectMember<>(accessor, col.type(), col.name());
	}

	public String getDatabaseObject() {
		return this.database_object;
	}

	public void setDatabaseObject(String value) {
		this.database_object = value;
	}

}
