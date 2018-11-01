package jports.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResultSetAdapterToClassList<T> implements ResultSetAdapter<List<T>> {

	private final DatabaseAspect<T> aspect;

	@Override
	public List<T> process(ResultSet resultset) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
