package jports.database;

import java.sql.Connection;
import java.sql.SQLException;

import jports.data.Delete;

public class DatabaseDelete extends Delete {

	private final Database database;
	private final String target;

	public DatabaseDelete(Database database, String target) {
		this.database = database;
		this.target = target;
	}

	public int execute(Connection connection) throws SQLException {
		return database
				.createCommand()
				.appendDelete(target, getFilter())
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
