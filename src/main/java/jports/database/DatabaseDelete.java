package jports.database;

import java.sql.Connection;
import java.sql.SQLException;

import jports.data.Delete;
import jports.data.FilterExpression;

public class DatabaseDelete extends Delete {

	private final Database database;
	private final String target;

	public DatabaseDelete(Database database, String target) {
		this.database = database;
		this.target = target;
	}

	public DatabaseCommand createDeleteCommand() {
		DatabaseCommand command = database
				.createCommand()
				.appendSql("DELETE FROM ")
				.appendName(target);
		FilterExpression filter = getFilter();
		return filter == null
				? command
				: command
						.appendSql(" WHERE ")
						.appendFilterExpression(filter);
	}

	public int execute(Connection connection) throws SQLException {
		return createDeleteCommand().executeNonQuery(connection);
	}

	@Override
	public int execute() {
		try {
			return createDeleteCommand().executeNonQuery();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
