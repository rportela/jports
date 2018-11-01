package jports.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetAdapterToMapList implements ResultSetAdapter<List<Map<String, Object>>> {

	@Override
	public List<Map<String, Object>> process(ResultSet resultset) throws SQLException {
		ResultSetMetaData meta = resultset.getMetaData();
		ArrayList<Map<String, Object>> list = new ArrayList<>(100);
		int colCount = meta.getColumnCount();
		while (resultset.next()) {
			HashMap<String, Object> map = new HashMap<>(colCount);
			for (int i = 1; i <= colCount; i++) {
				String name = meta.getColumnLabel(i);
				Object val = resultset.getObject(i);
				map.put(name, val);
			}
			list.add(map);
		}
		return list;
	}

}
