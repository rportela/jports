package jports.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetAdapter<T> {

	public T process(ResultSet resultset) throws SQLException;
}
