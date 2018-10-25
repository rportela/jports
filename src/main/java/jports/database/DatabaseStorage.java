package jports.database;

import java.util.List;

import jports.data.ColumnType;
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

	private DatabaseUpdate beginUpdate(T entity) {

	}

	private DatabaseUpdate createUpdateOnIdentityFor(T entity) {
		FilterTerm idTerm = aspect.createIdentityFilter(entity);
		if (idTerm != null) {
			return 
		}
	}

	private DatabaseUpdate createUpdateByColumn(T entity, DatabaseAspectMember<T> column, Object value) {
		DatabaseUpdate update = this.database.update(aspect.getObjectName());
		for (DatabaseAspectMember<T> dam : aspect) {
			if (column.equals(dam))
				continue;
			if (dam.getColumnType() != ColumnType.IDENTITY) {
				String name = dam.getColumnName();
				Object colValue = dam.getValue(entity);
				update.set(name, colValue);
			}
		}
		update.where(column.getColumnName(), value);
		return update;
	}

	private DatabaseUpdate createUpdateByCompositeKey(T entity) {
		DatabaseUpdate update = this.database.update(aspect.getObjectName());
		for (DatabaseAspectMember<T> dam : aspect) {
			if (column.equals(dam))
				continue;
			if (dam.getColumnType() != ColumnType.IDENTITY) {
				String name = dam.getColumnName();
				Object value = dam.getValue(entity);
				update.set(name, value);
			}
		}
		update.where(column.getColumnName(), value);
		return update;
	}

	@Override
	public void save(final T entity) {

	}

	@Override
	public void insert(T entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public int delete(T entity) {
		DatabaseAspectMember<T> identity = aspect.getIdentity();
		if (identity != null) {
			Object id = identity.getValue(entity);
			if (id != null &&
					!((id instanceof Number) && ((Number) id).longValue() == 0L)) {
				return database
						.delete(aspect.getObjectName())
						.where(identity.getColumnName(), id)
						.execute();
			}
		}

		for (DatabaseAspectMember<T> unique : aspect.getUniqueColumns()) {
			Object uniqueValue = unique.getValue(entity);
			if (uniqueValue != null) {
				int uniqueResult = database
						.delete(aspect.getObjectName())
						.where(unique.getColumnName(), uniqueValue)
						.execute();

				if (uniqueResult != 0)
					return uniqueResult;
			}
		}

		List<DatabaseAspectMember<T>> compositeKey = aspect.getCompositeKey();
		if (!compositeKey.isEmpty()) {
			DatabaseDelete delete = database.delete(aspect.getObjectName());
			for (DatabaseAspectMember<T> ck : compositeKey) {
				delete.andWhere(ck.getColumnName(), ck.getValue(entity));
			}
			return delete.execute();
		} else
			return 0;

	}

	@Override
	public int update(T entity) {
		DatabaseAspectMember<T> identity = aspect.getIdentity();
		if (identity != null) {
			Object id = identity.getValue(entity);
			if (id != null &&
					!((id instanceof Number) && ((Number) id).longValue() == 0L)) {
				return createUpdateByColumn(entity, identity, id).execute();
			}
		}

	}

	@Override
	public Select<T> select() {
		// TODO Auto-generated method stub
		return null;
	}

}
