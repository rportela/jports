package jports.database;

import java.util.HashMap;

import jports.data.DataAspect;
import jports.data.DataAspectMember;
import jports.reflection.AspectMemberAccessor;

public class DatabaseAspect<T> extends DataAspect<T, DataAspectMember<T>> {

	private String databaseObject;

	private DatabaseAspect(Class<T> dataType) {
		super(dataType);

		DatabaseObject dbo = dataType.getAnnotation(DatabaseObject.class);
		databaseObject = dbo == null || dbo.value().isEmpty()
				? dataType.getSimpleName()
				: dbo.value();
	}

	@Override
	protected DataAspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		DatabaseColumn col = accessor.getAnnotation(DatabaseColumn.class);
		return col == null
				? null
				: new DataAspectMember<>(accessor, col.type(), col.name(), null, null, col.readOnly());
	}

	public String getDatabaseObject() {
		return this.databaseObject;
	}

	public void setDatabaseObject(String value) {
		this.databaseObject = value;
	}

	private static final HashMap<Class<?>, DatabaseAspect<?>> INSTANCES;
	static {
		INSTANCES = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public static final synchronized <T> DatabaseAspect<T> getInstance(Class<T> dataType) {
		return (DatabaseAspect<T>) INSTANCES.computeIfAbsent(dataType, DatabaseAspect::new);
	}
}
