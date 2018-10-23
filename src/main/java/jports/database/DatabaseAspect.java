package jports.database;

import jports.reflection.Aspect;
import jports.reflection.AspectMemberAccessor;

public class DatabaseAspect<TClass> extends Aspect<TClass, DatabaseAspectMember<TClass>> {

	private final String objectName;

	protected DatabaseAspect(Class<TClass> dataType) {
		super(dataType);
		DatabaseObject anno = dataType.getAnnotation(DatabaseObject.class);
		this.objectName = anno.value().isEmpty() ? dataType.getSimpleName() : anno.value();
	}

	public String getObjectName() {
		return objectName;
	}

	@Override
	protected DatabaseAspectMember<TClass> visit(AspectMemberAccessor<TClass> accessor) {
		DatabaseColumn col = accessor.getAnnotation(DatabaseColumn.class);
		return col == null ? null : new DatabaseAspectMember<>(accessor, col);
	}

}
