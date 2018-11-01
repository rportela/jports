package jports.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetAdapterToScalar implements ResultSetAdapter<Object> {

	@Override
	public Object process(ResultSet resultset) throws SQLException {
		return resultset.next()
				? resultset.getObject(1)
				: null;
	}

}
