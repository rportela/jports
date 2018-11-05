package jports.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import jports.data.Insert;

public class DatabaseInsert extends Insert {

	private final Database database;
	private final String target;

	public DatabaseInsert(Database database, String table) {
		this.target = table;
		this.database = database;
	}

	public DatabaseCommand createCommand() {
		return database.createCommand()
				.appendSql("INSERT INTO ")
				.appendName(target)
				.appendSql(" (")
				.appendNames(getValues().keySet())
				.appendSql(") VALUES (")
				.appendValues(getValues().values())
				.appendSql(")");
	}

	public int execute(Connection conn) throws SQLException {
		return createCommand().executeNonQuery(conn);
	}

	public int execute() {
		try {
			return createCommand().executeNonQuery();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, Object> executeWithGeneratedKeys(Connection conn) throws SQLException {
		return createCommand().executeWithGeneratedKeys(conn);
	}

	public Map<String, Object> executeWithGeneratedKeys() {
		try {
			return createCommand().executeWithGeneratedKeys();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
