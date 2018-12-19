package jports.database.postgres;

import jports.database.Database;
import jports.database.DatabaseCommand;
import jports.database.DatabaseUpsert;

public class PostgresDatabase extends Database {

	public PostgresDatabase(String jdbcUrl, String username, String password) throws ClassNotFoundException {
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

	@Override
	public DatabaseUpsert upsert(String target) {
		return new PostgresUpsert(this, target);
	}

}
