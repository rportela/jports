package jports.database;

import java.util.List;
import java.util.Map;

import jports.data.FilterExpression;
import jports.data.Upsert;

public class DatabaseUpsert extends Upsert {

	private final Database database;
	private final String target;

	public DatabaseUpsert(Database database, String target) {
		this.database = database;
		this.target = target;
	}

	private FilterExpression createFilter() {
		List<String> keys2 = getKeys();
		if (keys2.isEmpty())
			return null;
		String keyName = keys2.get(0);
		Object keyValue = get(keyName);
		FilterExpression exp = new FilterExpression(keyName, keyValue);
		for (int i = 1; i < keys2.size(); i++) {
			keyName = keys2.get(i);
			keyValue = get(keyName);
			exp.and(keyName, keyValue);
		}
		return exp;
	}

	@Override
	public int execute() {
		try {
			DatabaseCommand command = database.createCommand();
			FilterExpression flt = createFilter();
			Map<String, Object> vls = getValues();
			return command
					.appendSql("IF NOT EXISTS (SELECT * FROM ")
					.appendName(target)
					.appendSql(" WHERE ")
					.appendFilter(flt)
					.appendSql(")\r\n INSERT INTO ")
					.appendName(target)
					.appendSql(" (")
					.appendNames(vls.keySet())
					.appendSql(") VALUES (")
					.appendValues(vls.values())
					.appendSql(");\r\n ELSE UPDATE ")
					.appendName(target)
					.appendSql(" SET ")
					.appendNameValue(vls)
					.appendSql(" WHERE ")
					.appendFilter(flt)
					.appendSql(";")
					.executeNonQuery();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
