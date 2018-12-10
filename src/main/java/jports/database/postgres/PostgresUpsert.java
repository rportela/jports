package jports.database.postgres;

import java.util.Map;
import java.util.Map.Entry;

import jports.database.Database;
import jports.database.DatabaseCommand;
import jports.database.DatabaseUpsert;

public class PostgresUpsert extends DatabaseUpsert {

	public PostgresUpsert(Database database, String target) {
		super(database, target);
	}

	@Override
	public DatabaseCommand createCommand() {
		Map<String, Object> map = getValues();
		DatabaseCommand command = database
				.createCommand()
				.appendInsert(target, map)
				.appendSql(" ON CONFLICT (")
				.appendNames(getKeys())
				.appendSql(") DO UPDATE SET ");

		boolean prependComma = false;
		for (Entry<String, Object> entry : map.entrySet()) {
			String colname = entry.getKey();
			if (containsKey(colname))
				continue;
			if (prependComma)
				command.appendSql(", ");
			else
				prependComma = true;
			command
					.appendName(colname)
					.appendSql("=EXCLUDED.").appendName(colname);
		}
		command.appendSql(";");
		return command;
	}

}
