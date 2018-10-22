package jports.database;

import java.sql.Connection;
import java.sql.SQLException;

import jports.data.Delete;
import jports.data.FilterExpression;

public class DatabaseDelete extends Delete<String> {

	public final Database database;

	public DatabaseDelete(Database database, String target) {
		super(target);
		this.database = database;
	}

	public DatabaseCommand createDeleteCommand() {
		DatabaseCommand command = database
				.createCommand()
				.appendSql("DELETE FROM ")
				.appendName(getTarget());
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
