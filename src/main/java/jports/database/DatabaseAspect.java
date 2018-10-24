package jports.database;

import jports.data.DataAspect;
import jports.data.FilterTerm;
import jports.reflection.AspectMemberAccessor;

public class DatabaseAspect<TClass> extends DataAspect<TClass, DatabaseAspectMember<TClass>> {

	private final String objectName;

	protected DatabaseAspect(Class<TClass> dataType) {
		super(dataType);
		DatabaseObject anno = dataType.getAnnotation(DatabaseObject.class);
		this.objectName = anno.value().isEmpty()
				? dataType.getSimpleName()
				: anno.value();
	}

	public String getObjectName() {
		return objectName;
	}

	@Override
	protected DatabaseAspectMember<TClass> visit(AspectMemberAccessor<TClass> accessor) {
		DatabaseColumn col = accessor.getAnnotation(DatabaseColumn.class);
		return col == null
				? null
				: new DatabaseAspectMember<>(accessor, col);
	}

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

}
