package jports.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jports.data.Delete;

public class DatabaseDelete extends Delete {

	private final Database database;
	private final String target;

	public DatabaseDelete(Database database, String target) {
		this.database = database;
		this.target = target;
	}

	public int execute() {
		DatabaseCommand command = database.createCommand();
		try {
			return command
					.appendDelete(target, getFilter())
					.executeNonQuery();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				command.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
