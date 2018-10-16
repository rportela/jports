package jports.database;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class DatabaseInsert {

	public final Database database;

	public final String table;

	public final HashMap<String, Object> values = new LinkedHashMap<>();

	public final HashMap<String, Object> generatedKeys = new LinkedHashMap<>();

	public DatabaseInsert(Database database, String table) {
		this.database = database;
		this.table = table;
	}

	public String createCommandText() {
		database.validateNameOrRaiseException(table);
		StringBuilder sqlBuilder = new StringBuilder(512);
		StringBuilder valueBuilder = new StringBuilder(255);
		String namePrefix = database.getNamePrefix();
		String nameSuffix = database.getNameSuffix();
		boolean prependComma = false;

		sqlBuilder.append("INSERT INTO ");
		sqlBuilder.append(namePrefix);
		sqlBuilder.append(table);
		sqlBuilder.append(nameSuffix);
		sqlBuilder.append(" (");

		valueBuilder.append(") VALUES (");
		for (String key : values.keySet()) {

			database.validateNameOrRaiseException(key);

			if (prependComma) {
				sqlBuilder.append(", ");
				valueBuilder.append(", ");
			} else {
				prependComma = true;
			}

			sqlBuilder.append(namePrefix);
			sqlBuilder.append(key);
			sqlBuilder.append(nameSuffix);
			valueBuilder.append("?");
		}
		valueBuilder.append(")");
		sqlBuilder.append(valueBuilder.toString());
		return sqlBuilder.toString();
	}

	public void execute(Connection connection, boolean getGeneratedKeys) {
		this.generatedKeys.clear();

	}

	public void execute(Connection connection) {
		execute(connection, true);
	}
}
