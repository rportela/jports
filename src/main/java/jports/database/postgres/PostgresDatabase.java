package jports.database.postgres;

import java.util.Properties;

import jports.database.Database;
import jports.database.DatabaseCommand;
import jports.database.DatabaseUpsert;

public class PostgresDatabase extends Database {

	public PostgresDatabase(String jdbcUrl, Properties properties) throws ClassNotFoundException {
		super(jdbcUrl, properties);
	}

	public PostgresDatabase(String jdbcUrl) throws ClassNotFoundException {
		super(jdbcUrl);
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

	@Override
	public String getNamePrefix() {
		return "\"";
	}

	@Override
	public String getNameSuffix() {
		return "\"";
	}

}
