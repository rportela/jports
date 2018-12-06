package jports.database.postgres;

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
		return new PostgresCommand(this);
	}

}
