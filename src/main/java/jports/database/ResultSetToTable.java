package jports.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jports.data.ColumnType;
import jports.data.Table;

public class ResultSetToTable implements ResultSetAdapter<Table> {

	@Override
	public Table process(final ResultSet resultset) throws SQLException {
		final ResultSetMetaData metaData = resultset.getMetaData();
		final Table table = new Table();
		final int colCount = metaData.getColumnCount();
		for (int i = 1; i <= colCount; i++) {
			table.addColumn(
					metaData.getColumnLabel(i),
					ColumnType.REGULAR);
		}
		while (resultset.next()) {
			final Object[] arr = new Object[colCount];
			for (int i = 0; i < arr.length; i++)
				arr[i] = resultset.getObject(i + 1);
			table.addRow(arr);
		}
		return table;
	}

}
