package jports.data;

import java.util.List;

/**
 * This is the abstract data storage class with methods than can handle saving,
 * deleting, updating and inserting entities to a back end storage;
 * 
 * @author rportela
 *
 * @param <T>
 */
public abstract class DataStorage<T> implements Storage<T> {

	/**
	 * Gets the data aspect with the annotated members of the data type stored by
	 * this instance;
	 * 
	 * @return
	 */
	public abstract DataAspect<T, ?> getAspect();

	/**
	 * When implemented should create an insert command capable of modifying the
	 * back end storage;
	 * 
	 * @return
	 */
	public abstract Insert createInsert();

	/**
	 * When implemented should create a delete command capable of modifying the back
	 * end storage;
	 * 
	 * @return
	 */
	public abstract Delete createDelete();

	/**
	 * When implemented should create an update command capable of modifying the
	 * back end storage;
	 * 
	 * @return
	 */
	public abstract Update createUpdate();

	/**
	 * When implemented should create a select command capable of retrieving records
	 * from the back end storage;
	 */
	public abstract Select<T> select();

	/**
	 * When implemented should create an upsert command capable of detecting
	 * conflicts on the insert command and doing an update instead;
	 * 
	 * @return
	 */
	public abstract Upsert createUpsert();

	/**
	 * This method attempts to update an entity if no records were affected, it
	 * inserts the entity; This method returns the number of records affected by the
	 * update or by the insert command;
	 */
	@Override
	public int save(final T entity) {
		int upd = update(entity);
		if (upd <= 0)
			upd = insert(entity);
		return upd;
	}

	/**
	 * This method saves a collection of entities and returns the sum of each save
	 * operation's number of records affected;
	 * 
	 * @param entities
	 * @return
	 */
	public int save(final Iterable<T> entities) {
		int i = 0;
		for (T e : entities)
			i += save(e);
		return i;
	}

	/**
	 * This method saves an array of entities and returns the sum of each save
	 * operation's number of records affected;
	 * 
	 * @param entities
	 * @return
	 */
	public int save(final T[] entities) {
		int c = 0;
		for (int i = 0; i < entities.length; i++)
			c += save(entities[i]);
		return c;
	}

	/**
	 * This methods inserts an entity and returns the number of records affected by
	 * the insert command;
	 */
	@Override
	public int insert(final T entity) {
		Insert insert = createInsert();
		for (DataAspectMember<T> member : getAspect()) {
			if (member.getColumnType() != ColumnType.IDENTITY) {
				insert.add(member.getColumnName(), member.getValue(entity));
			}
		}
		return insert.execute();
	}

	/**
	 * This method inserts a collection of entities and returns the number of
	 * records affected as the sum of each insert command's number of records
	 * affected;
	 * 
	 * @param entities
	 * @return
	 */
	public int insert(final Iterable<T> entities) {
		int c = 0;
		for (T child : entities)
			c += insert(child);
		return c;
	}

	/**
	 * This method inserts an array of entities and returns the number of records
	 * affected as the sum of each insert command's number of records affected;
	 * 
	 * @param entities
	 * @return
	 */
	public int insert(final T[] entities) {
		int c = 0;
		for (int i = 0; i < entities.length; i++)
			c += insert(entities[i]);
		return c;
	}

	/**
	 * This method attempts to delete an entity by it's annotated identity; If no
	 * identity was annotated or the identity value extracted from the entity is
	 * null or equal to Zero; -1 is returned. Otherwise it returns the number of
	 * records affected by the delete command;
	 * 
	 * @param entity
	 * @return
	 */
	public int deleteByIdentity(final T entity) {
		final DataAspectMember<T> identity = getAspect().getIdentity();
		if (identity == null)
			return -1;
		final Object id = identity.getValue(entity);
		if (id == null)
			return -1;
		if (Number.class.isAssignableFrom(identity.getDataType()) &&
				((Number) id).longValue() == 0L)
			return -1;
		return createDelete()
				.where(identity.getColumnName(), id)
				.execute();
	}

	/**
	 * This helper method deletes an entity by a specific member and returns the
	 * number of records affected by the delete command;
	 * 
	 * @param member
	 * @param entity
	 * @return
	 */
	protected int deleteByMember(DataAspectMember<T> member, T entity) {
		Object val = member.getValue(entity);
		if (val == null)
			return -1;
		return createDelete()
				.where(member.getColumnName(), val)
				.execute();
	}

	/**
	 * This method deletes an entity by a specific member and returns the number of
	 * records affected by the delete command;
	 * 
	 * @param name
	 * @param entity
	 * @return
	 */
	public int deleteByMember(final String name, final T entity) {
		return deleteByMember(getAspect().get(name), entity);
	}

	/**
	 * This method deletes an entity by it's composite key. If no composite key was
	 * annotated, it returns -1; Otherwise it returns the number of records affected
	 * by the delete command;
	 * 
	 * @param entity
	 * @return
	 */
	public int deleteByCompositeKey(final T entity) {
		final List<? extends DataAspectMember<T>> cks = getAspect().getCompositeKey();
		if (cks.isEmpty())
			return -1;
		DataAspectMember<T> ck = cks.get(0);
		final Delete delete = createDelete()
				.where(ck.getColumnName(),
						ck.getValue(entity));
		for (int i = 1; i < cks.size(); i++) {
			ck = cks.get(i);
			delete.andWhere(
					ck.getColumnName(),
					ck.getValue(entity));
		}
		return delete.execute();
	}

	/**
	 * This method attempts to delete an entity by it's identity, the by it's
	 * annotated unique members and lastly by it's composite key. If no combination
	 * of filters is found, an exception is throws, otherwise it returns the number
	 * of records affected by the delete command;
	 */
	@Override
	public int delete(final T entity) {

		boolean attemptWasMade = false;
		// tries to delete by identity
		int byid = deleteByIdentity(entity);
		if (byid > 0)
			return byid;
		else if (byid == 0)
			attemptWasMade = true;

		// tries to delete by an unique column
		for (DataAspectMember<T> member : getAspect().getUniqueColumns()) {
			int uq = deleteByMember(member, entity);
			if (uq > 0)
				return uq;
			else if (uq == 0)
				attemptWasMade = true;
		}

		// tries to delete by a composite key
		int ckid = deleteByCompositeKey(entity);
		if (ckid > 0)
			return ckid;
		else if (ckid == 0)
			attemptWasMade = true;

		// raises an exception if no attempt was made;
		if (attemptWasMade)
			return 0;
		else {
			throw new RuntimeException("Unable to find filters to delete " + entity
					+ ". Please try to annotate members as identity, unique or composite key "
					+ "and pass an argument that have one those values set.");
		}
	}

	/**
	 * This helper method updates an entity based on a specific member and a passed
	 * value. It returns the number of records affected by the update;
	 * 
	 * @param column
	 * @param value
	 * @param entity
	 * @return
	 */
	protected int updateByMember(DataAspectMember<T> column, Object value, T entity) {
		final Update update = createUpdate();
		for (DataAspectMember<T> member : getAspect()) {
			if (member.getColumnType() != ColumnType.IDENTITY && !column.equals(member)) {
				update.set(member.getColumnName(), member.getValue(entity));
			}
		}
		update.where(column.getColumnName(), value);
		return update.execute();
	}

	/**
	 * This method updates an entity by it's annotated identity; If no identity was
	 * annotated, -1 is returned. If the identity value on the entity is null or
	 * equal to Zero, 0 is returned; Otherwise it returns the number of records
	 * affected by the update;
	 * 
	 * @param entity
	 * @return
	 */
	public int updateByIdentity(final T entity) {
		final DataAspectMember<T> identity = getAspect().getIdentity();
		if (identity == null)
			return -1;
		final Object id = identity.getValue(entity);
		if (id == null)
			return 0;
		if (Number.class.isAssignableFrom(identity.getDataType()) &&
				((Number) id).longValue() == 0L)
			return 0;
		else
			return updateByMember(identity, id, entity);
	}

	/**
	 * This method updates an entity by a specific member and returns the number of
	 * records affected;
	 * 
	 * @param entity
	 * @param member
	 * @return
	 */
	public int updateByMember(final T entity, final String member) {
		DataAspectMember<T> aspectMember = getAspect().get(member);
		Object value = aspectMember.getValue(entity);
		return updateByMember(aspectMember, value, entity);
	}

	/**
	 * This method updates an entity by it's composite key. If no composite key was
	 * annotated, -1 is returned; Otherwise the number of records affected is
	 * returned;
	 * 
	 * @param entity
	 * @return
	 */
	public int updateByCompositeKey(final T entity) {
		List<? extends DataAspectMember<T>> cks = getAspect().getCompositeKey();
		if (cks.isEmpty())
			return -1;
		Update update = createUpdate();
		for (DataAspectMember<T> member : getAspect()) {
			ColumnType columnType = member.getColumnType();
			if (columnType != ColumnType.IDENTITY && columnType != ColumnType.COMPOSITE_KEY) {
				update.set(member.getColumnName(), member.getValue(entity));
			}
		}
		DataAspectMember<T> ck = cks.get(0);
		update.where(ck.getColumnName(), ck.getValue(entity));
		for (int i = 1; i < cks.size(); i++) {
			ck = cks.get(i);
			update.andWhere(ck.getColumnName(), ck.getValue(entity));
		}
		return update.execute();
	}

	/**
	 * This method tries to update an entity by it's identity, then by any annotated
	 * unique member and lastly by a composite key. If no combination of filters is
	 * found, an exception is thrown;
	 */
	@Override
	public int update(T entity) {

		boolean attemptWasMade = false;

		// tries to update by identity
		int byid = updateByIdentity(entity);
		if (byid > 0)
			return byid;
		else if (byid == 0)
			attemptWasMade = true;

		// tries to update by an unique column
		for (DataAspectMember<T> member : getAspect().getUniqueColumns()) {
			Object uqValue = member.getValue(entity);
			int uq = updateByMember(member, uqValue, entity);
			if (uq > 0)
				return uq;
			else if (uq == 0)
				attemptWasMade = true;
		}

		// tries to update by a composite key
		int ckid = updateByCompositeKey(entity);
		if (ckid > 0)
			return ckid;
		else if (ckid == 0)
			attemptWasMade = true;

		// raises an exception if no attempt was made;
		if (attemptWasMade)
			return 0;
		else {
			throw new RuntimeException("Unable to find filters to update " + entity
					+ ". Please try to annotate members as identity, unique or composite key "
					+ "and pass an argument that have one those values set.");
		}

	}

}
