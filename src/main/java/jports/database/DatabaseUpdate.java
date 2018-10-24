package jports.database;

import java.sql.SQLException;
import java.util.Map.Entry;

import jports.data.FilterExpression;
import jports.data.Update;

public class DatabaseUpdate extends Update<String> {

	private final Database database;

	public DatabaseUpdate(Database database, String target) {
		super(target);
		this.database = database;
	}

	@Override
	public int execute() {
		DatabaseCommand command = database.createCommand()
				.appendSql("UPDATE ")
				.appendName(getTarget())
				.appendSql(" SET ");

		// appends the values;
		boolean prependComma = false;
		for (Entry<String, Object> entry : getValues().entrySet()) {
			if (prependComma)
				command.appendSql(", ");
			else
				prependComma = true;
			command.appendName(entry.getKey());
			command.appendSql("=");
			command.appendValue(entry.getValue());
		}
		FilterExpression filter2 = getFilter();
		if (filter2 != null) {
			command.appendSql(" WHERE ");
			command.appendFilter(filter2);
		}
		try {
			return command.executeNonQuery();
		} catch (SQLException e) {
			throw new RuntimeException(command.toString(), e);
		}
	}

}
