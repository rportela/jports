package jports.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jports.ShowStopper;
import jports.data.Select;

public class DatabaseSelectRow extends Select<Map<String, Object>> {

	private final Database database;
	private final String objectName;
	private final ArrayList<String> columns = new ArrayList<>();

	public DatabaseSelectRow(Database database, String objectName) {
		this.database = database;
		this.objectName = objectName;
	}

	public DatabaseSelectRow addColumn(String name) {
		this.columns.add(name);
		return this;
	}

	public DatabaseSelectRow addColumns(String... names) {
		for (int i = 0; i < names.length; i++)
			columns.add(names[i]);
		return this;
	}

	@Override
	public List<Map<String, Object>> toList() {
		try {
			DatabaseCommand command = database
					.createCommand()
					.appendSql("SELECT ")
					.appendTop(getLimit());

			if (columns.isEmpty()) {
				command.appendSql("*");
			} else {
				command.appendNames(columns);
			}

			return command.appendSql(" FROM ")
					.appendName(objectName)
					.appendWhere(getFilter())
					.appendOrderBy(getSort())
					.appendOffset(getOffset())
					.appendLimit(getLimit())
					.executeQuery(new ResultSetToMapList(
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
					.appendName(objectName)
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
					.appendName(objectName)
					.appendWhere(getFilter())
					.appendSql(")")
					.executeQuery(r -> r.getObject(1));
			return obj != null && ((Number) obj).intValue() == 1;
		} catch (SQLException e) {
			throw new ShowStopper(e);
		}

	}

	@Override
	public Map<String, Object> first() {
		try {
			DatabaseCommand command = database
					.createCommand()
					.appendSql("SELECT ")
					.appendTop(1);

			if (columns.isEmpty()) {
				command.appendSql("*");
			} else {
				command.appendNames(columns);
			}

			return command.appendSql(" FROM ")
					.appendName(objectName)
					.appendWhere(getFilter())
					.appendOrderBy(getSort())
					.appendLimit(1)
					.executeQuery(new ResultSetToMap(
							database.acceptsOffset()
									? 0
									: getOffset()));
		} catch (SQLException e) {
			throw new ShowStopper(e);
		}
	}

}
