package jports.database;

import java.util.HashMap;

import jports.data.DataAspect;
import jports.data.FilterTerm;
import jports.reflection.AspectMemberAccessor;

/**
 * This class represents a database aspect. An aspect that can map fields and
 * properties to database columns;
 * 
 * @author rportela
 *
 * @param <TClass>
 */
public class DatabaseAspect<TClass> extends DataAspect<TClass, DatabaseAspectMember<TClass>> {

	/**
	 * The database object name;
	 */
	private final String objectName;

	/**
	 * Creates a new instance of the database aspect;
	 * 
	 * @param dataType
	 */
	private DatabaseAspect(Class<TClass> dataType) {
		super(dataType);
		DatabaseObject anno = dataType.getAnnotation(DatabaseObject.class);
		this.objectName = anno.value().isEmpty()
				? dataType.getSimpleName()
				: anno.value();
	}

	/**
	 * Gets the object name. Usually a table;
	 * 
	 * @return
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * Creates database aspect members if they are correctly annotated;
	 */
	@Override
	protected DatabaseAspectMember<TClass> visit(AspectMemberAccessor<TClass> accessor) {
		DatabaseColumn col = accessor.getAnnotation(DatabaseColumn.class);
		return col == null
				? null
				: new DatabaseAspectMember<>(accessor, col);
	}

	/**
	 * Creates an identity filter for queries;
	 * 
	 * @param entity
	 * @return
	 */
	public FilterTerm createIdentityFilter(final TClass entity) {
		// Does it have an identity column?
		DatabaseAspectMember<TClass> identity = getIdentity();
		if (identity == null)
			return null;

		// Is the identity value good?
		Object id = identity.getValue(entity);
		if (id == null)
			return null;
		else if (Number.class.isAssignableFrom(id.getClass()) && ((Number) id).longValue() == 0L)
			return null;
		else if (id instanceof String && ((String) id).isEmpty())
			return null;
		else
			return new FilterTerm(identity.getColumnName(), id);
	}

	/**
	 * The static map of database aspect instances to cache their instantiation;
	 */
	private static final HashMap<Class<?>, DatabaseAspect<?>> INSTANCES = new HashMap<>();

	/*
	 * Gets a specific database aspect instance by a class definition;
	 */
	@SuppressWarnings("unchecked")
	public static synchronized final <T> DatabaseAspect<T> getInstance(final Class<T> claz) {
		DatabaseAspect<T> aspect = (DatabaseAspect<T>) INSTANCES.get(claz);
		if (aspect == null) {
			aspect = new DatabaseAspect<>(claz);
			INSTANCES.put(claz, aspect);
		}
		return aspect;
	}
}
