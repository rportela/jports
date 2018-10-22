package jports.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import jports.data.Insert;

public class DatabaseInsert extends Insert<String> {

	public final Database database;

	public DatabaseInsert(Database database, String table) {
		super(table);
		this.database = database;
	}

	public DatabaseCommand createCommand() {
		return database.createCommand()
				.appendSql("INSERT INTO ")
				.appendName(getTarget())
				.appendSql(" (")
				.appendNames(getValues().keySet())
				.appendSql(") VALUES (")
				.appendValues(getValues().values())
				.appendSql(")");
	}

	public void execute(Connection conn) throws SQLException {
		createCommand().execute(conn);
	}

	public void execute() {
		try {
			createCommand().execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, Object> executeWithGeneratedKeys(Connection conn) throws SQLException {
		return createCommand().executeWithGeneratedKeys(conn);
	}

	public Map<String, Object> executeWithGeneratedKeys() {
		try {
			return createCommand().executeWithGeneratedKeys();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
