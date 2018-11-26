package jports.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jports.data.DataAspectMember;

public class ResultSetToObject<T> implements ResultSetAdapter<T> {

	private final DatabaseAspect<T> aspect;
	private final int offset;

	public ResultSetToObject(DatabaseAspect<T> aspect, int offset) {
		this.aspect = aspect;
		this.offset = offset;
	}

	public ResultSetToObject(DatabaseAspect<T> aspect) {
		this(aspect, 0);
	}

	@Override
	public T process(final ResultSet resultset) throws SQLException {
		for (int i = 0; i < offset && resultset.next(); i++)
			;
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
