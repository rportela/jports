package jports.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetToClassList<T> implements ResultSetAdapter<List<T>> {

	private final DatabaseAspect<T> aspect;

	public ResultSetToClassList(DatabaseAspect<T> aspect) {
		this.aspect = aspect;
	}

	@Override
	public List<T> process(ResultSet resultset) throws SQLException {
		final ResultSetMetaData metaData = resultset.getMetaData();
		final int colCount = metaData.getColumnCount();
		final ArrayList<Integer> indices = new ArrayList<>(Math.max(colCount, aspect.size()));
		final ArrayList<T> list = new ArrayList<>(100);
		for (int i = 1; i < colCount; i++) {
			String name = metaData.getColumnLabel(i);
			int ordinal = aspect.getColumnOrdinal(name);
			indices.add(ordinal);
		}
		while (resultset.next()) {
			T entity = aspect.newInstance();
			for (int i = 0; i < colCount; i++) {
				int ordinal = indices.get(i);
				if (ordinal >= 0) {
					Object value = resultset.getObject(i + 1);
					if (value != null) {
						aspect.get(ordinal).setValue(entity, value);
					}
				}
			}
		}
		return list;
	}
}
