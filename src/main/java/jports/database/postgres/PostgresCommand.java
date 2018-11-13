package jports.database.postgres;

import jports.database.Database;
import jports.database.DatabaseCommand;

public class PostgresCommand extends DatabaseCommand {

	public PostgresCommand(Database database) {
		super(database);
	}

	@Override
	public String getNamePrefix() {
		return "\"";
	}

	@Override
	public String getNameSuffix() {
		return "\"";
	}

	@Override
	public boolean acceptsLimit() {
		return true;
	}

	@Override
	public boolean acceptsOffset() {
		return true;
	}

}
