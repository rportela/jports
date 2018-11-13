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

	public int execute(Connection connection) throws SQLException {
		return database
				.createCommand()
				.appendInsert(target, getValues())
				.executeNonQuery(connection);
	}

	@Override
	public int execute() {
		try (final Connection connection = database.getConnection()) {
			try {
				return execute(connection);
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, Object> executeWithGeneratedKeys(Connection conn) throws SQLException {
		return database
				.createCommand()
				.appendInsert(target, getValues())
				.executeWithGeneratedKeys(conn);

	}

	public Map<String, Object> executeWithGeneratedKeys() {
		try (final Connection connection = database.getConnection()) {
			try {
				return executeWithGeneratedKeys(connection);
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
