package jports.database;

import jports.data.FilterTerm;
import jports.data.Select;
import jports.data.Storage;

public class DatabaseStorage<T> implements Storage<T> {

	DatabaseAspect<T> aspect;
	Database database;

	public DatabaseStorage(Database database, Class<T> claz) {
		this.database = database;
		this.aspect = new DatabaseAspect<>(claz);
	}

	private FilterTerm createIdentityFilter(T entity) {
		// Sanity check
		if (entity == null)
			return null;

		// Does it have an identity column?
		DatabaseAspectMember<T> identity = aspect.getIdentity();
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

	private DatabaseUpdate createUpdateOnIdentityFor(T entity) {
		FilterTerm idTerm = createIdentityFilter(entity);
		if (idTerm != null) {

		}
	}

	private DatabaseUpdate createUpdateFor(T entity) {

	}

	@Override
	public void save(final T entity) {

	}

	@Override
	public void insert(T entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(T entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Select<T> select() {
		// TODO Auto-generated method stub
		return null;
	}

}
