package jports.database;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jports.ShowStopper;
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
		return new DatabaseSelectObject<>(database, aspect);
	}

	@Override
	public DataAspect<T, DataAspectMember<T>> getAspect() {
		return aspect;
	}

	private int insertBulk(final Iterator<T> iterator) throws SQLException {
		DatabaseCommand command = database
				.createCommand()
				.appendSql("INSERT INTO ")
				.appendName(aspect.getDatabaseObject());

		List<DataAspectMember<T>> members = aspect.getMembers();

		command.appendSql(" (")
				.appendNames(members
						.stream()
						.map(DataAspectMember::getColumnName)
						.collect(Collectors.toList()))
				.appendSql(" ) VALUES ");

		int itemcount = 0;
		boolean prepComma = false;
		while (iterator.hasNext()) {
			if (prepComma) {
				command.appendSql(", ");
			} else {
				prepComma = true;
			}

			T entity = iterator.next();

			command
					.appendSql("(")
					.appendValueList(
							members
									.stream()
									.map(m -> m.getValue(entity))
									.collect(Collectors.toList()))
					.appendSql(")");

			itemcount++;
			if (itemcount >= 1000) {
				return command.executeNonQuery() + insertBulk(iterator);
			}
		}
		if (itemcount > 0) {
			return command.executeNonQuery();
		} else {
			return 0;
		}

	}

	@Override
	public int insert(final Iterable<T> entities) {
		DataAspectMember<T> identity = aspect.getIdentity();
		try {
			return identity != null
					? super.insert(entities)
					: this.insertBulk(entities.iterator());
		} catch (SQLException e) {
			throw new ShowStopper(e);
		}
	}

	private Consumer<T> createSaveBatchConsumer(
			Database database,
			DataAspect<T, DataAspectMember<T>> aspect,
			DatabaseUpsert upsert,
			StringBuilder cmdBuilder) {
		return new Consumer<T>() {
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
						throw new ShowStopper(e);
					}

					cmdBuilder.delete(0, cmdBuilder.length());
					counter = 0;
				}
			}
		};
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
			throw new ShowStopper("We couldn't find a suitable key " +
					"to perform upserts on " +
					aspect.getDataType());
		}

		final StringBuilder cmdBuilder = new StringBuilder(100000);
		stream.forEach(createSaveBatchConsumer(database, aspect, upsert, cmdBuilder));
		if (cmdBuilder.length() > 0) {
			try {
				database
						.createCommand()
						.appendSql(cmdBuilder.toString())
						.execute();

			} catch (Exception e) {
				throw new ShowStopper(e);
			}
		}

	}

}
