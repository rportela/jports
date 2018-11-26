package jports.database.postgres;

import java.sql.SQLException;

import jports.database.Database;
import jports.database.DatabaseCommand;

public class PostgresDatabase extends Database {

	public PostgresDatabase(String jdbcUrl, String username, String password) {
		super(jdbcUrl, username, password);
	}

	@Override
	public String getDriverClass() {
		return "org.postgresql.Driver";
	}

	@Override
	public DatabaseCommand createCommand() {
		try {
			return new PostgresCommand(getConnection().createStatement());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
