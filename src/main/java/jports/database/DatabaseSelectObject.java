package jports.database;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import jports.ShowStopper;
import jports.data.DataAspectMember;
import jports.data.Select;

public class DatabaseSelectObject<T> extends Select<T> {

	private Database database;
	private DatabaseAspect<T> aspect;

	public DatabaseSelectObject(Database database, DatabaseAspect<T> aspect) {
		this.database = database;
		this.aspect = aspect;
	}

	@Override
	public List<T> toList() {
		try {
			DatabaseCommand command = database
					.createCommand()
					.appendSql("SELECT ")
					.appendTop(getLimit())
					.appendNames(
							aspect
									.getColumns()
									.stream()
									.map(DataAspectMember::getColumnName)
									.collect(Collectors.toList()))
					.appendSql(" FROM ")
					.appendName(aspect.getDatabaseObject())
					.appendWhere(getFilter())
					.appendOrderBy(getSort())
					.appendOffset(getOffset())
					.appendLimit(getLimit());

			return command.executeQuery(
					new ResultSetToObjectList<>(
							aspect,
							database.acceptsOffset()
									? 0
									: getOffset(),
							database.acceptsLimit()
									? 0
									: getLimit()));
		} catch (SQLException e) {
			throw new ShowStopper(e);
		}
	}

	@Override
	public long count() {
		try {
			Object obj = database.createCommand()
					.appendSql("SELECT COUNT(*) FROM ")
					.appendName(aspect.getDatabaseObject())
					.appendWhere(getFilter())
					.executeQuery(r -> r.getObject(1));
			return obj == null
					? 0L
					: ((Number) obj).longValue();
		} catch (SQLException e) {
			throw new ShowStopper(e);
		}
	}

	@Override
	public boolean exists() {
		try {
			Object obj = database.createCommand()
					.appendSql("SELECT 1 WHERE EXISTS (SELECT * FROM ")
					.appendName(aspect.getDatabaseObject())
					.appendWhere(getFilter())
					.appendSql(")")
					.executeQuery(r -> r.getObject(1));
			return obj != null && ((Number) obj).intValue() == 1;
		} catch (SQLException e) {
			throw new ShowStopper(e);
		}

	}

	@Override
	public T first() {
		try {
			DatabaseCommand command = database
					.createCommand()
					.appendSql("SELECT ")
					.appendTop(1)
					.appendNames(
							aspect
									.getColumns()
									.stream()
									.map(DataAspectMember::getColumnName)
									.collect(Collectors.toList()))
					.appendSql(" FROM ")
					.appendName(aspect.getDatabaseObject())
					.appendWhere(getFilter())
					.appendOrderBy(getSort())
					.appendOffset(getOffset())
					.appendLimit(1);

			return command.executeQuery(
					new ResultSetToObject<>(
							aspect,
							database.acceptsOffset()
									? 0
									: getOffset()));
		} catch (SQLException e) {
			throw new ShowStopper(e);
		}
	}

}
