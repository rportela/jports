package jports.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jports.data.DataAspectMember;

public class ResultSetAdapterToClass<T> implements ResultSetAdapter<T> {

	private final DatabaseAspect<T> aspect;

	public ResultSetAdapterToClass(DatabaseAspect<T> aspect) {
		this.aspect = aspect;
	}

	@Override
	public T process(ResultSet resultset) throws SQLException {
		if (!resultset.next())
			return null;
		T entity = aspect.newInstance();
		ResultSetMetaData metaData = resultset.getMetaData();
		int colCount = metaData.getColumnCount();
		for (int i = 1; i <= colCount; i++) {
			Object value = resultset.getObject(i);
			if (value != null) {
				String name = metaData.getColumnLabel(i);
				DataAspectMember<T> member = aspect.getColumn(name);
				if (member != null) {
					member.setValue(entity, value);
				}
			}
		}
		return entity;
	}

}
