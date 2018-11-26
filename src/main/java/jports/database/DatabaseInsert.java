package jports.database;

import java.sql.SQLException;
import java.util.Map;

import jports.data.Insert;

public class DatabaseInsert extends Insert {

	private final Database database;
	private final String target;

	public DatabaseInsert(Database database, String table) {
		this.target = table;
		this.database = database;
	}

	public int execute() {
		try {
			return database
					.createCommand()
					.appendInsert(target, getValues())
					.executeNonQuery();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, Object> executeWithGeneratedKeys() {
		try {
			return database
					.createCommand()
					.appendInsert(target, getValues())
					.executeWithGeneratedKeys();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
