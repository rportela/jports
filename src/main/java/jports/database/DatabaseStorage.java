package jports.database;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import jports.data.DataAspect;
import jports.data.DataAspectMember;
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

	public final Database database;
	public final DatabaseAspect<T> aspect;

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
		return database.upsert(aspect.getDatabaseObject());
	}

	@Override
	public Select<T> select() {
		return new DatabaseSelectObject<T>(database, aspect);
	}

	@Override
	public DataAspect<T, ?> getAspect() {
		return aspect;
	}

	public synchronized void saveBatch(Stream<T> stream) {
		final DatabaseUpsert upsert = (DatabaseUpsert) createUpsert();

		DataAspectMember<T> identity = aspect.getIdentity();
		List<DataAspectMember<T>> uniqueColumns = aspect.getUniqueColumns();
		List<DataAspectMember<T>> compositeKey = aspect.getCompositeKey();

		// Is it an upsert with identity?
		if (identity != null) {
			upsert.addKey(identity.getColumnName());
		}
		// Is it an upsert with an unique column ?
		else if (!uniqueColumns.isEmpty()) {
			upsert.addKey(uniqueColumns.get(0).getColumnName());
		}
		// Is it an upsert with a composite key ?
		else if (!compositeKey.isEmpty()) {
			for (DataAspectMember<T> ckey : compositeKey)
				upsert.addKey(ckey.getColumnName());
		}
		// Is it an upsert with no key?! throw an exception
		else {
			throw new RuntimeException("We couldn't find a suitable key " +
					"to perform upserts on " + aspect.getDataType());
		}

		final StringBuilder cmdBuilder = new StringBuilder(100000);

		stream.forEach(new Consumer<T>() {
			int counter = 0;

			@Override
			public void accept(T arg0) {
				upsert.clear();
				for (DataAspectMember<T> member : aspect) {
					upsert.set(member.getColumnName(), member.getValue(arg0));
				}
				final String cmdText = upsert.createCommand().appendSql(";").toString();
				cmdBuilder.append(cmdText);
				counter++;
				if (counter >= 1000) {
					try {
						database
								.createCommand()
								.appendSql(cmdBuilder.toString())
								.execute();

					} catch (Exception e) {
						throw new RuntimeException(e);
					}

					cmdBuilder.delete(0, cmdBuilder.length());
					counter = 0;
				}
			}
		});

		if (cmdBuilder.length() > 0) {
			try {
				database
						.createCommand()
						.appendSql(cmdBuilder.toString())
						.execute();

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

}
