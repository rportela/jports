package jports.database;

import java.util.HashMap;

import jports.data.DataAspect;
import jports.data.DataAspectMember;
import jports.reflection.AspectMemberAccessor;

public class DatabaseAspect<T> extends DataAspect<T, DataAspectMember<T>> {

	private String database_object;

	private DatabaseAspect(Class<T> dataType) {
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
				: new DataAspectMember<>(accessor, col.type(), col.name(), null, null);
	}

	public String getDatabaseObject() {
		return this.database_object;
	}

	public void setDatabaseObject(String value) {
		this.database_object = value;
	}

	private static final HashMap<Class<?>, DatabaseAspect<?>> INSTANCES;
	static {
		INSTANCES = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public static synchronized final <T> DatabaseAspect<T> getInstance(Class<T> dataType) {
		DatabaseAspect<T> aspect = (DatabaseAspect<T>) INSTANCES.get(dataType);
		if (aspect == null) {
			aspect = new DatabaseAspect<>(dataType);
			INSTANCES.put(dataType, aspect);
		}
		return aspect;
	}
}
