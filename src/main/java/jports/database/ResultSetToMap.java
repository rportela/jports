package jports.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ResultSetToMap implements ResultSetAdapter<Map<String, Object>> {

	@Override
	public Map<String, Object> process(final ResultSet resultset) throws SQLException {
		if (!resultset.next())
			return null;
		final ResultSetMetaData metaData = resultset.getMetaData();
		final int colCount = metaData.getColumnCount();
		final Map<String, Object> hashmap = new HashMap<>(colCount);
		for (int i = 1; i <= colCount; i++) {
			String name = metaData.getColumnLabel(i);
			Object value = resultset.getObject(i);
			hashmap.put(name, value);
		}
		return hashmap;
	}

}
