package jports.database;

import java.sql.Connection;
import java.sql.SQLException;

import jports.data.Update;

public class DatabaseUpdate extends Update {

	private final Database database;
	private final String target;

	public DatabaseUpdate(Database database, String target) {
		this.target = target;
		this.database = database;
	}
	public int execute(Connection connection) throws SQLException {
		return database
				.createCommand()
				.appendUpdate(target, getValues(), getFilter())
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

}
