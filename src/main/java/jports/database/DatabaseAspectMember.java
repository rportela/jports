package jports.database;

import jports.data.DataAspectMember;
import jports.reflection.AspectMemberAccessor;

public class DatabaseAspectMember<TClass> extends DataAspectMember<TClass> {

	public DatabaseAspectMember(AspectMemberAccessor<TClass> accessor, DatabaseColumn column) {
		super(accessor, column.type(), column.name());
	}

}
