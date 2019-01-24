package jports.database.sqlserver;

import jports.database.Database;
import jports.database.DatabaseCommand;

public class SqlServerCommand extends DatabaseCommand {

	public SqlServerCommand(Database database) {
		super(database);
	}

	@Override
	public DatabaseCommand appendBoolean(Boolean value) {
		return super.appendSql(value
				? "1"
				: "0");
	}

}
