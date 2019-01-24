package jports.database;

import java.util.List;
import java.util.Map;

import jports.ShowStopper;
import jports.data.FilterExpression;
import jports.data.Upsert;

public class DatabaseUpsert extends Upsert {

	protected final Database database;
	protected final String target;

	public DatabaseUpsert(Database database, String target) {
		this.database = database;
		this.target = target;
	}

	protected FilterExpression createFilter() {
		List<String> filterKeys = getKeys();
		if (filterKeys.isEmpty())
			return null;
		String keyName = filterKeys.get(0);
		Object keyValue = get(keyName);
		FilterExpression exp = new FilterExpression(keyName, keyValue);
		for (int i = 1; i < filterKeys.size(); i++) {
			keyName = filterKeys.get(i);
			keyValue = get(keyName);
			exp.and(keyName, keyValue);
		}
		return exp;
	}

	public DatabaseCommand createCommand() {
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
				.appendValueList(vls.values())
				.appendSql(");\r\n ELSE UPDATE ")
				.appendName(target)
				.appendSql(" SET ")
				.appendNameValue(vls)
				.appendSql(" WHERE ")
				.appendFilter(flt)
				.appendSql(";");
	}

	@Override
	public void execute() {
		try {
			createCommand().execute();
		} catch (Exception e) {
			throw new ShowStopper(e);
		}

	}
}
