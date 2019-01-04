package jports.database;

import java.sql.SQLException;

import jports.ShowStopper;
import jports.data.Update;

public class DatabaseUpdate extends Update {

	private final Database database;
	private final String target;

	public DatabaseUpdate(Database database, String target) {
		this.target = target;
		this.database = database;
	}

	public int execute() {
		try {
			return database
					.createCommand()
					.appendUpdate(target, getValues(), getFilter())
					.executeNonQuery();
		} catch (SQLException e) {
			throw new ShowStopper(e);
		}
	}

}
