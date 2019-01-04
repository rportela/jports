package jports.database;

import java.sql.SQLException;

import jports.ShowStopper;
import jports.data.Delete;

public class DatabaseDelete extends Delete {

	private final Database database;
	private final String target;

	public DatabaseDelete(Database database, String target) {
		this.database = database;
		this.target = target;
	}

	public int execute() {
		try {
			return database
					.createCommand()
					.appendDelete(target, getFilter())
					.executeNonQuery();
		} catch (SQLException e) {
			throw new ShowStopper(e);
		}

	}

}
