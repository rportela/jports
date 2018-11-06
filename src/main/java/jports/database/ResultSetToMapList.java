package jports.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetToMapList implements ResultSetAdapter<List<Map<String, Object>>> {

	private final int offset;
	private final int limit;

	public ResultSetToMapList(int offset, int limit) {
		this.offset = offset;
		this.limit = limit <= 0 ?
				Integer.MAX_VALUE :
				limit;
	}

	public ResultSetToMapList() {
		this(0, 0);
	}

	@Override
	public List<Map<String, Object>> process(ResultSet resultset) throws SQLException {
		final ResultSetMetaData meta = resultset.getMetaData();
		final ArrayList<Map<String, Object>> list = new ArrayList<>(100);
		final int colCount = meta.getColumnCount();
		// skip offset
		for (int i = 0; i < offset && resultset.next(); i++)
			;
		int counter = 0;
		while (resultset.next() && (counter++ < limit)) {
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
