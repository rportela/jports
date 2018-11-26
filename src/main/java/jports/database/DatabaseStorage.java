package jports.database;

import jports.data.DataAspect;
import jports.data.DataStorage;
import jports.data.Delete;
import jports.data.Insert;
import jports.data.Select;
import jports.data.Update;
import jports.data.Upsert;

/**
 * This class wraps a data aspect and uses database commands to perform inserts,
 * updates, deletes and selects.
 * 
 * @author rportela
 *
 * @param <T>
 */
public class DatabaseStorage<T> extends DataStorage<T> {

	private final Database database;
	private final DatabaseAspect<T> aspect;

	public DatabaseStorage(Database database, DatabaseAspect<T> aspect) {
		this.database = database;
		this.aspect = aspect;
	}

	public DatabaseStorage(Database database, Class<T> dataType) {
		this(database, DatabaseAspect.getInstance(dataType));
	}

	@Override
	public Insert createInsert() {
		return database.insert(aspect.getDatabaseObject());
	}

	@Override
	public Delete createDelete() {
		return database.delete(aspect.getDatabaseObject());
	}

	@Override
	public Update createUpdate() {
		return database.update(aspect.getDatabaseObject());
	}

	@Override
	public Upsert createUpsert() {
		return new DatabaseUpsert(database, aspect.getDatabaseObject());
	}

	@Override
	public Select<T> select() {
		return new DatabaseSelectObject<T>(database, aspect);
	}

	@Override
	public DataAspect<T, ?> getAspect() {
		return aspect;
	}

}
