package jports.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jports.data.ColumnType;
import jports.data.Table;

public class ResultSetAdapterToTable implements ResultSetAdapter<Table> {

	@Override
	public Table process(ResultSet resultset) throws SQLException {
		ResultSetMetaData metaData = resultset.getMetaData();
		Table table = new Table();
		int colCount = metaData.getColumnCount();
		for (int i = 1; i <= colCount; i++) {
			table.addColumn(metaData.getColumnLabel(i), ColumnType.REGULAR);
		}
		while (resultset.next()) {
			Object[] arr = new Object[colCount];
			for (int i = 0; i < arr.length; i++)
				arr[i] = resultset.getObject(i + 1);
			table.addRow(arr);
		}
		return table;
	}

}
