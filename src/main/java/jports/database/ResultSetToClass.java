package jports.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jports.data.DataAspectMember;

public class ResultSetToClass<T> implements ResultSetAdapter<T> {

	private final DatabaseAspect<T> aspect;

	public ResultSetToClass(DatabaseAspect<T> aspect) {
		this.aspect = aspect;
	}

	@Override
	public T process(final ResultSet resultset) throws SQLException {
		if (!resultset.next())
			return null;
		final T entity = aspect.newInstance();
		final ResultSetMetaData metaData = resultset.getMetaData();
		final int colCount = metaData.getColumnCount();
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
